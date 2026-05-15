import "./CourseContentModal.css";
import { useState } from "react";

export function ContentUploadModal({ onCancel, onConfirm }) {
    const [selectedFile, setSelectedFile] = useState(null);

    function handleSubmit(event) {
        event.preventDefault();

        if (!selectedFile) {
            return;
        }

        onConfirm(selectedFile);
    }

    return (
        <div className="content-modal-backdrop">
            <form className="content-modal" onSubmit={handleSubmit}>
                <h2>Upload Content</h2>

                <label>
                    File
                    <input
                        type="file"
                        accept=".md,image/*,video/*"
                        onChange={(event) => setSelectedFile(event.target.files[0])}
                        required
                    />
                </label>

                <p className="upload-help-text">
                    Supported files: markdown, images, and videos.
                </p>

                <div className="content-modal-actions">
                    <button type="button" onClick={onCancel}>
                        Cancel
                    </button>

                    <button type="submit">
                        Upload
                    </button>
                </div>
            </form>
        </div>
    );
}