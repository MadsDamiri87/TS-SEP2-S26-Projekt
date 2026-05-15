import "./CourseContentModal.css";

export function ConfirmDeleteModal({ title, message, onCancel, onConfirm }) {
    return (
        <div className="content-modal-backdrop">
            <div className="content-modal">
                <h2>{title}</h2>

                <p>{message}</p>

                <div className="content-modal-actions">
                    <button type="button" onClick={onCancel}>
                        Cancel
                    </button>

                    <button type="button" onClick={onConfirm}>
                        Delete
                    </button>
                </div>
            </div>
        </div>
    );
}