import "./LoginModal.css";
import {useState} from "react";
import {login, register} from "../../../api/authApi.js";
import {ErrorModal} from "../../modal/error/ErrorModal.jsx";


export function LoginModal({isOpen, onClose, isLogin, setIsLogin, onSubmit}) {
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [errorMessage, setErrorMessage] = useState("");

    const closeMenu = () => {
        setIsLogin(true);
        onClose();
    };

    const clearInputFields = () => {
        setUsername("")
        setEmail("")
        setPassword("")
    }

    const handleSubmit = (e) => {
        e.preventDefault();
        setErrorMessage("");

        if (isLogin) {
            login(username, password)
                .then((res) => {
                    console.log("login success", res);
                    onSubmit()
                    clearInputFields()
                    closeMenu();
                })
                .catch((err) => {
                    setErrorMessage(err.message);
                });
        } else {
            register(username, password, email)
                .then((res) => {
                    console.log("register success", res);
                    onSubmit()
                    clearInputFields()
                    closeMenu();
                })
                .catch((err) => {
                    setErrorMessage(err.message);
                });
        }
    };

    if (!isOpen) return null;

    return (
        <div className="auth-modal-overlay" onClick={closeMenu}>
            <div className="auth-modal" onClick={(e) => e.stopPropagation()}>
                <button className="close-btn" onClick={closeMenu}>
                    ×
                </button>

                <h2>{isLogin ? "Login" : "Create Account"}</h2>

                <form className="auth-form" onSubmit={handleSubmit}>
                    <input
                        type="text"
                        placeholder="Username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        pattern="^.{5,30}$"
                        title="Username must be between 5 and 30 characters"
                        required
                    />

                    {!isLogin && (
                        <input
                            type="email"
                            placeholder="Email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    )}

                    <input
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        pattern="^.{5,50}$"
                        title="Username must be between 5 and 50 characters"
                        required
                    />

                    <p className="error-message-label">
                        {errorMessage}
                    </p>

                    <button type="submit" className="highlight-btn">
                        {isLogin ? "Login" : "Create Account"}
                    </button>
                </form>

                <p className="popup-text-bottom">
                    {isLogin ? "Don't have an account?" : "Already have an account?"}{" "}
                    <span
                        className="switch-mode"
                        onClick={() => setIsLogin(!isLogin)}
                    >
                        {isLogin ? "Create one" : "Login"}
                    </span>
                </p>
            </div>

        </div>


    );
}