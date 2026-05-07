import { useEffect, useState } from "react";
import { CourseCard } from "../../components/coursecard/CourseCard.jsx";
import { getAllPublishedCourses } from "../../api/courseApi.js";

export function LandingPage() {
    const [courses, setCourses] = useState([]);

    useEffect(() => {
        async function fetchCourses() {
            try {
                const publishedCourses = await getAllPublishedCourses();
                setCourses(publishedCourses);
            } catch (error) {
                console.error("Failed to fetch published courses:", error);
            }
        }

        fetchCourses();
    }, []);

    return (
        <div className="page-container">
            <div>
                <h1>LearnHub</h1>
                <p>
                    Discover courses across programming, design, business, and more.
                    Learn at your own pace with structured lessons, practical projects,
                    and content made to help you build real skills.
                </p>
            </div>

            <div>
                <h2>Popular courses</h2>

                <div className="course-container">
                    {courses.map((course) => (
                        <CourseCard
                            key={course.id}
                            title={course.title}
                            shortDescription={course.shortDescription}
                            price={course.price}
                        />
                    ))}
                </div>
            </div>
        </div>
    );
}