import "./MyCoursesPage.css";
import { useEffect, useState } from "react";
import { CourseCard } from "../../components/coursecard/CourseCard.jsx";
import { getAllEnrolledCourses } from "../../api/courseApi.js";

export function MyCoursesPage() {

    const [myCourses, setMyCourses] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {

        async function fetchMyCourses() {

            try {
                const userDetails = JSON.parse(localStorage.getItem("userDetails"));

                if (!userDetails) {
                    return;
                }

                const data = await getAllEnrolledCourses(userDetails.userId);

                setMyCourses(data);

            } catch (error) {
                console.error("Error fetching enrolled courses", error);

            } finally {
                setLoading(false);
            }
        }

        fetchMyCourses();

    }, []);

    if (loading) {
        return <p>Loading courses...</p>;
    }

    return (
        <div className="page-container">
            <h1>My Courses</h1>

            <div className="courses-grid">
                {myCourses.length > 0 ? (
                    myCourses.map((course) => (
                        <CourseCard
                            key={course.id}
                            course={course}
                        />
                    ))
                ) : (
                    <p>You are not enrolled in any courses yet.</p>
                )}
            </div>
        </div>
    );
}