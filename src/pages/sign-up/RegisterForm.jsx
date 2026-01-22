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
        resetForm();
    };

    const resetForm = () => {
        setNombre("");
        setCorreo("");
        setPassword("");
        setConfirmPassword("");
        setNumero("");
    };

    return (
        <div className="card p-4 shadow-sm">
            <h3 className="mb-3">Registro</h3>

            {error && <p className="text-danger">{error}</p>}

            <form onSubmit={handleSubmit}>

                <div className="mb-3">
                    <label className="form-label">Nombre</label>
                    <input
                        type="text"
                        className="form-control"
                        value={nombre}
                        onChange={(e) => setNombre(e.target.value)}
                        required
                    />
                </div>

                <div className="mb-3">
                    <label className="form-label">Correo</label>
                    <input
                        type="email"
                        className="form-control"
                        value={correo}
                        onChange={(e) => setCorreo(e.target.value)}
                        required
                    />
                </div>

                <div className="mb-3">
                    <label className="form-label">Contraseña</label>
                    <input
                        type="password"
                        className="form-control"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>

                <div className="mb-3">
                    <label className="form-label">Confirmar Contraseña</label>
                    <input
                        type="password"
                        className="form-control"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        required
                    />
                </div>

                <div className="mb-3">
                    <label className="form-label">Teléfono</label>
                    <input
                        type="text"
                        className="form-control"
                        value={numero}
                        onChange={(e) => setNumero(e.target.value)}
                        required
                    />
                </div>

                <button type="submit" className="btn btn-success w-100">
                    Registrar
                </button>
            </form>
        </div>
    );
}
