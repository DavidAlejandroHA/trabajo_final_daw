import { useState } from "react";
import { api } from "../api/http.js";

export default function CrearServicioPage() {
  const [nombre, setNombre] = useState("");
  const [duracionMin, setDuracionMin] = useState(30);
  const [precio, setPrecio] = useState("12.50");
  const [msg, setMsg] = useState("");
  const [error, setError] = useState("");

  async function onSubmit(e) {
    e.preventDefault();
    setMsg("");
    setError("");

    try {
      const payload = {
        nombre: nombre.trim(),
        duracionMin: Number(duracionMin),
        precio: Number(precio),
      };
      const created = await api.crearServicio(payload);
      setMsg(`Servicio creado (id=${created.id}): ${created.nombre}`);
      setNombre("");
      setDuracionMin(30);
      setPrecio("12.50");
    } catch (e2) {
      // Si vienen errores de validación del backend
      if (e2.data?.fields) {
        setError(`${e2.message} — ${JSON.stringify(e2.data.fields)}`);
      } else {
        setError(e2.message);
      }
    }
  }

  return (
    <div>
      <h2>Crear servicio (para cargar datos rápido)</h2>

      <form onSubmit={onSubmit} className="form">
        <label>
          Nombre
          <input value={nombre} onChange={(e) => setNombre(e.target.value)} placeholder="Corte" />
        </label>

        <label>
          Duración (min)
          <input
            type="number"
            min="1"
            value={duracionMin}
            onChange={(e) => setDuracionMin(e.target.value)}
          />
        </label>

        <label>
          Precio
          <input
            type="number"
            step="0.01"
            min="0"
            value={precio}
            onChange={(e) => setPrecio(e.target.value)}
          />
        </label>

        <button type="submit">Crear</button>
      </form>

      {msg && <p className="ok">{msg}</p>}
      {error && <p className="error">Error: {error}</p>}
    </div>
  );
}

