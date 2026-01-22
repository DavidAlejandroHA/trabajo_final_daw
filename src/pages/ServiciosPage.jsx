import { useEffect, useState } from "react";
import { api } from "../api/http.js";

export default function ServiciosPage() {
  const [servicios, setServicios] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  async function cargar() {
    try {
      setError("");
      setLoading(true);
      const data = await api.listarServicios();
      setServicios(data || []);
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
        <h2>Servicios</h2>
        <button className="btn btn-secondary" onClick={cargar}>
          Recargar
        </button>
      </div>

      {loading && <p className="text-muted">Cargando...</p>}
      {error && <p className="text-danger">Error: {error}</p>}

      {!loading && !error && servicios.length === 0 && (
        <p className="text-muted">
          No hay servicios. Crea uno en la pestaña “Crear servicio”.
        </p>
      )}

      {servicios.length > 0 && (
        <table className="table table-striped">
          <thead>
            <tr>
              <th>ID</th>
              <th>Nombre</th>
              <th>Duración (min)</th>
              <th>Precio</th>
            </tr>
          </thead>
          <tbody>
            {servicios.map((s) => (
              <tr key={s.id}>
                <td>{s.id}</td>
                <td>{s.nombre}</td>
                <td>{s.duracionMin}</td>
                <td>{Number(s.precio).toFixed(2)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
