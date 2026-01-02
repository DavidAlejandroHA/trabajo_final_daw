async function request(path, options = {}) {
  const res = await fetch(path, {
    headers: {
      "Content-Type": "application/json",
      ...(options.headers || {}),
    },
    ...options,
  });

  // Intenta leer JSON aunque sea error
  const text = await res.text();
  const data = text ? JSON.parse(text) : null;

  if (!res.ok) {
    // Mensaje bonito si viene del backend
    const msg =
      data?.message ||
      data?.error ||
      `Error HTTP ${res.status}`;
    const err = new Error(msg);
    err.status = res.status;
    err.data = data;
    throw err;
  }

  return data;
}

export const api = {
  listarServicios: () => request("/api/servicios"),
  crearServicio: (payload) =>
    request("/api/servicios", { method: "POST", body: JSON.stringify(payload) }),

  listarCitas: () => request("/api/citas"),
  crearCita: (payload) =>
    request("/api/citas", { method: "POST", body: JSON.stringify(payload) }),
};
