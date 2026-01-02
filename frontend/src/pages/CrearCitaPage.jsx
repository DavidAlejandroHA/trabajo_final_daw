import { useEffect, useMemo, useState } from "react";
import { api } from "../api/http.js";

function toLocalDateTimeString(dt) {
  // Convierte Date -> "YYYY-MM-DDTHH:MM"
  const pad = (n) => String(n).padStart(2, "0");
  const y = dt.getFullYear();
  const m = pad(dt.getMonth() + 1);
  const d = pad(dt.getDate());
  const hh = pad(dt.getHours());
  const mm = pad(dt.getMinutes());
  return `${y}-${m}-${d}T${hh}:${mm}`;
}

export default function CrearCitaPage() {
  const [servicios, setServicios] = useState([]);
  const [servicioId, setServicioId] = useState("");
  const [fechaHora, setFechaHora] = useState(toLocalDateTimeString(new Date(Date.now() + 60 * 60 * 1000))); // +1h
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

      // Backend espera LocalDateTime tipo "2026-01-05T10:30:00"
      const payload = {
        servicioId: Number(servicioId),
        fechaHora: `${fechaHora}:00`,
      };

      const created = await api.crearCita(payload);
      setMsg(
        `Cita creada (id=${created.id}) para "${created.servicioNombre}" en ${created.fechaHora}`
      );
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
      <h2>Crear cita</h2>

      {servicios.length === 0 ? (
        <p className="muted">
          No hay servicios disponibles. Crea uno en “Crear servicio” y vuelve.
        </p>
      ) : (
        <form onSubmit={onSubmit} className="form">
          <label>
            Servicio
            <select value={servicioId} onChange={(e) => setServicioId(e.target.value)}>
              {servicios.map((s) => (
                <option key={s.id} value={s.id}>
                  {s.nombre} ({s.duracionMin} min) — {Number(s.precio).toFixed(2)}
                </option>
              ))}
            </select>
          </label>

          <label>
            Fecha y hora
            <input
              type="datetime-local"
              value={fechaHora}
              onChange={(e) => setFechaHora(e.target.value)}
            />
          </label>

          {servicioSeleccionado && (
            <p className="muted">
              Seleccionado: <strong>{servicioSeleccionado.nombre}</strong>
            </p>
          )}

          <button type="submit">Reservar</button>
        </form>
      )}

      {msg && <p className="ok">{msg}</p>}
      {error && <p className="error">Error: {error}</p>}
    </div>
  );
}