import React, { useState } from "react";
import ServiciosPage from "./ServiciosPage.jsx";
import CrearCitaPage from "./CrearCitaPage.jsx";
import CitasPage from "./CitasPage.jsx";
import CrearServicioPage from "./CrearServicioPage.jsx";

const TABS = {
  SERVICIOS: "servicios",
  CREAR_CITA: "crear_cita",
  CITAS: "citas",
  CREAR_SERVICIO: "crear_servicio",
};

export default function App() {
  const [tab, setTab] = useState(TABS.SERVICIOS);

  return (
    <div className="container py-4">

      <header className="mb-4 text-center">
        <h1 className="fw-bold">CanaryCode Appointments</h1>
      </header>

      <nav className="nav nav-pills justify-content-center mb-4">
        <button
          className={`nav-link ${tab === TABS.SERVICIOS ? "active" : ""}`}
          onClick={() => setTab(TABS.SERVICIOS)}
        >
          Servicios
        </button>

        <button
          className={`nav-link ${tab === TABS.CREAR_CITA ? "active" : ""}`}
          onClick={() => setTab(TABS.CREAR_CITA)}
        >
          Crear cita
        </button>

        <button
          className={`nav-link ${tab === TABS.CITAS ? "active" : ""}`}
          onClick={() => setTab(TABS.CITAS)}
        >
          Ver citas
        </button>

        <button
          className={`nav-link ${tab === TABS.CREAR_SERVICIO ? "active" : ""}`}
          onClick={() => setTab(TABS.CREAR_SERVICIO)}
        >
          Crear servicio
        </button>
      </nav>

      <main className="card p-4 shadow-sm">
        {tab === TABS.SERVICIOS && <ServiciosPage />}
        {tab === TABS.CREAR_CITA && <CrearCitaPage />}
        {tab === TABS.CITAS && <CitasPage />}
        {tab === TABS.CREAR_SERVICIO && <CrearServicioPage />}
      </main>

      <footer className="text-center text-muted mt-4">
        Consejo: primero crea 2–3 servicios en “Crear servicio”, luego crea una cita.
      </footer>
    </div>
  );
}
