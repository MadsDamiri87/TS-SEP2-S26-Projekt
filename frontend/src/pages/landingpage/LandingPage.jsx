import {useEffect, useState} from "react";
import {CourseCard} from "../../components/coursecard/CourseCard.jsx";
import {getAllEnrolledCourses, getAllPublishedCourses} from "../../api/courseApi.js";

export function LandingPage() {
    const [courses, setCourses] = useState([]);
    const [enrolledCourses, setEnrolledCourses] = useState([]);


    useEffect(() => {
        const userDetails = JSON.parse(localStorage.getItem("userDetails"));
        const userId = userDetails?.userId;

        getAllPublishedCourses()
            .then((data) => setCourses(data))
            .catch((error) => console.error(error));

        if (userId) {
            getAllEnrolledCourses(userId)
                .then((data) => setEnrolledCourses(data))
                .catch((error) => console.error(error));
        }

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
                <h1>Welcome to LearnHub!</h1>
                <p>
                    Discover courses across programming, design, business, and
                    more. Learn at your own pace with structured lessons,
                    practical projects,
                    and content made to help you build real skills.
                </p>
            </div>

            <div>
                <h2>Popular courses</h2>

                <div className="course-container">
                    {courses.map((course) => {
                        const isEnrolled = enrolledCourses.some(
                            (enrolledCourse) => enrolledCourse.courseId === course.courseId
                        );

                        return (
                            <CourseCard
                                key={course.courseId}
                                courseId={course.courseId}
                                title={course.title}
                                shortDescription={course.shortDescription}
                                price={course.price}
                                isEnrolled={isEnrolled}
                            />
                        );
                    })}
                </div>
            </div>
        </div>
    );
}