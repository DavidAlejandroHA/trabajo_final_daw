import { useEffect, useMemo, useState } from "react";
import { api } from "../api/http.js";

function toLocalDateTimeString(dt) {
  const pad = (n) => String(n).padStart(2, "0");
  return `${dt.getFullYear()}-${pad(dt.getMonth() + 1)}-${pad(dt.getDate())}T${pad(dt.getHours())}:${pad(dt.getMinutes())}`;
}

export default function CrearCitaPage() {
  const [servicios, setServicios] = useState([]);
  const [servicioId, setServicioId] = useState("");
  const [fechaHora, setFechaHora] = useState(toLocalDateTimeString(new Date(Date.now() + 3600000)));
  const [msg, setMsg] = useState("");
  const [error, setError] = useState("");

  const servicioSeleccionado = useMemo(
    () => servicios.find((s) => String(s.id) === String(servicioId)),
    [servicios, servicioId]
  );

  useEffect(() => {
    (async () => {
      try {
        const data = await api.listarServicios();
        setServicios(data || []);
        if (data?.length) setServicioId(String(data[0].id));
      } catch (e) {
        setError(e.message);
      }
    })();
  }, []);

  async function onSubmit(e) {
    e.preventDefault();
    setMsg("");
    setError("");

    try {
      if (!servicioId) throw new Error("Selecciona un servicio");
      if (!fechaHora) throw new Error("Selecciona fecha y hora");

      const payload = {
        servicioId: Number(servicioId),
        fechaHora: `${fechaHora}:00`,
      };

      const created = await api.crearCita(payload);
      setMsg(`Cita creada (id=${created.id}) para "${created.servicioNombre}" en ${created.fechaHora}`);
    } catch (e2) {
      setError(e2.message);
    }
  }

  return (
    <div>
      <h2 className="mb-3">Crear cita</h2>

      {servicios.length === 0 ? (
        <p className="text-muted">No hay servicios disponibles.</p>
      ) : (
        <form onSubmit={onSubmit} className="card p-4 shadow-sm">

          <div className="mb-3">
            <label className="form-label">Servicio</label>
            <select
              className="form-select"
              value={servicioId}
              onChange={(e) => setServicioId(e.target.value)}
            >
              {servicios.map((s) => (
                <option key={s.id} value={s.id}>
                  {s.nombre} ({s.duracionMin} min) — {Number(s.precio).toFixed(2)}
                </option>
              ))}
            </select>
          </div>

          <div className="mb-3">
            <label className="form-label">Fecha y hora</label>
            <input
              type="datetime-local"
              className="form-control"
              value={fechaHora}
              onChange={(e) => setFechaHora(e.target.value)}
            />
          </div>

          {servicioSeleccionado && (
            <p className="text-muted">
              Seleccionado: <strong>{servicioSeleccionado.nombre}</strong>
            </p>
          )}

          <button className="btn btn-primary w-100">Reservar</button>
        </form>
      )}

      {msg && <p className="text-success mt-3">{msg}</p>}
      {error && <p className="text-danger mt-3">Error: {error}</p>}
    </div>
  );
}

