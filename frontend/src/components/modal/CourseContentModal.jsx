import "./CourseContentModal.css";
import { useState } from "react";

export function CourseContentModal({
                                       title,
                                       nameLabel,
                                       descriptionLabel,
                                       initialName = "",
                                       initialDescription = "",
                                       onCancel,
                                       onConfirm
                                   }) {
    const [name, setName] = useState(initialName);
    const [description, setDescription] = useState(initialDescription);

    function handleSubmit(event) {
        event.preventDefault();

        onConfirm({
            name,
            description
        });
    }

    return (
        <div className="content-modal-backdrop">
            <form className="content-modal" onSubmit={handleSubmit}>
                <h2>{title}</h2>

                <label>
                    {nameLabel}
                    <input
                        type="text"
                        value={name}
                        onChange={(event) => setName(event.target.value)}
                        required
                    />
                </label>

                <label>
                    {descriptionLabel}
                    <textarea
                        value={description}
                        onChange={(event) => setDescription(event.target.value)}
                        required
                    />
                </label>

                <div className="content-modal-actions">
                    <button type="button" onClick={onCancel}>
                        Cancel
                    </button>

                    <button type="submit">
                        Save
                    </button>
                </div>
            </form>
        </div>
    );
}