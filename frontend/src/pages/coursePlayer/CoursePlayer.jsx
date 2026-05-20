import { useEffect, useMemo, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getCourseById } from "../../api/courseApi.js";
import {
    getAllContentByLessonId,
    getContentFileUrl
} from "../../api/contentApi.js";
import "./CoursePlayer.css";

export function CoursePlayer() {
    const { courseId } = useParams();
    const navigate = useNavigate();

    const [course, setCourse] = useState(null);
    const [selectedLesson, setSelectedLesson] = useState(null);
    const [contents, setContents] = useState([]);

    const [isCourseLoading, setIsCourseLoading] = useState(true);
    const [isContentLoading, setIsContentLoading] = useState(false);

    const [courseError, setCourseError] = useState(null);
    const [contentError, setContentError] = useState(null);

    useEffect(() => {
        async function loadCourse() {
            setIsCourseLoading(true);
            setCourseError(null);

            try {
                const loadedCourse = await getCourseById(courseId);

                const sortedModules = [...(loadedCourse.modules ?? [])]
                    .sort((a, b) => a.orderNumber - b.orderNumber)
                    .map((module) => ({
                        ...module,
                        lessons: [...(module.lessons ?? [])].sort(
                            (a, b) => a.orderNumber - b.orderNumber
                        )
                    }));

                const normalizedCourse = {
                    ...loadedCourse,
                    modules: sortedModules
                };

                setCourse(normalizedCourse);

                const firstLesson = sortedModules
                    .flatMap((module) => module.lessons ?? [])
                    .at(0);

                setSelectedLesson(firstLesson ?? null);
            } catch (error) {
                console.error("Could not load course:", error);
                setCourseError("Could not load course.");
            } finally {
                setIsCourseLoading(false);
            }
        }

        loadCourse();
    }, [courseId]);

    useEffect(() => {
        async function loadLessonContents() {
            if (!selectedLesson?.lessonId) {
                setContents([]);
                return;
            }

            setIsContentLoading(true);
            setContentError(null);

            try {
                const loadedContents = await getAllContentByLessonId(
                    selectedLesson.lessonId
                );

                const sortedContents = [...(loadedContents ?? [])].sort(
                    (a, b) => a.orderNumber - b.orderNumber
                );

                setContents(sortedContents);
            } catch (error) {
                console.error("Could not load lesson content:", error);
                setContents([]);
                setContentError("Could not load lesson content.");
            } finally {
                setIsContentLoading(false);
            }
        }

        loadLessonContents();
    }, [selectedLesson]);

    const allLessons = useMemo(() => {
        return course?.modules?.flatMap((module) => module.lessons ?? []) ?? [];
    }, [course]);

    const selectedLessonIndex = allLessons.findIndex(
        (lesson) => lesson.lessonId === selectedLesson?.lessonId
    );

    const selectedModule = course?.modules?.find((module) =>
        module.lessons?.some(
            (lesson) => lesson.lessonId === selectedLesson?.lessonId
        )
    );

    function handleSelectLesson(lesson) {
        setSelectedLesson(lesson);
    }

    function handlePreviousLesson() {
        if (selectedLessonIndex <= 0) return;
        setSelectedLesson(allLessons[selectedLessonIndex - 1]);
    }

    function handleNextLesson() {
        if (selectedLessonIndex >= allLessons.length - 1) return;
        setSelectedLesson(allLessons[selectedLessonIndex + 1]);
    }

    if (isCourseLoading) {
        return (
            <main className="course-player-page">
                <div className="course-player-state-card">
                    Loading course...
                </div>
            </main>
        );
    }

    if (courseError) {
        return (
            <main className="course-player-page">
                <div className="course-player-state-card error">
                    {courseError}
                </div>
            </main>
        );
    }

    return (
        <main className="course-player-page">
            <header className="course-player-title-section">
                <div>
                    <p className="course-player-label">Course Player</p>
                    <h1>{course?.title}</h1>
                    <p>{course?.shortDescription}</p>
                </div>

                <button
                    type="button"
                    className="course-player-secondary-button"
                    onClick={() => navigate(-1)}
                >
                    Go back
                </button>
            </header>

            <section className="course-player-layout">
                <section className="course-player-main">
                    <div className="lesson-header-card">
                        <p className="course-player-label">
                            {selectedModule
                                ? `Module ${selectedModule.orderNumber}: ${selectedModule.name}`
                                : "Lesson"}
                        </p>

                        <h2>
                            {selectedLesson
                                ? selectedLesson.title
                                : "No lesson selected"}
                        </h2>

                        <p>
                            {selectedLesson?.description ??
                                "Select a lesson from the course content."}
                        </p>
                    </div>

                    <div className="lesson-preview-card">
                        {contentError && (
                            <div className="course-player-message error">
                                {contentError}
                            </div>
                        )}

                        {isContentLoading ? (
                            <div className="course-player-message">
                                Loading lesson content...
                            </div>
                        ) : (
                            <LessonContentPreview contents={contents} />
                        )}
                    </div>
                </section>

                <aside className="course-player-sidebar">
                    <div className="sidebar-heading">
                        <p className="course-player-label">Overview</p>
                        <h2>Course content</h2>
                    </div>

                    <div className="course-module-accordion">
                        {course?.modules?.length > 0 ? (
                            course.modules.map((module) => {
                                const isActiveModule = module.lessons?.some(
                                    (lesson) =>
                                        lesson.lessonId === selectedLesson?.lessonId
                                );

                                return (
                                    <details
                                        key={module.moduleId}
                                        open={isActiveModule}
                                        className="course-module-item"
                                    >
                                        <summary>
                                            <div>
                                                <strong>
                                                    Module {module.orderNumber}:{" "}
                                                    {module.name}
                                                </strong>

                                                <span>
                                                    {module.lessons?.length ?? 0}{" "}
                                                    {(module.lessons?.length ?? 0) === 1
                                                        ? "lesson"
                                                        : "lessons"}
                                                </span>
                                            </div>
                                        </summary>

                                        <div className="course-module-content">
                                            {module.description && (
                                                <p className="module-description">
                                                    {module.description}
                                                </p>
                                            )}

                                            <div className="lesson-list">
                                                {module.lessons?.map((lesson) => {
                                                    const isActive =
                                                        lesson.lessonId ===
                                                        selectedLesson?.lessonId;

                                                    return (
                                                        <button
                                                            key={lesson.lessonId}
                                                            type="button"
                                                            className={`lesson-list-item ${
                                                                isActive ? "active" : ""
                                                            }`}
                                                            onClick={() =>
                                                                handleSelectLesson(lesson)
                                                            }
                                                        >
                                                            <span className="lesson-number">
                                                                {lesson.orderNumber}
                                                            </span>

                                                            <span className="lesson-list-text">
                                                                <strong>
                                                                    {lesson.title}
                                                                </strong>
                                                                <small>
                                                                    {lesson.description}
                                                                </small>
                                                            </span>
                                                        </button>
                                                    );
                                                })}
                                            </div>
                                        </div>
                                    </details>
                                );
                            })
                        ) : (
                            <p className="empty-sidebar-text">
                                This course does not have any modules yet.
                            </p>
                        )}
                    </div>
                </aside>
            </section>

            <footer className="course-player-footer">
                <button
                    type="button"
                    className="course-player-nav-button"
                    disabled={selectedLessonIndex <= 0}
                    onClick={handlePreviousLesson}
                >
                    ← Previous lesson
                </button>

                <span>
                    {selectedLessonIndex >= 0
                        ? `Lesson ${selectedLessonIndex + 1} of ${allLessons.length}`
                        : `0 of ${allLessons.length}`}
                </span>

                <button
                    type="button"
                    className="course-player-nav-button highlighted"
                    disabled={
                        selectedLessonIndex === -1 ||
                        selectedLessonIndex >= allLessons.length - 1
                    }
                    onClick={handleNextLesson}
                >
                    Next lesson →
                </button>
            </footer>
        </main>
    );
}

function LessonContentPreview({ contents }) {
    if (!contents || contents.length === 0) {
        return (
            <div className="empty-content-card">
                <h3>No content yet</h3>
                <p>This lesson does not have any uploaded content yet.</p>
            </div>
        );
    }

    return (
        <div className="lesson-content-list">
            {contents.map((content) => (
                <ContentPreviewItem
                    key={content.contentId}
                    content={content}
                />
            ))}
        </div>
    );
}

function ContentPreviewItem({ content }) {
    const fileUrl = getContentFileUrl(content.contentId);
    const fileName = content.originalFileName ?? "";
    const contentType = content.contentType ?? "";

    const isImage =
        contentType.startsWith("image") ||
        /\.(png|jpg|jpeg|gif|webp|svg)$/i.test(fileName);

    const isVideo =
        contentType.startsWith("video") ||
        /\.(mp4|webm|ogg)$/i.test(fileName);

    const isPdf =
        contentType.includes("pdf") ||
        /\.pdf$/i.test(fileName);

    const isMarkdown =
        contentType.toLowerCase().includes("markdown") ||
        /\.md$/i.test(fileName);

    return (
        <article className="content-preview-item">
            <header className="content-preview-header">
                <div>
                    <p className="course-player-label">
                        Content {content.orderNumber}
                    </p>
                    <h3>{fileName || `Content ${content.contentId}`}</h3>
                </div>

                <a
                    href={fileUrl}
                    target="_blank"
                    rel="noreferrer"
                    className="open-file-button"
                >
                    Open file
                </a>
            </header>

            {isImage && (
                <img
                    src={fileUrl}
                    alt={fileName}
                    className="content-image-preview"
                />
            )}

            {isVideo && (
                <video
                    src={fileUrl}
                    controls
                    className="content-video-preview"
                />
            )}

            {isPdf && (
                <iframe
                    src={fileUrl}
                    title={fileName}
                    className="content-pdf-preview"
                />
            )}

            {isMarkdown && <MarkdownPreview fileUrl={fileUrl} />}

            {!isImage && !isVideo && !isPdf && !isMarkdown && (
                <div className="unknown-content-preview">
                    <p>This file type cannot be previewed directly.</p>
                    <a href={fileUrl} target="_blank" rel="noreferrer">
                        Open file
                    </a>
                </div>
            )}
        </article>
    );
}

function MarkdownPreview({ fileUrl }) {
    const [text, setText] = useState("");
    const [error, setError] = useState(null);

    useEffect(() => {
        async function loadMarkdown() {
            try {
                const response = await fetch(fileUrl);
                const markdownText = await response.text();
                setText(markdownText);
            } catch (error) {
                console.error("Could not load markdown:", error);
                setError("Could not preview markdown.");
            }
        }

        loadMarkdown();
    }, [fileUrl]);

    if (error) {
        return <p className="course-player-message error">{error}</p>;
    }

    return (
        <pre className="markdown-preview">
            {text || "Loading markdown..."}
        </pre>
    );
}