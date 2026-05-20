import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {getAllEnrolledCourses} from "../../api/courseApi.js";
import "./PurchasedCourses.css";

export default function PurchasedCourses() {
    const navigate = useNavigate();

    const [purchasedCourses, setPurchasedCourses] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState("");

    useEffect(() => {
        const userDetails = JSON.parse(localStorage.getItem("userDetails"));
        const userId = userDetails?.userId;

        if (!userId) {
            setIsLoading(false);
            return;
        }

        getAllEnrolledCourses(userId)
            .then((data) => {
                setPurchasedCourses(data);
            })
            .catch((error) => {
                console.error("Failed to fetch purchased courses", error);
                setErrorMessage("Could not load your purchased courses.");
            })
            .finally(() => {
                setIsLoading(false);
            });
    }, []);

    function handleOpenCourse(course) {
        navigate(`/course/${course.courseId}/${course.title}`);
    }

    return (
        <section className="purchased-profile-card">
            <div className="purchased-profile-header">
                <h3>Purchased courses</h3>
                <p>Courses you are currently enrolled in.</p>
            </div>

            {isLoading && (
                <p className="purchased-profile-muted">Loading courses...</p>
            )}

            {!isLoading && errorMessage && (
                <p className="purchased-profile-error">{errorMessage}</p>
            )}

            {!isLoading && !errorMessage && purchasedCourses.length === 0 && (
                <div className="purchased-profile-empty">
                    <h4>No purchased courses yet</h4>
                    <p>
                        When you buy or enroll in a course, it will appear here.
                    </p>
                </div>
            )}

            {!isLoading && !errorMessage && purchasedCourses.length > 0 && (
                <div className="purchased-profile-list">
                    {purchasedCourses.map((course) => (
                        <button
                            key={course.courseId}
                            type="button"
                            className="purchased-profile-item"
                            onClick={() => handleOpenCourse(course)}
                        >
                            <div>
                                <h4>{course.title}</h4>
                                <p>{course.shortDescription}</p>
                            </div>
                        </button>
                    ))}
                </div>
            )}
        </section>
    );
}