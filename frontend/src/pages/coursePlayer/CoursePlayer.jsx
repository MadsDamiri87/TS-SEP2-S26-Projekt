import {useNavigate, useParams} from "react-router-dom";
import { getCourseById } from "../../api/courseApi.js";
import { useEffect, useState } from "react";
import { getAllContentByLessonId } from "../../api/contentApi.js";
import LessonPreview from "../../components/lessonPreview/LessonPreview.jsx";

export function CoursePlayer() {
    const { courseId, lessonId } = useParams();
    const [course, setCourse] = useState(null);
    const [contents, setContents] = useState([]);
    const [selectedContent, setSelectedContent] = useState(null);
    const [isLoading, setIsLoading] = useState(false);
    const [errorMessage, setErrorMessage] = useState(null);
    const navigate = useNavigate();

    function getLessonText(lessons) {
        if (!lessons || lessons.length === 0) return "No lessons";
        return `${lessons.length} ${lessons.length === 1 ? "lesson" : "lessons"}`;
    }

    async function loadContents() {
        setIsLoading(true);
        try {
            const loadedContents = await getAllContentByLessonId(lessonId);
            const sortedContents = [...loadedContents].sort(
                (a, b) => a.orderNumber - b.orderNumber
            );
            setContents(sortedContents);
            if (sortedContents.length > 0) {
                setSelectedContent(sortedContents[0]);
            }
        } catch (error) {
            console.error("Could not load content:", error);
            setErrorMessage("Could not load lesson content.");
        } finally {
            setIsLoading(false);
        }
    }

    async function loadCourse() {
        try {
            const loadedCourse = await getCourseById(courseId);
            setCourse(loadedCourse);
        } catch (error) {
            console.error("Could not load course:", error);
        }
    }

    useEffect(() => {
        loadCourse();
    }, [courseId]);

    useEffect(() => {
        if (lessonId) {
            loadContents();
        }
    }, [lessonId]);

    const currentLesson = course?.modules
        ?.flatMap((m) => m.lessons ?? [])
        ?.find((l) => String(l.id) === String(lessonId));

    if (isLoading) {
        return (
            <main className="edit-lesson-page">
                <p>Loading lesson content...</p>
            </main>
        );
    }

    if (errorMessage) {
        return (
            <main className="edit-lesson-page">
                <p>{errorMessage}</p>
            </main>
        );
    }

    return (
        <main>
            <section>
                <div>
                    <h1>{course?.name}</h1>
                </div>
            </section>

            <div className="flex-row">
                <div>
                    <LessonPreview
                        lesson={currentLesson}
                        contents={contents}
                    />
                </div>

                <div className="module-accordion">
                    {course?.modules && course.modules.length > 0 ? (
                        course.modules.map((module, index) => (
                            <details key={module.id ?? index}>
                                <summary>
                                    <span>Module {index + 1} {module.title}</span>
                                    <span>{getLessonText(module.lessons)}</span>
                                </summary>

                                <div className="module-content">
                                    <p className="module-description">
                                        {module.description || "No description available for this module."}
                                    </p>
                                    {module.lessons && module.lessons.length > 0 && (
                                        <ul className="module-lessons">
                                            {module.lessons.map((lesson, lessonIndex) => (
                                                <li key={lesson.id ?? lessonIndex}>
                                                    {lessonIndex + 1}. {lesson.title}
                                                </li>
                                            ))}
                                        </ul>
                                    )}
                                </div>
                            </details>
                        ))
                    ) : (
                        <p>This course does not have any modules available yet.</p>
                    )}
                </div>
            </div>
        </main>
    );
}