export function ContentListItem({
                                    content,
                                    isSelected,
                                    onSelect,
                                    onDelete
                                }) {
    return (
        <article className={`content-list-item ${isSelected ? "selected" : ""}`}>
            <button
                type="button"
                className="content-list-main"
                onClick={() => onSelect(content)}
            >
                <span className="content-order-number">
                    {content.orderNumber}
                </span>

                <div className="content-list-text">
                    <h3>{content.originalFileName}</h3>
                    <p>{content.contentType}</p>
                </div>
            </button>

            <button
                type="button"
                className="content-delete-button"
                onClick={() => onDelete(content.contentId)}
                aria-label={`Delete ${content.originalFileName}`}
                title="Delete"
            >
                <img src="/icons/delete.png" alt="" aria-hidden="true" />
            </button>
        </article>
    );
}