import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {getAllCreatedCourses} from "../../api/courseApi.js";
import "./CreatedCoursesForProfile.css";

export default function CreatedCoursesForProfile() {
    const navigate = useNavigate();

    const [createdCourses, setCreatedCourses] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState("");

    useEffect(() => {
        const userDetails = JSON.parse(localStorage.getItem("userDetails"));
        const userId = userDetails?.userId;

        if (!userId) {
            setIsLoading(false);
            return;
        }

        getAllCreatedCourses(userId)
            .then((data) => {
                const sortedCourses = [...data].sort((a, b) => {
                    return new Date(b.lastEdited) - new Date(a.lastEdited);
                });

                setCreatedCourses(sortedCourses);
            })
            .catch((error) => {
                console.error("Failed to fetch created courses", error);
                setErrorMessage("Could not load your created courses.");
            })
            .finally(() => {
                setIsLoading(false);
            });
    }, []);

    function handleEditCourse(courseId) {
        navigate(`/edit-course/${courseId}`);
    }

    function handleCreateCourse() {
        navigate("/create-course");
    }

    return (
        <section className="created-profile-card">
            <div className="created-profile-header">
                <div>
                    <h3>Created courses</h3>
                    <p>Courses you have created as a course provider.</p>
                </div>

                <button
                    type="button"
                    className="created-profile-create-button"
                    onClick={handleCreateCourse}
                >
                    Create course
                </button>
            </div>

            {isLoading && (
                <p className="created-profile-muted">Loading courses...</p>
            )}

            {!isLoading && errorMessage && (
                <p className="created-profile-error">{errorMessage}</p>
            )}

            {!isLoading && !errorMessage && createdCourses.length === 0 && (
                <div className="created-profile-empty">
                    <h4>No created courses yet</h4>
                    <p>
                        When you create a course, it will appear here.
                    </p>
                </div>
            )}

            {!isLoading && !errorMessage && createdCourses.length > 0 && (
                <div className="created-profile-list">
                    {createdCourses.map((course) => (
                        <article
                            key={course.courseId}
                            className="created-profile-item"
                        >
                            <div className="created-profile-main">
                                <div>
                                    <h4>{course.title}</h4>
                                    <p>{course.shortDescription}</p>
                                </div>

                                <span
                                    className={
                                        course.isPublished
                                            ? "created-profile-status published"
                                            : "created-profile-status draft"
                                    }
                                >
                                    {course.isPublished ? "Published" : "Draft"}
                                </span>
                            </div>

                            <div className="created-profile-footer">
                                <span>{course.price} kr.</span>

                                <button
                                    type="button"
                                    onClick={() => handleEditCourse(course.courseId)}
                                >
                                    Edit
                                </button>
                            </div>
                        </article>
                    ))}
                </div>
            )}
        </section>
    );
}