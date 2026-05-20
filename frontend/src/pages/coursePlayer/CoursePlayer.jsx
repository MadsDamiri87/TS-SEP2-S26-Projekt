import {useParams} from "react-router-dom";
import {getCourseById} from "../../api/courseApi.js";
import {useState} from "react";

export function CoursePlayer() {
    const {courseId} = useParams();
    const [course, setCourse] = useState(null);

    <div className={"flex-row"}>
        <div className="flex-column">

        </div>
        <div className="module-accordion">
            {course.modules && course.modules.length > 0 ? (
                course.modules.map((module, index) => (
                    <details key={module.id || index}>
                        <summary>
                            <span>Module {index + 1} {module.title}</span>
                            <span>{getLessonText(module.lessons)}</span>
                        </summary>

                        <div className="module-content">
                            <p className="module-description">{module.description || "No description available for this module."}</p>
                            {module.lessons && module.lessons.length > 0 && (
                                <ul className="module-lessons">
                                    {module.lessons.map((lesson, lessonIndex) => (
                                        <li key={lesson.id || lessonIndex}>
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
}