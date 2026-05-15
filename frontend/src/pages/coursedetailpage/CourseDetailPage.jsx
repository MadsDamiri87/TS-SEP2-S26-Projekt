import "./CourseDetailPage.css";
import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {getCourseById} from "../../api/courseApi.js";

export function CourseDetailPage() {
    const navigate = useNavigate();

    const {courseId, courseTitle} = useParams();
    const [course, setCourse] = useState(null);
    const [loading, setLoading] = useState(true);

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

    if(loading) return <div className="page-container"> Loading course... </div>;
    if(!course) return <div className="page-container"> Course not found... </div>;

    const formattedDate = new Date(course.lastEdited).toLocaleDateString("da-DK", {
        year: "numeric",
        month: "long",
        day: "numeric",
    });

    return (
        <div className="page-container">
            <div>
                <h1>{course.title}</h1>
                <p>{course.shortDescription}</p>
            </div>

            <section className="course-description-section">
                <h2>Description</h2>
                <p>
                    {course.description}
                </p>
            </section>
            <div className="page-detail-content">
                <section className="course-details-hero">
                    <div>
                        <p className="course-kicker">Course details </p>
                    </div>

                    <div className="course-purchase-card">
                        <p className="course-price">{course.price}</p>
                        <button className="highlight-btn">
                            Buy Course
                        </button>
                    </div>
                </section>


                <section className="course-content-section">
                    <h2>Course content</h2>

                    <div className="module-accordion">
                        <details>
                            <summary>
                                <span>Module 1: Introduction</span>
                                <span>3 lessons</span>
                            </summary>

                            <p>
                                Modulbeskrivelse her...
                            </p>
                        </details>

                        <details>
                            <summary>
                                <span>Module 2: Advanced topics</span>
                                <span>5 lessons</span>
                            </summary>

                            <p>
                                Modulbeskrivelse her...
                            </p>
                        </details>
                    </div>
                </section>
            </div>
        </div>
    )
}
