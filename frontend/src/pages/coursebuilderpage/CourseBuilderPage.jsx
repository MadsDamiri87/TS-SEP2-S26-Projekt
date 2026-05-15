import "./CourseBuilderPage.css";
import { OwnedCourseItem } from "../../components/createdcourses/OwnedCourseItem.jsx";
import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { PublishCourseModal } from "../../components/modal/PublishCourseModal.jsx";
import {
    deleteCourse,
    getAllCreatedCourses,
    publishCourse,
    unPublishCourse
} from "../../api/courseApi.js";
import {ConfirmDeleteModal} from "../../components/modal/ConfirmDeleteModal.jsx";

export function CourseBuilderPage() {
    const navigate = useNavigate();

    const [ownedCourses, setOwnedCourses] = useState([]);
    const [showPublishModal, setShowPublishModal] = useState(false);
    const [selectedCourse, setSelectedCourse] = useState(null);
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [courseToDelete, setCourseToDelete] = useState(null);

    useEffect(() => {
        async function loadCreatedCourses() {
            const rawUserDetails = localStorage.getItem("userDetails");

            if (!rawUserDetails) {
                navigate("/access-denied");
                return;
            }

            const userDetails = JSON.parse(rawUserDetails);
            const userId = userDetails?.userId;

            if (!userId) {
                console.log("No user id found");
                navigate("/access-denied");
                return;
            }

            if (!userDetails.isCourseProvider) {
                navigate("/access-denied");
                return;
            }

            try {
                const courses = await getAllCreatedCourses(userId);

                const sortedCourses = [...courses].sort((a, b) => {
                    return new Date(b.lastEdited) - new Date(a.lastEdited);
                });

                setOwnedCourses(sortedCourses);
            } catch (error) {
                console.error("Could not fetch created courses:", error);
            }
        }

        loadCreatedCourses();
    }, [navigate]);

    function handleEdit(courseId) {
        navigate(`/edit-course/${courseId}`);
    }

    function handleToggleVisibility(courseId) {
        const course = ownedCourses.find((course) => course.courseId === courseId);

        if (!course) {
            return;
        }

        setSelectedCourse(course);
        setShowPublishModal(true);
    }

    function handleDelete(courseId) {
        const course = ownedCourses.find((course) => course.courseId === courseId);

        if (!course) {
            return;
        }

        setCourseToDelete(course);
        setShowDeleteModal(true);
    }

    async function confirmDeleteCourse() {
        if (!courseToDelete) {
            return;
        }

        try {
            await deleteCourse(courseToDelete.courseId);

            setOwnedCourses((previousCourses) =>
                previousCourses.filter((course) => course.courseId !== courseToDelete.courseId)
            );
        } catch (error) {
            console.error("Could not delete course:", error);
        } finally {
            setShowDeleteModal(false);
            setCourseToDelete(null);
        }
    }

    async function confirmToggleVisibility() {
        if (!selectedCourse) {
            return;
        }

        try {
            const updatedCourse = selectedCourse.isPublished
                ? await unPublishCourse(selectedCourse.courseId)
                : await publishCourse(selectedCourse.courseId);

            setOwnedCourses((previousCourses) =>
                previousCourses.map((course) =>
                    course.courseId === updatedCourse.courseId
                        ? updatedCourse
                        : course
                )
            );
        } catch (error) {
            console.error("Could not update course visibility:", error);
        } finally {
            setShowPublishModal(false);
            setSelectedCourse(null);
        }
    }

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

            {showDeleteModal && courseToDelete && (
                <ConfirmDeleteModal
                    title="Delete Course"
                    message={`Are you sure you want to delete "${courseToDelete.title}"?`}
                    onCancel={() => {
                        setShowDeleteModal(false);
                        setCourseToDelete(null);
                    }}
                    onConfirm={confirmDeleteCourse}
                />
            )}
        </div>
    );
}