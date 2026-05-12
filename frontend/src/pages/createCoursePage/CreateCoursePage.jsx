import "./createCourse.css"
import {createCourse} from "../../api/courseApi.js"
import {useNavigate} from "react-router-dom";
import {useState} from "react";
import {ErrorModal} from "../../components/modal/error/ErrorModal.jsx";

export function CreateCoursePage() {
    const navigate = useNavigate();

    const [error, setError] = useState(null);

    async function handleSubmit(event) {
        event.preventDefault();

        const form = event.currentTarget;
        if (!form.checkValidity()) {
            form.reportValidity();
            return;
        }

        const formData = new FormData(form);

        try {
            await createCourse(
                formData.get("title"),
                formData.get("shortDescription"),
                formData.get("description"),
                formData.get("price")
            );

            navigate("/course-builder");
            window.location.reload()
        } catch (error) {
            console.error("Failed to create course:", error);
            setError({
                status: error.status || "Error",
                message: error.message || "There was an error saving your course. Please try again."
            });
        }
    }

    return (
        <div className="page-container">
            <div>
                <h1>
                    Create your own course
                </h1>
                <p>Create your course from scratch by filling in the fields
                    below. Upon creation, your course is saved as a draft,
                    allowing you to add modules and lessons later via the
                    Course Builder.</p>
            </div>


            <div className="section-heading">
                <p>
                    Start by filling in the basic information for your course.
                </p>
            </div>

            <div className="form-container">
                <form className="formular" onSubmit={handleSubmit}>
                    <div className="form-column">
                        <div className="form-group">
                            <label>Title</label>
                            <input name="title" type="text"
                                   placeholder="Enter course title" minLength="1" maxLength="100"
                                   required/>
                        </div>

                        <div className="form-group">
                            <label>Description</label>
                            <textarea name="description" rows="5"
                                      placeholder="Enter a full course description" minLength="5"
                                      maxLength="5000" required/>
                        </div>
                        <p className="form-footer-note">
                            * All courses are by default initially created as
                            drafts...
                        </p>
                    </div>

                    <div className="form-column">
                        <div className="form-group">
                            <label>Short description</label>
                            <textarea name="shortDescription" rows="2"
                                      placeholder="Will be shown on course cards!" minLength="2"
                                      maxLength="255"/>
                        </div>

                        <div className="form-end-group">
                            <div className="form-group">
                                <label>Price</label>
                                <input name="price" type="number" className="no-spinner"
                                       placeholder="$ 0.00" required/>
                            </div>

                            <div className="form-btn-group">

                                <button type="submit"
                                        className="create-course-btn">
                                    Create Course
                                </button>
                                <button className="cancel-course-btn" type="button"
                                        onClick={() => navigate("/")}>
                                    Go Back
                                </button>
                            </div>
                        </div>

                    </div>
                </form>
            </div>
            {error && (
                <ErrorModal
                errorStatus={error.status}
                errorMessage={error.message}
                onClose={() => setError(null)}
                />
            )}
        </div>
    );
}