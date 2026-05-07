import "./OwnedCourseItem.css";

export function OwnedCourseItem({
                                    courseId,
                                    courseName,
                                    isPublished,
                                    onEdit,
                                    onToggleVisibility,
                                    onDelete
                                }) {
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
    );
}