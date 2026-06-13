// Renders the sidebar + topbar shell on every authenticated page.
function renderShell(activeKey, title) {
  const user = getUser() || {};
  const adminOnly = isAdmin();
  const navItems = [
    { key: "dashboard", href: "dashboard.html", label: "Dashboard" },
    { key: "customers", href: "customers.html", label: "Customers" },
    { key: "products",  href: "products.html",  label: "Products" },
    { key: "orders",    href: "orders.html",    label: "Orders" },
    { key: "reports",   href: "reports.html",   label: "Reports" },
  ];
  const links = navItems.map(i =>
    `<a href="${i.href}" class="${i.key === activeKey ? "active" : ""}">${i.label}</a>`
  ).join("");

  document.body.innerHTML = `
    <div class="app">
      <aside class="sidebar">
        <div class="brand">Clothing CRM</div>
        <nav class="nav">
          ${links}
          <div class="logout" id="logoutBtn">Logout</div>
        </nav>
      </aside>
      <main class="main">
        <div class="topbar">
          <h1>${title}</h1>
          <div class="user-chip">${user.fullName || user.username || ""} ${adminOnly ? "(Admin)" : "(Employee)"}</div>
        </div>
        <div id="page"></div>
      </main>
    </div>
  `;
  document.getElementById("logoutBtn").onclick = () => { clearAuth(); location.href = "login.html"; };
}

function el(html) { const d = document.createElement("div"); d.innerHTML = html.trim(); return d.firstChild; }
function fmtMoney(n) { return "$" + Number(n || 0).toFixed(2); }
function fmtDate(s) { if (!s) return ""; const d = new Date(s); return d.toLocaleString(); }
function qsParam(name) { return new URLSearchParams(location.search).get(name); }
