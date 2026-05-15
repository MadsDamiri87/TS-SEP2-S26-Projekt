import "./OwnedCourseItem.css";

export function OwnedCourseItem({
                                    courseId,
                                    courseName,
                                    isPublished,
                                    lastEdited,
                                    onEdit,
                                    onToggleVisibility,
                                    onDelete
                                }) {
    function formatLastEdited(dateValue) {
        if (!dateValue) {
            return "Last edited: unknown";
        }

        const date = new Date(dateValue);

        return `Last edited: ${date.toLocaleDateString("en-GB", {
            day: "2-digit",
            month: "short",
            year: "numeric"
        })} at ${date.toLocaleTimeString("en-GB", {
            hour: "2-digit",
            minute: "2-digit"
        })}`;
    }

    return (
        <div className="owned-course-item">
            <div className="owned-course-info">
                <h3>{courseName}</h3>

                <span
                    className={`course-status ${
                        isPublished
                            ? "course-status-published"
                            : "course-status-draft"
                    }`}
                >
                    {isPublished ? "Published" : "Draft"}
                </span>
            </div>

            <div className="owned-course-right">
                <p className="owned-course-last-edited">
                    {formatLastEdited(lastEdited)}
                </p>

                <div className="owned-course-actions">
                    <button
                        type="button"
                        className="course-icon-button"
                        onClick={() => onEdit(courseId)}
                        aria-label={`Edit ${courseName}`}
                        title="Edit"
                    >
                        <img src="/icons/edit.png" alt="" aria-hidden="true" />
                    </button>

                    <button
                        type="button"
                        className="course-icon-button"
                        onClick={() => onToggleVisibility(courseId)}
                        aria-label={
                            isPublished
                                ? `Unpublish ${courseName}`
                                : `Publish ${courseName}`
                        }
                        title={isPublished ? "Unpublish" : "Publish"}
                    >
                        <img
                            src={
                                isPublished
                                    ? "/icons/visibilityOn.png"
                                    : "/icons/visibilityOff.png"
                            }
                            alt=""
                            aria-hidden="true"
                        />
                    </button>

                    <button
                        type="button"
                        className="course-icon-button danger"
                        onClick={() => onDelete(courseId)}
                        aria-label={`Delete ${courseName}`}
                        title="Delete"
                    >
                        <img src="/icons/delete.png" alt="" aria-hidden="true" />
                    </button>
                </div>
            </div>
        </div>
    );
}