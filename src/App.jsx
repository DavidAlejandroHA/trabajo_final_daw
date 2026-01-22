import React, { useState } from "react";
import RegisterForm from "./pages/sign-up/RegisterForm.jsx";
import LoginForm from "./pages/sign-up/LoginForm.jsx";
import App from "./pages/index.jsx";

export default function Contenedor() {
    const [usuarios, setUsuarios] = useState([]);
    const [modo, setModo] = useState("login");
    const [usuarioLogueado, setUsuarioLogueado] = useState(null);

    function registerUser(nuevoUsuario) {
        setUsuarios(prev => [...prev, nuevoUsuario]);
        // Tras completas registro, nos redirige a Login
        setModo("login");
    }

    function loginUser({ correo, password }) {
        const usuario = usuarios.find(
            u => u.correo === correo && u.password === password
        );

        // Alerta informando de correo o contraseña incorrectas
        if (!usuario) {
            alert("Credenciales incorrectas");
            return;
        }
        setUsuarioLogueado(usuario);
    }

    function logout() {
        setUsuarioLogueado(null);
        setModo("login");
    }

    function switchMode() {
        setUsuarioLogueado(null);
        setModo(modo === "login" ? "register" : "login");
    }

  return (
  <div className="d-flex justify-content-center align-items-center" style={{ minHeight: "100vh", background: "#f5f5f5" }}>
    <div className="card shadow p-4" style={{ width: "420px", borderRadius: "12px" }}>

      {usuarioLogueado ? (
    <>
      <App/>
      <button onClick={logout} className="btn btn-warning mt-3">Cerrar sesión</button>
    </>
) : (

        <>
          {modo === "login" && <LoginForm loginUser={loginUser} />}
          {modo === "register" && <RegisterForm registerUser={registerUser} />}

          <button
            type="button"
            className="btn btn-link mt-3"
            onClick={switchMode}
          >
            {modo === "login"
              ? "¿No tienes cuenta? Regístrate aquí"
              : "¿Ya tienes cuenta? Inicia sesión aquí"}
          </button>
        </>
      )}

    </div>
  </div>
);

}
