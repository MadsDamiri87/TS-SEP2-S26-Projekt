import "./CourseBuilderPage.css";
import { OwnedCourseItem } from "../../components/ownedcourses/OwnedCourseItem.jsx"
import {useNavigate} from "react-router-dom";

const ownedCourses = [
    {
        id: 1,
        name: "React Basics",
        isPublished: true
    },
    {
        id: 2,
        name: "Advanced JavaScript",
        isPublished: false
    },
    {
        id: 3,
        name: "UI Design Fundamentals",
        isPublished: true
    }
];

export function CourseBuilderPage() {
    const navigate = useNavigate()
    const handleEdit = (courseId) => {
        console.log("Edit course:", courseId);

        // later you can navigate with the id:
        // navigate(`/course-builder/${courseId}`);
    };

    const handleToggleVisibility = (courseId) => {
        console.log("Toggle visibility for course:", courseId);

        // later:
        // publish/unpublish course with this id
    };

    const handleDelete = (courseId) => {
        console.log("Delete course:", courseId);

        // later:
        // delete course with this id
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

                <button className="create-course-button"
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
                            key={course.id}
                            courseId={course.id}
                            courseName={course.name}
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