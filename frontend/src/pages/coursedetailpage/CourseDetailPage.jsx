import "./CourseDetailPage.css";
import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {
    getCourseById, getAllEnrolledCourses,
} from "../../api/courseApi.js";
import {BuyPopup} from "../../components/popup/buyform/BuyPopup.jsx";

export function CourseDetailPage() {
    const navigate = useNavigate();

    const {courseId} = useParams();
    const [course, setCourse] = useState(null);
    const [loading, setLoading] = useState(true);
    const [isBuyPopupOpen, setIsBuyPopOpen] = useState(false);
    const [enrolled, setEnrolled] = useState(false);

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

    useEffect(() => {
        async function fetchEnrollmentStatus() {
            const userDetails = JSON.parse(localStorage.getItem("userDetails"));
            console.log("userDetails:", userDetails);
            if (!userDetails?.userId) return;

            try {
                const enrolledCourses = await getAllEnrolledCourses(userDetails.userId);
                console.log("enrolledCourses:", enrolledCourses);
                console.log("courseId vi leder efter:", courseId);
                const isEnrolled = enrolledCourses.some(
                    (enrolledCourse) => enrolledCourse.courseId === Number(courseId)
                );
                console.log("isEnrolled:", isEnrolled);
                setEnrolled(isEnrolled);
            } catch (error) {
                console.error("Failed to fetch enrollment status:", error);
            }
        }

        fetchEnrollmentStatus();

        window.addEventListener("enrollmentChange", fetchEnrollmentStatus);
        return () => {
            window.removeEventListener("enrollmentChange", fetchEnrollmentStatus);
        };
    }, [courseId]);

    if (loading) return <div className="page-container"> Loading
        course... </div>;
    if (!course) return <div className="page-container"> Course not
        found... </div>;

    const formattedDate = new Date(course.lastEdited).toLocaleDateString("en-EN", {
        year: "numeric", month: "long", day: "numeric",
    });

    function handleBuyCourse() {
        setIsBuyPopOpen(true);
    }

    function handleContinueCourse(event) {
        event.stopPropagation();
        navigate(`/course-player/${courseId}`);
    }

    return (
        <div className="page-container">
            <div>
                <h1>{course.title}</h1>
                <p>{course.shortDescription}</p>
            </div>

            <div className="page-detail-content-wrapper flex-row">
                <section className="course-description-section">
                    <h2>Description</h2>
                    <p>
                        {course.description}
                    </p>
                </section>

                <section className="course-content-section">
                    {enrolled ? (
                        <div className="continue-learning"
                             onClick={handleContinueCourse}>
                                <span className="continue-text">
                                    Go to course
                                </span>

                            <span className="continue-arrow">
                                    →
                                </span>
                        </div>
                    ) : (
                        <div className="course-purchase-card">
                            <p className="course-price">{course.price} $</p>
                            <button className="highlight-btn"
                                    onClick={handleBuyCourse}>
                                Buy Course
                            </button>
                        </div>
                    )}
                    < h2> Course content</h2>

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
                            <p>This course does not have any modules available
                                yet.</p>
                        )}
                    </div>
                </section>
            </div>
            <div className="flex-column full-width">
                <section className="course-details-hero full-width">
                    <div>
                        <p className="course-kicker">Last
                            edited: {formattedDate}</p>
                    </div>
                </section>
            </div>

            {
                isBuyPopupOpen && (
                    <BuyPopup
                        course={course}
                        onClose={() => setIsBuyPopOpen(false)}
                    />
                )
            }

        </div>


    )
}
