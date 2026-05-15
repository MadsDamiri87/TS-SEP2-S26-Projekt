import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./BuyPopup.css";

export function BuyPopup({ course, onClose }) {
    const navigate = useNavigate();

    // Steps: 1: Detaljer, 2: Betaling & Bruger, 3: Bekræftelse, 4: Succes
    const [currentStep, setCurrentStep] = useState(1);
    const [formData, setFormData] = useState({
        name: "",
        email: "",
        password: "",
        cardNumber: "",
        expiry: "",
        cvc: ""
    });

    const handleNext = () => setCurrentStep((prev) => prev + 1);
    const handleBack = () => setCurrentStep((prev) => prev - 1);

    const handleConfirmAndPay = async (event) => {
        event.preventDefault();
        try {
            handleNext();
        } catch (error) {
            alert("Something went wrong during payment! Please try again.");
        }
    };

    return (
        <div className="buy-modal-overlay" onClick={onClose}>
            <div className="buy-modal" onClick={(e) => e.stopPropagation()}>
                <button type="button" className="buy-modal-close" onClick={onClose}>×</button>

                <nav className="step-indicator">
                    {[1, 2, 3, 4].map((num) => (
                        <div key={num} className={`step-item ${currentStep >= num ? "active" : ""}`}>
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
                        <button className="highlight-btn" onClick={handleNext}>Next: Payment & Account</button>
                    </section>
                )}

                {currentStep === 2 && (
                    <section className="step-content">
                        <div className="split-layout">
                            <div className="form-column left">
                                <h3>Payment Info</h3>
                                <input type="text" placeholder="Cardholder Name" required onChange={e => setFormData({...formData, name: e.target.value})} />
                                <input type="text" placeholder="1234 1234 1234 1234" required onChange={e => setFormData({...formData, cardNumber: e.target.value})} />
                                <div className="input-row">
                                    <input type="text" placeholder="MM/YY" required />
                                    <input type="text" placeholder="CVC" required />
                                </div>
                            </div>

                            <div className="divider"></div>

                            <div className="form-column right">
                                <h3>Account</h3>
                                <p className="small-hint">Sign in or create account to continue</p>
                                <input type="email" placeholder="Email" required onChange={e => setFormData({...formData, email: e.target.value})} />
                                <input type="password" placeholder="Password" required />
                                <p className="status-note">You will be logged in automatically after purchase.</p>
                            </div>
                        </div>
                        <div className="button-group">
                            <button className="secondary-btn" onClick={handleBack}>Back</button>
                            <button className="highlight-btn" onClick={handleNext}>Review Order</button>
                        </div>
                    </section>
                )}

                {currentStep === 3 && (
                    <section className="step-content">
                        <h2>Confirm your order</h2>
                        <div className="confirmation-box">
                            <p><strong>Course:</strong> {course.title}</p>
                            <p><strong>Account:</strong> {formData.email}</p>
                            <p><strong>Price:</strong> {course.price} DKK</p>
                            <p className="small-hint">By clicking confirm, you agree to our terms.</p>
                        </div>
                        <div className="button-group">
                            <button className="secondary-btn" onClick={handleBack}>Edit details</button>
                            <button className="highlight-btn" onClick={handleConfirmAndPay}>Confirm & Pay</button>
                        </div>
                    </section>
                )}

                {currentStep === 4 && (
                    <section className="step-content success-view">
                        <div className="success-icon">🎉</div>
                        <h2>Congratulations!</h2>
                        <p>Purchase complete. You now have access to the course.</p>
                        <div className="final-actions">
                            <button className="highlight-btn" onClick={() => navigate("/")}>Go to Homepage</button>
                            <button className="secondary-btn" onClick={onClose}>Back to Course</button>
                        </div>
                    </section>
                )}
            </div>
        </div>
    );
}