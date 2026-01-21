import React, { useState } from "react";
import RegisterForm from "./pages/sign-up/RegisterForm.jsx";
import LoginForm from "./pages/sign-up/LoginForm.jsx";
import App from "./pages/index.jsx";
import { api } from "./api/http.js";

export default function Contenedor() {
    const [usuarios, setUsuarios] = useState([]);
    const [modo, setModo] = useState("login");
    const [usuarioLogueado, setUsuarioLogueado] = useState(null);

    function registerUser(nuevoUsuario) {
        setUsuarios(prev => [...prev, nuevoUsuario]);
        // Tras completas registro, nos redirige a Login
        setModo("login");
    }

    async function loginUser({ correo, password }) {
        const usuario = await api.login({email: correo, password: password});

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
        <div className="card">

            {/* Componente Inicio Sesion Correcto (Previo Registro) */}
            {usuarioLogueado ? (
               <App/>
            ) : (
                <>
                    {/* Formulario de Login y Registro */}
                    {modo === "login" && <LoginForm loginUser={loginUser}/>}
                    {modo === "register" && <RegisterForm registerUser={registerUser} />}

                    {/* Cambio entre formulario de Registro y Login*/}
                    {modo === "login" && <button type="button"
                                                 className="link-button" onClick={switchMode}>¿No tienes cuenta? Registrate aquí</button>}
                    {modo === "register" && <button type="button"
                                                 className="link-button" onClick={switchMode}>¿Ya tienes cuenta? Logueate aquí</button>}
                </>
            )}
        </div>
    );
}
