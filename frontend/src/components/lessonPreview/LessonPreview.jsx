import {ContentPreview} from "../content/ContentPreview.jsx";

export default function LessonPreview({ lesson, contents }) {
    return (
        <div>
            <h2>{lesson?.title}</h2>
            <p>{lesson?.description}</p>

            <div className="flex-column">
                {contents.map((content) => (
                    <ContentPreview
                        key={content.contentId}
                        content={content}
                    />
                ))}
            </div>
        </div>
    );
}