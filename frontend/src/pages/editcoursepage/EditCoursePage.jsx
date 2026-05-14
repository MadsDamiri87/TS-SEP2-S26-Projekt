import "./EditCoursePage.css";
import { useState } from "react";

export function EditCoursePage() {
    const [course, setCourse] = useState({
        title: "",
        shortDescription: "",
        description: "",
        price: ""
    });

    const [modules, setModules] = useState([
        {
            moduleId: 1,
            orderNumber: 1,
            name: "Module one",
            description: "Module description",
            isOpen: true,
            lessons: [
                {
                    lessonId: 1,
                    orderNumber: 1,
                    title: "Lesson one",
                    description: "Lesson description"
                },
                {
                    lessonId: 2,
                    orderNumber: 2,
                    title: "Lesson two",
                    description: "Lesson description"
                }
            ]
        },
        {
            moduleId: 2,
            orderNumber: 2,
            name: "Module two",
            description: "Module description",
            isOpen: false,
            lessons: []
        }
    ]);

    function handleCourseChange(event) {
        const { name, value } = event.target;

        setCourse(previousCourse => ({
            ...previousCourse,
            [name]: value
        }));
    }

    function toggleModule(moduleId) {
        setModules(previousModules =>
            previousModules.map(module =>
                module.moduleId === moduleId
                    ? { ...module, isOpen: !module.isOpen }
                    : module
            )
        );
    }

    function handleSubmit(event) {
        event.preventDefault();

        console.log(course);
    }

    function handleAddModule() {
        console.log("Add module");
    }

    function handleEditModule(moduleId) {
        console.log("Edit module", moduleId);
    }

    function handleDeleteModule(moduleId) {
        console.log("Delete module", moduleId);
    }

    function handleAddLesson(moduleId) {
        console.log("Add lesson to module", moduleId);
    }

    function handleEditLesson(lessonId) {
        console.log("Edit lesson", lessonId);
    }

    function handleDeleteLesson(lessonId) {
        console.log("Delete lesson", lessonId);
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
                        {modules.map(module => (
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
                                            className={`accordion-arrow-button ${module.isOpen ? "open" : ""}`}
                                            onClick={() => toggleModule(module.moduleId)}
                                            aria-label={module.isOpen ? "Collapse module" : "Expand module"}
                                        >
                                            <span className="accordion-arrow" />
                                        </button>
                                    </div>
                                </div>

                                {module.isOpen && (
                                    <div className="lesson-list">
                                        {module.lessons.length === 0 ? (
                                            <p className="empty-lessons">No lessons yet.</p>
                                        ) : (
                                            module.lessons.map(lesson => (
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
                        ))}
                    </div>
                </section>
            </section>
        </main>
    );
}