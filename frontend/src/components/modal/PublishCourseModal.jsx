import "./PublishCourseModal.css";
import { createPortal } from "react-dom";

export function PublishCourseModal({
                                       selectedCourse,
                                       onCancel,
                                       onConfirm
                                   }) {
    return createPortal(
        <div className="publish-modal-overlay">
            <div className="publish-modal">
                <h2>
                    {selectedCourse?.isPublished
                        ? "Unpublish Course"
                        : "Publish Course"}
                </h2>

                <p>
                    {selectedCourse?.isPublished
                        ? "Are you sure you want to unpublish this course?"
                        : "Do you want to publish this course?"}
                </p>

                <div className="publish-modal-actions">
                    <button
                        type="button"
                        className="publish-modal-cancel"
                        onClick={onCancel}
                    >
                        Cancel
                    </button>

                    <button
                        type="type"
                        className="publish-modal-confirm"
                        onClick={onConfirm}
                    >
                        Confirm
                    </button>
                </div>
            </div>
        </div>,
        document.body
    );
}