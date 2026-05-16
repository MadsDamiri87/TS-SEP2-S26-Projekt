import {useState, useEffect} from "react";
import {useNavigate} from "react-router-dom";
import {register, login} from "../../../api/authApi.js";
import {enrollInCourse} from "../../../api/enrollmentApi.js";
import "./BuyPopup.css";

export function BuyPopup({course, onClose}) {
    const navigate = useNavigate();

    // Steps: 1: Detaljer, 2: Betaling & Bruger, 3: Bekræftelse, 4: Succes
    const [currentStep, setCurrentStep] = useState(1);
    const [loading, setLoading] = useState(false);

    const [isLogin, setIsLogin] = useState(true);
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [errorMessage, setErrorMessage] = useState(true);

    const [user, setUser] = useState(() => JSON.parse(localStorage.getItem("userDetails")));

    useEffect(() => {
        window.history.pushState(null, null, window.location.pathname);
        const handleBackButton = () => onClose();
        window.addEventListener('popstate', handleBackButton);
        return () => window.removeEventListener('popstate', handleBackButton);
    }, [onClose]);

    const handleAuthStep = async (e) => {
        e.preventDefault();
        setErrorMessage("");

        if (!formData.cardNumber || !formData.name) {
            setErrorMessage("Please fill in your payment details before continuing.");
            return;
        }

        if (user) {
            handleNext();
        } else {
            if (username.length < 5 || password.length < 5) {
                setErrorMessage("Username and password must be at least 5 characters.");
                return;
            }
            setLoading(true);
            try {
                if (isLogin) {
                    await login(username, password);
                } else {
                    await register(username, password, email);
                }

                const newUserDetails = JSON.parse(localStorage.getItem("userDetails"));
                setUser(newUserDetails);

                window.dispatchEvent(new Event("authChange"));
                setErrorMessage("");
            } catch (error) {
                setErrorMessage(error.message || "Authentication failed");
            } finally {
                setLoading(false);
            }
        }
    };


const [formData, setFormData] = useState({
    name: "",
    cardNumber: "",
    expiry: "",
    cvc: ""
});
const handleNext = () => setCurrentStep((prev) => prev + 1);

const handleBack = () => setCurrentStep((prev) => prev - 1);

const handleConfirmAndPay = async () => {
    setLoading(true);
    try {
        await enrollInCourse(course.courseId);
        handleNext();
    } catch (error) {
        if (error.message.includes("409") || error.message.includes("Conflict")) {
            alert("You are already enrolled in this course!");
            onClose();
        } else {
            alert("Error with payment completion: " + error.message);
        }
    } finally {
        setLoading(false);
    }
};

return (
    <div className="buy-modal-overlay" onClick={onClose}>
        <div className="buy-modal" onClick={(e) => e.stopPropagation()}>
            <button type="button" className="buy-modal-close"
                    onClick={onClose}>×
            </button>

            <nav className="step-indicator">
                {[1, 2, 3, 4].map((num) => (
                    <div key={num}
                         className={`step-item ${currentStep >= num ? "active" : ""}`}>
                        <div className="step-circle">{num}</div>
                        <span className="step-text">
                                {num === 1 ? "Course" : num === 2 ? "Payment" : num === 3 ? "Confirm" : "Done!"}
                            </span>
                    </div>
                ))}
            </nav>

            {currentStep === 1 && (
                <section className="step-content">
                    <h2>Course details</h2>
                    <div className="summary-box">
                        <h3>{course.title}</h3>
                        <p>{course.shortDescription}</p>
                        <p className="price-display">{course.price} DKK</p>
                    </div>
                    <button className="highlight-btn" onClick={handleNext}>Next:
                        Payment & Account
                    </button>
                </section>
            )}

            {currentStep === 2 && (
                <section className="step-content">
                    <div className="split-layout">
                        <div className="form-column left">
                            <h3>Payment Info</h3>
                            <input type="text" placeholder="Cardholder Name"
                                   required onChange={e => setFormData({
                                ...formData,
                                name: e.target.value
                            })}/>
                            <input type="text" placeholder="1234 1234 1234 1234"
                                   required onChange={e => setFormData({
                                ...formData,
                                cardNumber: e.target.value
                            })}/>
                            <input type="text" placeholder="MM/YY" required/>
                            <input type="text" placeholder="CVC" required/>
                        </div>

                        <div className="divider"></div>

                        <div className="form-column">
                            {user? (
                                <div className="logged-in-box">
                                    <h3>Account</h3>
                                    <p>You are purchasing as:</p>
                                    <div className="user-badge">
                                        <strong>{user.username}</strong>
                                        <span>({user.email})</span>
                                    </div>
                                    <p className="small-hint">Not you? <br/>Log
                                        out on the main page first.</p>
                                    <button className="secondary-btn"
                                            onClick={onClose}>
                                        Go Back
                                    </button>
                                </div>
                            ) : (
                                <div className="auth-sub-flow">
                                    <h3>{isLogin ? "Login" : "Create Account"}</h3>
                                    <form className="buy-auth-form"
                                          onSubmit={handleAuthStep}>
                                        <input
                                            type="text"
                                            placeholder="Username"
                                            value={username}
                                            onChange={(e) => setUsername(e.target.value)}
                                            minLength={5}
                                            maxLength={30}
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
                                            required
                                        />
                                        {errorMessage &&
                                            <p className="error-text">{errorMessage}</p>}

                                        <button type="submit"
                                                className="highlight-btn">
                                            {isLogin ? "Login" : "Create"}
                                        </button>
                                    </form>

                                    <p className="toggle-text">
                                        {isLogin ? "New here?" : "Already have an account?"}{" "}
                                        <span
                                            onClick={() => setIsLogin(!isLogin)}>
                                {isLogin ? "Create Account" : "Login"}
                            </span>
                                    </p>
                                </div>
                            )}
                        </div>
                    </div>
                    <div className="button-group">
                        <button onClick={handleBack}
                                className="secondary-btn">Back
                        </button>
                        <button onClick={handleAuthStep}
                                className="highlight-btn" disabled={loading}>
                            {loading ? "Creating account..." : "Continue to confirm"}
                        </button>
                    </div>
                </section>
            )}

            {currentStep === 3 && (
                <section className="step-content">
                    <h2>Confirm your order</h2>
                    <div className="confirmation-box">
                        <p><strong>Course:</strong> {course.title}</p>
                        <p><strong>Account:</strong> {user?.username || "Loading..."}</p>
                        <p><strong>Price:</strong> {course.price} DKK</p>
                        <p className="small-hint">By clicking confirm, you agree
                            to our terms.</p>
                    </div>
                    <div className="button-group">
                        <button className="secondary-btn"
                                onClick={handleBack}>Edit details
                        </button>
                        <button className="highlight-btn"
                                onClick={handleConfirmAndPay} disabled={loading}>
                            {loading ? "Processing..." : "Confirm & Pay"}
                        </button>
                    </div>
                </section>
            )}

            {currentStep === 4 && (
                <section className="step-content success-view">
                    <div className="success-icon">🎉</div>
                    <h2>Congratulations!</h2>
                    <p>You have successfully been enrolled
                        in <strong>{course.title}</strong>.</p>
                    <div className="final-actions">
                        <button onClick={() => navigate("/")}
                                className="highlight-btn">Go to Home
                        </button>
                        <button onClick={onClose} className="secondary-btn">Stay
                            on Page
                        </button>
                    </div>
                </section>
            )}
        </div>
    </div>
);
}