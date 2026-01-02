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
      <div className="row">
        <h2>Citas</h2>
        <button onClick={cargar}>Recargar</button>
      </div>

      {loading && <p className="muted">Cargando...</p>}
      {error && <p className="error">Error: {error}</p>}

      {!loading && !error && citas.length === 0 && (
        <p className="muted">No hay citas aún.</p>
      )}

      {citas.length > 0 && (
        <table className="table">
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
