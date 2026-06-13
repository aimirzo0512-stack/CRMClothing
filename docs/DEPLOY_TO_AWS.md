# Deploy to AWS EC2

This guide covers a single-VM deployment using Docker Compose, DuckDNS for a
free hostname, and Nginx + Let's Encrypt for HTTPS.

## 1. Launch an EC2 instance
1. AMI: **Ubuntu 22.04 LTS**
2. Type: **t3.small** (2 GB RAM minimum — JVM + Postgres)
3. Storage: 16 GB gp3
4. Security group inbound rules:
   - 22/tcp  (SSH, your IP)
   - 80/tcp  (HTTP, anywhere)
   - 443/tcp (HTTPS, anywhere)
5. Create / download a key pair (`crm-key.pem`)

SSH in:

```bash
chmod 400 crm-key.pem
ssh -i crm-key.pem ubuntu@<EC2-PUBLIC-IP>
```

## 2. Install Docker + Compose

```bash
sudo apt update && sudo apt -y upgrade
sudo apt -y install docker.io docker-compose-plugin git nginx certbot python3-certbot-nginx curl
sudo usermod -aG docker $USER
newgrp docker
```

## 3. DuckDNS hostname (free)
1. Go to https://www.duckdns.org, sign in with GitHub/Google.
2. Create a subdomain, e.g. `mycrm.duckdns.org`.
3. Point it to your EC2 public IP.
4. Auto-update IP every 5 min:

```bash
mkdir -p ~/duckdns && cd ~/duckdns
cat > duck.sh <<'EOF'
echo url="https://www.duckdns.org/update?domains=mycrm&token=YOUR_DUCKDNS_TOKEN&ip=" \
  | curl -k -o ~/duckdns/duck.log -K -
EOF
chmod 700 duck.sh
( crontab -l 2>/dev/null; echo "*/5 * * * * ~/duckdns/duck.sh >/dev/null 2>&1" ) | crontab -
```

## 4. Get the project on the server

```bash
git clone https://github.com/<you>/clothing-crm.git
cd clothing-crm
```

Or use SCP / the GitHub Actions workflow (see below).

## 5. Run the stack

```bash
docker compose up -d --build
```

Verify:

```bash
docker compose ps
curl http://localhost            # frontend
curl http://localhost:8080/api/auth/login -X POST -H "Content-Type: application/json" \
     -d '{"username":"admin","password":"admin123"}'
```

## 6. Nginx reverse proxy + HTTPS

Free port 80 on the host (the frontend container exposed it). Change `docker-compose.yml`
to bind frontend on `127.0.0.1:8000:80` and backend on `127.0.0.1:8080:8080`,
or simply stop the frontend container and let host Nginx serve the static files.

The simplest setup is **host Nginx → frontend container (port 8000) → backend container (port 8080)**.
Edit the ports in `docker-compose.yml`:

```yaml
backend:   ports: ["127.0.0.1:8080:8080"]
frontend:  ports: ["127.0.0.1:8000:80"]
```

Then create `/etc/nginx/sites-available/crm`:

```nginx
server {
    listen 80;
    server_name mycrm.duckdns.org;

    location /api/ {
        proxy_pass         http://127.0.0.1:8080/api/;
        proxy_set_header   Host              $host;
        proxy_set_header   X-Real-IP         $remote_addr;
        proxy_set_header   X-Forwarded-For   $proxy_add_x_forwarded_for;
        proxy_set_header   X-Forwarded-Proto $scheme;
    }

    location / {
        proxy_pass         http://127.0.0.1:8000/;
        proxy_set_header   Host              $host;
        proxy_set_header   X-Real-IP         $remote_addr;
    }
}
```

Enable + reload + issue cert:

```bash
sudo ln -s /etc/nginx/sites-available/crm /etc/nginx/sites-enabled/crm
sudo nginx -t && sudo systemctl reload nginx
sudo certbot --nginx -d mycrm.duckdns.org --redirect --agree-tos -m you@example.com -n
```

The frontend will now reach the backend via the same origin at `/api/...`.
Edit `frontend/js/api.js` if you want a different prefix; by default it
uses `/api-proxy` for non-localhost, and the in-container Nginx forwards
`/api-proxy/ → backend:8080/`. If you put host Nginx in front, point the
frontend at `/api` and proxy that path instead.

## 7. GitHub Actions auto-deploy
Add these repo **Settings → Secrets and variables → Actions** secrets:

| Secret       | Value                               |
|--------------|-------------------------------------|
| EC2_HOST     | mycrm.duckdns.org (or public IP)    |
| EC2_USER     | ubuntu                              |
| EC2_SSH_KEY  | contents of `crm-key.pem`           |

Push to `main` → the workflow in `.github/workflows/deploy.yml` will SCP the
project to the EC2 box and run `docker compose up -d --build`.

## 8. Backup the database

```bash
docker compose exec -T postgres pg_dump -U crm clothingcrm > backup-$(date +%F).sql
```

## 9. Production hardening checklist
- [ ] Change `JWT_SECRET` in `docker-compose.yml` (a long random string).
- [ ] Change the default `admin` and `employee` passwords after first login.
- [ ] Lock down the PostgreSQL port (don't expose 5432 publicly).
- [ ] Set `CORS_ORIGINS=https://mycrm.duckdns.org`.
- [ ] Schedule daily `pg_dump` to S3.
