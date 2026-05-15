import "./EditCoursePage.css";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import {getAllCreatedCourses, updateCourse} from "../../api/courseApi.js";

import { CourseContentModal } from "../../components/modal/CourseContentModal.jsx";
import { ConfirmDeleteModal } from "../../components/modal/ConfirmDeleteModal.jsx";

import {
    createModule,
    updateModule,
    deleteModule
} from "../../api/moduleApi.js";

import {
    createLesson,
    updateLesson,
    deleteLesson
} from "../../api/lessonApi.js";

export function EditCoursePage() {
    const navigate = useNavigate();
    const { courseId } = useParams();

    const [course, setCourse] = useState({
        courseId: null,
        ownerId: null,
        title: "",
        shortDescription: "",
        description: "",
        price: "",
        modules: []
    });

    const [openModuleIds, setOpenModuleIds] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState("");

    const [modalMode, setModalMode] = useState(null);
    const [selectedModule, setSelectedModule] = useState(null);
    const [selectedLesson, setSelectedLesson] = useState(null);

    async function loadCourse() {
        const rawUserDetails = localStorage.getItem("userDetails");

        if (!rawUserDetails) {
            navigate("/");
            return;
        }

        let userDetails;

        try {
            userDetails = JSON.parse(rawUserDetails);
        } catch {
            setErrorMessage("Could not read logged in user.");
            setIsLoading(false);
            return;
        }

        const userId = userDetails?.userId ?? userDetails?.id;

        if (!userId) {
            setErrorMessage("Could not find logged in user.");
            setIsLoading(false);
            return;
        }

        try {
            const courses = await getAllCreatedCourses(userId);

            const selectedCourse = courses.find(course =>
                course.courseId === Number(courseId)
            );

            if (!selectedCourse) {
                setErrorMessage("Course was not found.");
                return;
            }

            setCourse({
                courseId: selectedCourse.courseId,
                ownerId: selectedCourse.ownerId,
                title: selectedCourse.title,
                shortDescription: selectedCourse.shortDescription,
                description: selectedCourse.description,
                price: selectedCourse.price,
                modules: selectedCourse.modules ?? []
            });

            setOpenModuleIds(
                (selectedCourse.modules ?? []).map(module => module.moduleId)
            );
        } catch (error) {
            console.error(error);
            setErrorMessage("Could not load course.");
        } finally {
            setIsLoading(false);
        }
    }

    useEffect(() => {
        loadCourse();
    }, [courseId]);

    function handleCourseChange(event) {
        const { name, value } = event.target;

        setCourse(previousCourse => ({
            ...previousCourse,
            [name]: value
        }));
    }

    function toggleModule(moduleId) {
        setOpenModuleIds(previousOpenModuleIds => {
            if (previousOpenModuleIds.includes(moduleId)) {
                return previousOpenModuleIds.filter(id => id !== moduleId);
            }

            return [...previousOpenModuleIds, moduleId];
        });
    }

    function closeModal() {
        setModalMode(null);
        setSelectedModule(null);
        setSelectedLesson(null);
    }

    function handleAddModule() {
        setModalMode("create-module");
    }

    function handleEditModule(moduleId) {
        const module = course.modules.find(module => module.moduleId === moduleId);

        if (!module) {
            return;
        }

        setSelectedModule(module);
        setModalMode("edit-module");
    }

    function handleDeleteModule(moduleId) {
        const module = course.modules.find(module => module.moduleId === moduleId);

        if (!module) {
            return;
        }

        setSelectedModule(module);
        setModalMode("delete-module");
    }

    function handleAddLesson(moduleId) {
        const module = course.modules.find(module => module.moduleId === moduleId);

        if (!module) {
            return;
        }

        setSelectedModule(module);
        setModalMode("create-lesson");
    }

    function handleEditLesson(lessonId) {
        const module = course.modules.find(module =>
            module.lessons.some(lesson => lesson.lessonId === lessonId)
        );

        if (!module) {
            return;
        }

        const lesson = module.lessons.find(lesson => lesson.lessonId === lessonId);

        if (!lesson) {
            return;
        }

        setSelectedModule(module);
        setSelectedLesson(lesson);
        setModalMode("edit-lesson");
    }

    function handleDeleteLesson(lessonId) {
        const module = course.modules.find(module =>
            module.lessons.some(lesson => lesson.lessonId === lessonId)
        );

        if (!module) {
            return;
        }

        const lesson = module.lessons.find(lesson => lesson.lessonId === lessonId);

        if (!lesson) {
            return;
        }

        setSelectedModule(module);
        setSelectedLesson(lesson);
        setModalMode("delete-lesson");
    }

    async function confirmCreateModule({ name, description }) {
        try {
            const createdModule = await createModule(course.courseId, name, description);

            setCourse(previousCourse => ({
                ...previousCourse,
                modules: [...previousCourse.modules, createdModule]
            }));

            setOpenModuleIds(previousIds => [...previousIds, createdModule.moduleId]);

            closeModal();
        } catch (error) {
            console.error("Could not create module:", error);
        }
    }

    async function confirmUpdateModule({ name, description }) {
        try {
            const updatedModule = await updateModule(
                selectedModule.moduleId,
                course.courseId,
                name,
                description
            );

            setCourse(previousCourse => ({
                ...previousCourse,
                modules: previousCourse.modules.map(module =>
                    module.moduleId === updatedModule.moduleId
                        ? updatedModule
                        : module
                )
            }));

            closeModal();
        } catch (error) {
            console.error("Could not update module:", error);
        }
    }

    async function confirmDeleteModule() {
        try {
            await deleteModule(selectedModule.moduleId);
            await loadCourse();

            closeModal();
        } catch (error) {
            console.error("Could not delete module:", error);
        }
    }

    async function confirmCreateLesson({ name, description }) {
        try {
            const createdLesson = await createLesson(
                selectedModule.moduleId,
                name,
                description
            );

            setCourse(previousCourse => ({
                ...previousCourse,
                modules: previousCourse.modules.map(module =>
                    module.moduleId === selectedModule.moduleId
                        ? {
                            ...module,
                            lessons: [...module.lessons, createdLesson]
                        }
                        : module
                )
            }));

            closeModal();
        } catch (error) {
            console.error("Could not create lesson:", error);
        }
    }

    async function confirmUpdateLesson({ name, description }) {
        try {
            const updatedLesson = await updateLesson(
                selectedLesson.lessonId,
                selectedModule.moduleId,
                name,
                description
            );

            setCourse(previousCourse => ({
                ...previousCourse,
                modules: previousCourse.modules.map(module =>
                    module.moduleId === selectedModule.moduleId
                        ? {
                            ...module,
                            lessons: module.lessons.map(lesson =>
                                lesson.lessonId === updatedLesson.lessonId
                                    ? updatedLesson
                                    : lesson
                            )
                        }
                        : module
                )
            }));

            closeModal();
        } catch (error) {
            console.error("Could not update lesson:", error);
        }
    }

    async function confirmDeleteLesson() {
        try {
            await deleteLesson(selectedLesson.lessonId);
            await loadCourse();

            closeModal();
        } catch (error) {
            console.error("Could not delete lesson:", error);
        }
    }

    async function handleSubmit(event) {
        event.preventDefault();
        try {
            await updateCourse(
                course.courseId,
                course.ownerId,
                course.title,
                course.shortDescription,
                course.description,
                Number(course.price)
            );

            navigate("/course-builder");
        } catch (error) {
            console.error("Could not update course:", error);
            setErrorMessage("Could not update course.");
        }
    }

    if (isLoading) {
        return (
            <main className="edit-course-page">
                <p>Loading course...</p>
            </main>
        );
    }

    if (errorMessage) {
        return (
            <main className="edit-course-page">
                <h1>Something went wrong</h1>
                <p>{errorMessage}</p>
            </main>
        );
    }

    return (
        <main className="edit-course-page">
            <section className="edit-course-header">
                <h1>Edit your course here!</h1>
                <p>
                    Update your course details, manage modules, and organize lessons in the order students should follow.
                </p>
            </section>

            <section className="edit-course-content">
                <form className="edit-course-form" onSubmit={handleSubmit}>
                    <label>
                        Title
                        <input
                            type="text"
                            name="title"
                            value={course.title}
                            onChange={handleCourseChange}
                            placeholder="title..."
                        />
                    </label>

                    <label>
                        Short Description
                        <input
                            type="text"
                            name="shortDescription"
                            value={course.shortDescription}
                            onChange={handleCourseChange}
                            placeholder="short description..."
                        />
                    </label>

                    <label>
                        Description
                        <textarea
                            name="description"
                            value={course.description}
                            onChange={handleCourseChange}
                            placeholder="description..."
                        />
                    </label>

                    <label>
                        Price
                        <input
                            type="number"
                            name="price"
                            value={course.price}
                            onChange={handleCourseChange}
                            placeholder="price..."
                            min="0"
                            step="0.01"
                        />
                    </label>

                    <button type="submit" className="save-course-button">
                        Save Course
                    </button>
                </form>

                <section className="course-structure">
                    <div className="course-structure-header">
                        <h2>Course content</h2>
                        <button type="button" onClick={handleAddModule}>
                            +
                        </button>
                    </div>

                    <div className="module-list">
                        {course.modules.length === 0 ? (
                            <p className="empty-lessons">No modules yet.</p>
                        ) : (
                            [...course.modules]
                                .sort((a, b) => a.orderNumber - b.orderNumber)
                                .map(module => {
                                    const isOpen = openModuleIds.includes(module.moduleId);

                                    return (
                                        <article className="module-item" key={module.moduleId}>
                                            <div className="module-header">
                                                <button
                                                    type="button"
                                                    className="module-toggle"
                                                    onClick={() => toggleModule(module.moduleId)}
                                                >
                                                    {module.orderNumber}: {module.name}
                                                </button>

                                                <div className="module-actions">
                                                    <button type="button" onClick={() => handleAddLesson(module.moduleId)}>
                                                        +
                                                    </button>

                                                    <button type="button" onClick={() => handleEditModule(module.moduleId)}>
                                                        Edit
                                                    </button>

                                                    <button type="button" onClick={() => handleDeleteModule(module.moduleId)}>
                                                        Delete
                                                    </button>

                                                    <button
                                                        type="button"
                                                        className={`accordion-arrow-button ${isOpen ? "open" : ""}`}
                                                        onClick={() => toggleModule(module.moduleId)}
                                                        aria-label={isOpen ? "Collapse module" : "Expand module"}
                                                    >
                                                        <span className="accordion-arrow" />
                                                    </button>
                                                </div>
                                            </div>

                                            {isOpen && (
                                                <div className="lesson-list">
                                                    {module.lessons.length === 0 ? (
                                                        <p className="empty-lessons">No lessons yet.</p>
                                                    ) : (
                                                        [...module.lessons]
                                                            .sort((a, b) => a.orderNumber - b.orderNumber)
                                                            .map(lesson => (
                                                                <div className="lesson-item" key={lesson.lessonId}>
                                                                    <span>
                                                                        {lesson.orderNumber}: {lesson.title}
                                                                    </span>

                                                                    <div className="lesson-actions">
                                                                        <button type="button" onClick={() => handleEditLesson(lesson.lessonId)}>
                                                                            Edit
                                                                        </button>

                                                                        <button type="button" onClick={() => handleDeleteLesson(lesson.lessonId)}>
                                                                            Delete
                                                                        </button>
                                                                    </div>
                                                                </div>
                                                            ))
                                                    )}
                                                </div>
                                            )}
                                        </article>
                                    );
                                })
                        )}
                    </div>
                </section>
            </section>

            {modalMode === "create-module" && (
                <CourseContentModal
                    title="Create Module"
                    nameLabel="Module name"
                    descriptionLabel="Module description"
                    onCancel={closeModal}
                    onConfirm={confirmCreateModule}
                />
            )}

            {modalMode === "edit-module" && selectedModule && (
                <CourseContentModal
                    title="Edit Module"
                    nameLabel="Module name"
                    descriptionLabel="Module description"
                    initialName={selectedModule.name}
                    initialDescription={selectedModule.description}
                    onCancel={closeModal}
                    onConfirm={confirmUpdateModule}
                />
            )}

            {modalMode === "delete-module" && selectedModule && (
                <ConfirmDeleteModal
                    title="Delete Module"
                    message={`Are you sure you want to delete "${selectedModule.name}"?`}
                    onCancel={closeModal}
                    onConfirm={confirmDeleteModule}
                />
            )}

            {modalMode === "create-lesson" && selectedModule && (
                <CourseContentModal
                    title="Create Lesson"
                    nameLabel="Lesson title"
                    descriptionLabel="Lesson description"
                    onCancel={closeModal}
                    onConfirm={confirmCreateLesson}
                />
            )}

            {modalMode === "edit-lesson" && selectedLesson && (
                <CourseContentModal
                    title="Edit Lesson"
                    nameLabel="Lesson title"
                    descriptionLabel="Lesson description"
                    initialName={selectedLesson.title}
                    initialDescription={selectedLesson.description}
                    onCancel={closeModal}
                    onConfirm={confirmUpdateLesson}
                />
            )}

            {modalMode === "delete-lesson" && selectedLesson && (
                <ConfirmDeleteModal
                    title="Delete Lesson"
                    message={`Are you sure you want to delete "${selectedLesson.title}"?`}
                    onCancel={closeModal}
                    onConfirm={confirmDeleteLesson}
                />
            )}
        </main>
    );
}