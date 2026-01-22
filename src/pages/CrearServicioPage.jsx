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
      if (e2.data?.fields) {
        setError(`${e2.message} — ${JSON.stringify(e2.data.fields)}`);
      } else {
        setError(e2.message);
      }
    }
  }

  return (
    <div>
      <h2 className="mb-3">Crear servicio</h2>

      <form onSubmit={onSubmit} className="card p-4 shadow-sm">

        <div className="mb-3">
          <label className="form-label">Nombre</label>
          <input
            className="form-control"
            value={nombre}
            onChange={(e) => setNombre(e.target.value)}
            placeholder="Corte"
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Duración (min)</label>
          <input
            type="number"
            min="1"
            className="form-control"
            value={duracionMin}
            onChange={(e) => setDuracionMin(e.target.value)}
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Precio (€)</label>
          <input
            type="number"
            step="0.01"
            min="0"
            className="form-control"
            value={precio}
            onChange={(e) => setPrecio(e.target.value)}
          />
        </div>

        <button type="submit" className="btn btn-success w-100">
          Crear
        </button>
      </form>

      {msg && <p className="text-success mt-3">{msg}</p>}
      {error && <p className="text-danger mt-3">Error: {error}</p>}
    </div>
  );
}
