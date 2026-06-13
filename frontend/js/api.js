// API base URL. Override by setting window.API_BASE before this script loads.
window.API_BASE = window.API_BASE || (location.hostname === "localhost" ? "http://localhost:8081" : "/api-proxy");

const TOKEN_KEY = "crm_token";
const USER_KEY  = "crm_user";

function getToken() { return localStorage.getItem(TOKEN_KEY); }
function setAuth(data) {
  localStorage.setItem(TOKEN_KEY, data.token);
  localStorage.setItem(USER_KEY, JSON.stringify({
    username: data.username, fullName: data.fullName, roles: data.roles || []
  }));
}
function getUser() { try { return JSON.parse(localStorage.getItem(USER_KEY) || "null"); } catch { return null; } }
function clearAuth() { localStorage.removeItem(TOKEN_KEY); localStorage.removeItem(USER_KEY); }
function isAdmin() { const u = getUser(); return u && (u.roles || []).includes("ADMIN"); }

async function api(path, opts = {}) {
  const headers = Object.assign({ "Content-Type": "application/json" }, opts.headers || {});
  const t = getToken();
  if (t) headers["Authorization"] = "Bearer " + t;
  const res = await fetch(window.API_BASE + path, Object.assign({}, opts, { headers }));
  if (res.status === 401) { clearAuth(); location.href = "login.html"; throw new Error("Unauthorized"); }
  const text = await res.text();
  const body = text ? JSON.parse(text) : null;
  if (!res.ok) throw new Error((body && body.error) || res.statusText);
  return body;
}

function requireAuth() {
  if (!getToken()) { location.href = "login.html"; }
}
