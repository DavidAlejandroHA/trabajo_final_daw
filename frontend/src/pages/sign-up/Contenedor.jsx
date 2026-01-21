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
        <div className="card">

            {/* Componente Inicio Sesion Correcto (Previo Registro) */}
            {usuarioLogueado ? (
                <LoggedIn usuario={usuarioLogueado} onLogout={logout} />
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
