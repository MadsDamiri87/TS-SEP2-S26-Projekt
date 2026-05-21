import {useEffect, useState} from "react";
import {CourseCard} from "../../components/coursecard/CourseCard.jsx";
import {
    getAllEnrolledCourses,
    getAllPublishedCourses
} from "../../api/courseApi.js";

export function LandingPage({isLoggedIn, userDetails}) {
    const [courses, setCourses] = useState([]);
    const [enrolledCourses, setEnrolledCourses] = useState([]);

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

    useEffect(() => {
        async function fetchEnrolledCourses() {
            if (!userDetails?.userId) {
                setEnrolledCourses([]);
                return;
            }

            try {
                const enrolled = await getAllEnrolledCourses(userDetails.userId);
                setEnrolledCourses(enrolled);
            } catch (error) {
                console.error("Failed to fetch enrolled courses:", error);
            }
        }

        fetchEnrolledCourses();

        window.addEventListener("enrollmentChange", fetchEnrolledCourses);
        return () => {
            window.removeEventListener("enrollmentChange", fetchEnrolledCourses);
        };
    }, [userDetails]);

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
                            (enrolledCourse) =>
                                enrolledCourse.courseId === course.courseId
                        );

                        return (
                            <CourseCard
                                key={course.courseId}
                                courseId={course.courseId}
                                title={course.title}
                                shortDescription={course.shortDescription}
                                price={course.price}
                                isEnrolled={isEnrolled}
                                isLoggedIn={isLoggedIn}
                                userDetails={userDetails}
                            />
                        );
                    })}
                </div>
            </div>
        </div>
    );
}