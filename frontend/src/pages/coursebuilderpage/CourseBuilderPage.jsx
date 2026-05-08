import "./CourseBuilderPage.css";
import {OwnedCourseItem} from "../../components/ownedcourses/OwnedCourseItem.jsx";
import {useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";
import { PublishCourseModal } from "../../components/modal/PublishCourseModal.jsx";
import {getAllCreatedCourses, publishCourse, unPublishCourse} from "../../api/courseApi.js";

export function CourseBuilderPage() {
    const navigate = useNavigate();

    const [ownedCourses, setOwnedCourses] = useState([]);

    // her
    const [showPublishModal, setShowPublishModal] = useState(false);
    const [selectedCourse, setSelectedCourse] = useState(null);
    // her

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

    const handleToggleVisibility = async (courseId) => {
        console.log("Clicked courseId:", courseId);
        console.log("Owned courses:", ownedCourses);

        const course = ownedCourses.find((course) => course.courseId === courseId);

        console.log("Found course:", course);
        if (!course) {
            return;
        }

        const message = course.isPublished
            ? "Are you sure you want to unpublish this course?"
            : "Are you sure you want to publish this course?";

        setSelectedCourse(course);
        setShowPublishModal(true);
        console.log("show modal should open");

    };

    const handleDelete = (courseId) => {
        console.log("Delete course:", courseId);
    };

    // her
    const confirmToggleVisibility = async () => {
        if (!selectedCourse) {
            return;
        }
        try {
            const updatedCourse = selectedCourse.isPublished
                ? await unPublishCourse(selectedCourse.courseId)
                : await publishCourse(selectedCourse.courseId);

            setOwnedCourses((prevCourses) =>
                prevCourses.map((course) =>
                    course.courseId === updatedCourse.courseId
                        ? updatedCourse
                        : course));
        }
        catch (error) {
            console.error("Could not update course visibility:", error);
        } finally {
            setShowPublishModal(false);
            setSelectedCourse(null);
        }
    }
    // her

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

            {showPublishModal && (
                <PublishCourseModal
                    selectedCourse={selectedCourse}
                    onCancel={() => {
                        setShowPublishModal(false);
                        setSelectedCourse(null);
                    }}
                    onConfirm={confirmToggleVisibility}
                />
            )}

        </div>
    );
}