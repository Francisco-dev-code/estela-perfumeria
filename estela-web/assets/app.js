const API = 'http://localhost:8080/api';

const state = {
  auth: sessionStorage.getItem('estela.auth') || null,
  user: null,
  cart: [],
  products: []
};

const $ = (selector) => document.querySelector(selector);
const money = (value) => new Intl.NumberFormat('es-CL', { style: 'currency', currency: 'CLP' }).format(value);

function setStatus(element, message, type = '') {
  element.textContent = message;
  element.className = 'status ' + type;
}

function authHeader() {
  return state.auth ? { Authorization: 'Basic ' + state.auth } : {};
}

async function api(path, options = {}) {
  const response = await fetch(API + path, {
    ...options,
    headers: { 'Content-Type': 'application/json', ...authHeader(), ...(options.headers || {}) }
  });
  if (response.status === 204) return null;
  const body = await response.json().catch(() => ({}));
  if (!response.ok) throw new Error(body.message || 'Error ' + response.status);
  return body;
}

function showView(name) {
  document.querySelectorAll('.view').forEach((view) => {
    view.hidden = view.id !== 'view-' + name;
  });
  if (name === 'pedidos') loadOrders();
  if (name === 'admin') loadAdminOrders();
}

function renderSession() {
  $('#session-name').textContent = state.user ? state.user.name : 'Sin sesion';
  $('#btn-login').hidden = Boolean(state.user);
  $('#btn-logout').hidden = !state.user;
  $('#nav-admin').hidden = !state.user || state.user.role !== 'ADMIN';
}

async function loadSession() {
  if (!state.auth) return renderSession();
  try {
    state.user = await api('/auth/me');
  } catch {
    state.auth = null;
    sessionStorage.removeItem('estela.auth');
  }
  renderSession();
}

async function loadProducts(query = '') {
  const status = $('#catalogo-status');
  setStatus(status, 'Cargando catalogo...');
  try {
    state.products = await api('/products' + (query ? '?q=' + encodeURIComponent(query) : ''));
    renderProducts();
    setStatus(status, state.products.length + ' fragancias disponibles');
  } catch (error) {
    setStatus(status, error.message, 'error');
  }
}

function renderProducts() {
  const grid = $('#catalogo');
  grid.replaceChildren();
  if (!state.products.length) {
    const empty = document.createElement('p');
    empty.textContent = 'No encontramos fragancias con ese criterio.';
    grid.append(empty);
    return;
  }
  state.products.forEach((product) => {
    const card = document.createElement('article');
    card.className = 'card';
    const brand = document.createElement('span');
    brand.className = 'brandline';
    brand.textContent = product.brand;
    const name = document.createElement('h3');
    name.textContent = product.name;
    const description = document.createElement('p');
    description.textContent = product.description || 'Fragancia de autor.';
    const price = document.createElement('span');
    price.className = 'price';
    price.textContent = money(product.price);
    const stock = document.createElement('span');
    stock.className = 'stock';
    stock.textContent = product.stock > 0 ? 'Stock disponible: ' + product.stock : 'Sin stock';
    const button = document.createElement('button');
    button.className = 'btn';
    button.type = 'button';
    button.textContent = 'Agregar al carrito';
    button.disabled = product.stock === 0;
    button.addEventListener('click', () => addToCart(product));
    card.append(brand, name, description, price, stock, button);
    grid.append(card);
  });
}

function addToCart(product) {
  const existing = state.cart.find((item) => item.product.id === product.id);
  const quantity = existing ? existing.quantity + 1 : 1;
  if (quantity > product.stock) {
    setStatus($('#cart-status'), 'No hay mas stock de ' + product.name, 'error');
    return;
  }
  if (existing) existing.quantity = quantity;
  else state.cart.push({ product, quantity: 1 });
  $('#cart').hidden = false;
  renderCart();
}

function renderCart() {
  const container = $('#cart-items');
  container.replaceChildren();
  let total = 0;
  state.cart.forEach((item, index) => {
    total += item.product.price * item.quantity;
    const row = document.createElement('div');
    row.className = 'cart-item';
    const label = document.createElement('span');
    label.textContent = item.product.name + ' x' + item.quantity;
    const remove = document.createElement('button');
    remove.className = 'btn ghost';
    remove.type = 'button';
    remove.textContent = 'Quitar';
    remove.setAttribute('aria-label', 'Quitar ' + item.product.name);
    remove.addEventListener('click', () => {
      state.cart.splice(index, 1);
      renderCart();
    });
    row.append(label, remove);
    container.append(row);
  });
  $('#cart-total').textContent = money(total);
  $('#btn-comprar').disabled = state.cart.length === 0;
}

async function checkout() {
  const status = $('#cart-status');
  if (!state.user) {
    setStatus(status, 'Debes iniciar sesion para comprar.', 'error');
    $('#dialog-login').showModal();
    return;
  }
  try {
    await api('/orders', {
      method: 'POST',
      body: JSON.stringify({ items: state.cart.map((item) => ({ productId: item.product.id, quantity: item.quantity })) })
    });
    state.cart = [];
    renderCart();
    setStatus(status, 'Pedido confirmado. Revisa Mis pedidos.', 'ok');
    await loadProducts($('#buscar').value);
  } catch (error) {
    setStatus(status, error.message, 'error');
  }
}

async function loadOrders() {
  const status = $('#pedidos-status');
  if (!state.user) {
    setStatus(status, 'Inicia sesion para ver tus pedidos.', 'error');
    $('#pedidos').replaceChildren();
    return;
  }
  try {
    const orders = await api('/orders/mine');
    renderOrders(orders, $('#pedidos'), false);
    setStatus(status, orders.length + ' pedidos registrados');
  } catch (error) {
    setStatus(status, error.message, 'error');
  }
}

function renderOrders(orders, container, admin) {
  container.replaceChildren();
  if (!orders.length) {
    const empty = document.createElement('p');
    empty.textContent = 'Aun no hay pedidos.';
    container.append(empty);
    return;
  }
  orders.forEach((order) => {
    const article = document.createElement('article');
    article.className = 'pedido';
    const header = document.createElement('header');
    const title = document.createElement('strong');
    title.textContent = 'Pedido #' + order.id + (admin ? ' · ' + order.customer : '');
    const pill = document.createElement('span');
    pill.className = 'pill';
    pill.textContent = order.status;
    header.append(title, pill);
    const list = document.createElement('ul');
    order.items.forEach((item) => {
      const li = document.createElement('li');
      li.textContent = item.product + ' x' + item.quantity + ' · ' + money(item.subtotal);
      list.append(li);
    });
    const total = document.createElement('p');
    total.textContent = 'Total: ' + money(order.total);
    article.append(header, list, total);
    if (!admin && order.status === 'PAID') {
      const actions = document.createElement('div');
      actions.className = 'actions';
      actions.append(actionButton('Cancelar', order.id, 'cancel'), actionButton('Devolver', order.id, 'return'));
      article.append(actions);
    }
    container.append(article);
  });
}

function actionButton(label, orderId, action) {
  const button = document.createElement('button');
  button.className = 'btn ghost';
  button.type = 'button';
  button.textContent = label;
  button.addEventListener('click', async () => {
    try {
      await api('/orders/' + orderId + '/' + action, { method: 'POST' });
      await loadOrders();
      setStatus($('#pedidos-status'), 'Pedido actualizado.', 'ok');
    } catch (error) {
      setStatus($('#pedidos-status'), error.message, 'error');
    }
  });
  return button;
}

async function loadAdminOrders() {
  try {
    const orders = await api('/admin/orders');
    renderOrders(orders, $('#admin-pedidos'), true);
  } catch (error) {
    setStatus($('#admin-status'), error.message, 'error');
  }
}

$('#form-buscar').addEventListener('submit', (event) => {
  event.preventDefault();
  loadProducts($('#buscar').value.trim());
});

document.querySelectorAll('.nav-link').forEach((button) => {
  button.addEventListener('click', () => showView(button.dataset.view));
});

$('#btn-login').addEventListener('click', () => $('#dialog-login').showModal());
$('#cerrar-carrito').addEventListener('click', () => { $('#cart').hidden = true; });
$('#btn-comprar').addEventListener('click', checkout);

$('#form-login').addEventListener('submit', async (event) => {
  if (event.submitter && event.submitter.value === 'cancel') return;
  event.preventDefault();
  const email = $('#email').value.trim();
  const password = $('#password').value;
  const credentials = btoa(email + ':' + password);
  try {
    const user = await fetch(API + '/auth/me', { headers: { Authorization: 'Basic ' + credentials } });
    if (!user.ok) throw new Error('Credenciales invalidas');
    state.auth = credentials;
    state.user = await user.json();
    sessionStorage.setItem('estela.auth', credentials);
    renderSession();
    $('#dialog-login').close();
    setStatus($('#login-status'), '');
    if (state.user.role === 'ADMIN') $('#nav-admin').hidden = false;
    loadOrders();
  } catch (error) {
    setStatus($('#login-status'), error.message, 'error');
  }
});

$('#btn-logout').addEventListener('click', () => {
  state.auth = null;
  state.user = null;
  state.cart = [];
  sessionStorage.removeItem('estela.auth');
  renderSession();
  renderCart();
  showView('catalogo');
});

$('#form-producto').addEventListener('submit', async (event) => {
  event.preventDefault();
  const status = $('#admin-status');
  try {
    await api('/admin/products', {
      method: 'POST',
      body: JSON.stringify({
        name: $('#p-nombre').value.trim(),
        brand: $('#p-marca').value.trim(),
        description: $('#p-desc').value.trim(),
        price: Number($('#p-precio').value),
        stock: Number($('#p-stock').value)
      })
    });
    event.target.reset();
    setStatus(status, 'Producto creado.', 'ok');
    await loadProducts();
  } catch (error) {
    setStatus(status, error.message, 'error');
  }
});

loadSession().then(() => loadProducts());
