import "./ErrorModal.css";
import { createPortal } from "react-dom";

export function ErrorModal({
                                       errorMessage,
                                       errorStatus,
                                       onClose
                                   }) {
    return createPortal(
        <div className="error-modal-overlay">
            <div className="error-modal">
                <h2>
                    {errorStatus || "Something went wrong"}
                </h2>

                <p>
                    {errorMessage}
                </p>

                <div className="error-modal-actions">
                    <button
                        type="button"
                        className="error-modal-cancel"
                        onClick={onClose}
                    >
                        Close
                    </button>

                </div>
            </div>
        </div>,
        document.body
    );
}