import "./CourseContentModal.css";
import { useState } from "react";

const ALLOWED_FILE_TYPES = [".md", ".mp4", ".png"];

function isValidFileType(file) {
    if (!file) {
        return false;
    }

    const fileName = file.name.toLowerCase();

    return ALLOWED_FILE_TYPES.some(fileType =>
        fileName.endsWith(fileType)
    );
}

export function ContentUploadModal({ onCancel, onConfirm }) {
    const [selectedFile, setSelectedFile] = useState(null);
    const [isInvalidFileType, setIsInvalidFileType] = useState(false);
    const [isUploading, setIsUploading] = useState(false);

    const canUpload = selectedFile && !isInvalidFileType && !isUploading;

    function handleFileChange(event) {
        const file = event.target.files[0];

        setSelectedFile(file ?? null);
        setIsInvalidFileType(file ? !isValidFileType(file) : false);
    }

    async function handleSubmit(event) {
        event.preventDefault();

        if (!canUpload) {
            return;
        }

        try {
            setIsUploading(true);
            await onConfirm(selectedFile);
        } finally {
            setIsUploading(false);
        }
    }

    return (
        <div className="content-modal-backdrop">
            <form className="content-modal" onSubmit={handleSubmit}>
                <h2>Upload Content</h2>

                <label
                    className={
                        isInvalidFileType
                            ? "content-file-field invalid"
                            : selectedFile
                                ? "content-file-field valid"
                                : "content-file-field"
                    }
                >
                    File

                    <input
                        type="file"
                        accept=".md,.mp4,.png"
                        onChange={handleFileChange}
                        required
                    />
                </label>

                {selectedFile && (
                    <p className="selected-file-name">
                        Selected file: {selectedFile.name}
                    </p>
                )}

                {isInvalidFileType && (
                    <p className="invalid-file-message">
                        Invalid file type. Please select an md, mp4, or png file.
                    </p>
                )}

                <p className="upload-help-text">
                    Supported files: markdown, png images, and mp4 videos.
                </p>

                <div className="content-modal-actions">
                    <button
                        type="button"
                        onClick={onCancel}
                        disabled={isUploading}
                    >
                        Cancel
                    </button>

                    <button
                        type="submit"
                        disabled={!canUpload}
                    >
                        {isUploading ? "Uploading..." : "Upload"}
                    </button>
                </div>
            </form>
        </div>
    );
}