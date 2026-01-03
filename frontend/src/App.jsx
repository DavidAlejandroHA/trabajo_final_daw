import React, { useState } from "react";
import ServiciosPage from "./pages/ServiciosPage.jsx";
import CrearCitaPage from "./pages/CrearCitaPage.jsx";
import CitasPage from "./pages/CitasPage.jsx";
import CrearServicioPage from "./pages/CrearServicioPage.jsx";
import 'bootstrap/dist/css/bootstrap.min.css';

const TABS = {
  SERVICIOS: "servicios",
  CREAR_CITA: "crear_cita",
  CITAS: "citas",
  CREAR_SERVICIO: "crear_servicio",
};

// Importante llamar a la función App() para que se muestre nuestra aplicación
App();

export default function App() {
  const [tab, setTab] = useState(TABS.SERVICIOS);

  return (
    <div className="container">
      <header className="header">
        <h1>CanaryCode Appointments — Sprint 1</h1>
        <p className="muted">
          React (Vite) + Apache (proxy /api) + Spring Boot + MySQL (Docker)
        </p>
      </header>

      <nav className="nav">
        <button className={tab === TABS.SERVICIOS ? "active" : ""} onClick={() => setTab(TABS.SERVICIOS)}>
          Servicios
        </button>
        <button className={tab === TABS.CREAR_CITA ? "active" : ""} onClick={() => setTab(TABS.CREAR_CITA)}>
          Crear cita
        </button>
        <button className={tab === TABS.CITAS ? "active" : ""} onClick={() => setTab(TABS.CITAS)}>
          Ver citas
        </button>
        <button className={tab === TABS.CREAR_SERVICIO ? "active" : ""} onClick={() => setTab(TABS.CREAR_SERVICIO)}>
          Crear servicio
        </button>
      </nav>

      <main className="card">
        {tab === TABS.SERVICIOS && <ServiciosPage />}
        {tab === TABS.CREAR_CITA && <CrearCitaPage />}
        {tab === TABS.CITAS && <CitasPage />}
        {tab === TABS.CREAR_SERVICIO && <CrearServicioPage />}
      </main>

      <footer className="footer muted">
        Consejo: primero crea 2–3 servicios en “Crear servicio”, luego crea una cita.
      </footer>
    </div>
  );
}


