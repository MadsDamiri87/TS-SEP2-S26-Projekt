import "./CourseDetailPage.css";
import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {getCourseById} from "../../api/courseApi.js";
import {BuyPopup} from "../../components/popup/buyform/BuyPopup.jsx";

export function CourseDetailPage() {
    const navigate = useNavigate();

    const {courseId, courseTitle} = useParams();
    const [course, setCourse] = useState(null);
    const [loading, setLoading] = useState(true);
    const [isBuyPopupOpen, setIsBuyPopOpen] = useState(false);

    const getLessonText = (lessons) => {
        const count = lessons ? lessons.length : 0;
        return count === 1 ? "1 lesson" : `${count} lessons`;
    };

    useEffect(() => {
        async function fetchDetails() {
            try {
                const data = await getCourseById(courseId);
                setCourse(data);
            } catch (error) {
                console.error("Error fetching course", error);
            } finally {
                setLoading(false);
            }
        }

        fetchDetails();
    }, [courseId]);

    if (loading) return <div className="page-container"> Loading course... </div>;
    if (!course) return <div className="page-container"> Course not found... </div>;

    const formattedDate = new Date(course.lastEdited).toLocaleDateString("da-DK", {
        year: "numeric", month: "long", day: "numeric",
    });

    function handleBuyCourse() {
        const userDetails = localStorage.getItem("userDetails");

        if (!userDetails) {
            navigate("/login");
            return
        }
        setIsBuyPopOpen(true);
    }

    return (<div className="page-container">
        <div>
            <h1>{course.title}</h1>
            <p>{course.shortDescription}</p>
        </div>

        <div className="page-detail-content-wrapper">
            <section className="course-description-section">
                <h2>Description</h2>
                <p>
                    {course.description}
                </p>
            </section>

            <section className="course-content-section">
                <div className="course-purchase-card">
                    <p className="course-price">{course.price} $</p>
                    <button className="highlight-btn" onClick={handleBuyCourse}>
                        Buy Course
                    </button>
                </div>
                <h2>Course content</h2>

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
            </section>
        </div>
        <div className="page-detail-content">
            <section className="course-details-hero">
                <div>
                    <p className="course-kicker">Last edited: {formattedDate}</p>
                </div>
            </section>
        </div>

            {isBuyPopupOpen && (
                <BuyPopup
                    course={course}
                    onClose={() => setIsBuyPopOpen(false)}
                />
            )}

    </div>


)
}
