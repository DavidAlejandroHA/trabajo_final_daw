import { useEffect, useState } from "react";
import { api } from "../api/http.js";

export default function CitasPage() {
  const [citas, setCitas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  async function cargar() {
    try {
      setError("");
      setLoading(true);
      const data = await api.listarCitas();
      setCitas(data || []);
    } catch (e) {
      setError(e.message);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    cargar();
  }, []);

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h2>Citas</h2>
        <button className="btn btn-secondary" onClick={cargar}>
          Recargar
        </button>
      </div>

      {loading && <p className="text-muted">Cargando...</p>}
      {error && <p className="text-danger">Error: {error}</p>}

      {!loading && !error && citas.length === 0 && (
        <p className="text-muted">No hay citas aún.</p>
      )}

      {citas.length > 0 && (
        <table className="table table-striped">
          <thead>
            <tr>
              <th>ID</th>
              <th>Fecha/Hora</th>
              <th>Servicio</th>
              <th>Duración</th>
              <th>Precio</th>
            </tr>
          </thead>
          <tbody>
            {citas.map((c) => (
              <tr key={c.id}>
                <td>{c.id}</td>
                <td>{c.fechaHora}</td>
                <td>{c.servicioNombre}</td>
                <td>{c.servicioDuracionMin} min</td>
                <td>{Number(c.servicioPrecio).toFixed(2)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
