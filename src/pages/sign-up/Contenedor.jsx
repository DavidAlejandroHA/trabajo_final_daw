import React, { useState } from "react";
import RegisterForm from "./RegisterForm";
import LoginForm from "./LoginForm";
import LoggedIn from "../components/LoggedIn";

export default function Contenedor() {
    const [usuarios, setUsuarios] = useState([]);
    const [modo, setModo] = useState("login");
    const [usuarioLogueado, setUsuarioLogueado] = useState(null);

    function registerUser(nuevoUsuario) {
        setUsuarios(prev => [...prev, nuevoUsuario]);
        setModo("login");
    }

    function loginUser({ correo, password }) {
        const usuario = usuarios.find(
            u => u.correo === correo && u.password === password
        );

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
        <div className="container mt-4">
            <div className="card p-4 shadow-sm">

                {usuarioLogueado ? (
                    <LoggedIn usuario={usuarioLogueado} onLogout={logout} />
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
