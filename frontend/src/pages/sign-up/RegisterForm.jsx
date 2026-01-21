import React, { useState } from "react";

export default function RegisterForm({ registerUser }) {
    const [nombre, setNombre] = useState("");
    const [correo, setCorreo] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [numero, setNumero] = useState("");
    const [error, setError] = useState("");

    const handleSubmit = (e) => {
        e.preventDefault();

        if (password !== confirmPassword) {
            setError("Las contraseñas no coinciden");
            return;
        }

        setError("");

        const nuevoUsuario = {
            nombre,
            correo,
            password,
            numero
        };

        registerUser(nuevoUsuario);
        resetForm()
    };

    const resetForm = () => {
        setNombre("");
        setCorreo("");
        setPassword("");
        setConfirmPassword("");
        setNumero("");
    }

    return (
        <div>
            <form onSubmit={handleSubmit}>
                <div className="row">

                    <div className="col-12 form-input">
                        <label>Nombre</label>
                        <input
                            type="text"
                            value={nombre}
                            onChange={(e) => setNombre(e.target.value)}
                            required
                        />
                    </div>

                    <div className="col-12 form-input">
                        <label>Correo</label>
                        <input
                            type="email"
                            value={correo}
                            onChange={(e) => setCorreo(e.target.value)}
                            required
                        />
                    </div>

                    <div className="col-12 form-input">
                        <label>Contraseña</label>
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>

                    <div className="col-12 form-input">
                        <label>Confirmar Contraseña</label>
                        <input
                            type="password"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            required
                        />
                    </div>

                    <div className="col-12 form-input">
                        <label>Teléfono</label>
                        <input
                            type="text"
                            value={numero}
                            onChange={(e) => setNumero(e.target.value)}
                            required
                        />
                    </div>

                    <br/>

                    <button type="submit">Registrar</button>
                </div>
            </form>
        </div>

    );
}
