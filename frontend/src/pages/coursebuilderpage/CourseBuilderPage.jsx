import "./CourseBuilderPage.css";
import { OwnedCourseItem } from "../../components/ownedcourses/OwnedCourseItem.jsx";
import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { getAllCreatedCourses } from "../../api/courseApi.js"

export function CourseBuilderPage() {
    const navigate = useNavigate();

    const [ownedCourses, setOwnedCourses] = useState([]);

    useEffect(() => {
        const userDetails = JSON.parse(localStorage.getItem("userDetails"));
        const userId = userDetails?.userId;

        if (!userId) {
            console.log("No user id found");
            return;
        }

        getAllCreatedCourses(userId)
            .then((courses) => {
                const sortedCourses = [...courses].sort((a, b) => {
                    return new Date(b.lastEdited) - new Date(a.lastEdited);
                });

                setOwnedCourses(sortedCourses);
            })
            .catch((error) => {
                console.error("Could not fetch created courses:", error);
            });
    }, []);

    const handleEdit = (courseId) => {
        console.log("Edit course:", courseId);
        // navigate(`/course-builder/${courseId}`);
    };

    const handleToggleVisibility = (courseId) => {
        console.log("Toggle visibility for course:", courseId);
    };

    const handleDelete = (courseId) => {
        console.log("Delete course:", courseId);
    };

    return (
        <div className="page-container">
            <section className="course-builder-header">
                <div>
                    <h1>Course Builder</h1>
                    <p>
                        Manage your courses in one place. View your owned courses,
                        update details, add lesson content, publish new courses,
                        or remove courses that are no longer needed.
                    </p>
                </div>

                <button
                    className="create-course-button"
                    onClick={() => navigate("/create-course")}
                >
                    Create Course
                </button>
            </section>

            <section className="owned-courses-panel">
                <div className="owned-courses-header">
                    <h2>Your Courses</h2>
                    <span>{ownedCourses.length} courses</span>
                </div>

                <div className="owned-course-list">
                    {ownedCourses.map((course) => (
                        <OwnedCourseItem
                            key={course.courseId}
                            courseId={course.courseId}
                            courseName={course.title}
                            isPublished={course.isPublished}
                            onEdit={handleEdit}
                            onToggleVisibility={handleToggleVisibility}
                            onDelete={handleDelete}
                        />
                    ))}
                </div>
            </section>
        </div>
    );
}