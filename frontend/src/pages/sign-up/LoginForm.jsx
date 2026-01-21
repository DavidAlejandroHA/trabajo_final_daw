import React, { useState } from "react";

export default function LoginForm({ loginUser }) {
    const [correo, setCorreo] = useState("");
    const [password, setPassword] = useState("");

    const handleSubmit = (e) => {
        e.preventDefault();

        loginUser({correo, password});
    };

    return (
        <div>
            <form onSubmit={handleSubmit}>
                <div className="row">

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

                    <br/>

                    <button type="submit">Login</button>
                    <br/>
                </div>
            </form>
        </div>

    );
}
