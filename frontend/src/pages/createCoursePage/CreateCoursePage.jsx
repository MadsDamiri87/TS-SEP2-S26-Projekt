import "./createCourse.css"

export function CreateCoursePage() {
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
                <form className="formular">
                    <div className="form-column">
                        <div className="form-group">
                            <label>Title</label>
                            <input type="text"
                                   placeholder="Enter course title"/>
                        </div>

                        <div className="form-group">
                            <label>Description</label>
                            <textarea rows="5"
                                      placeholder="Enter a full course description"/>
                        </div>
                        <p className="form-footer-note">
                            * All courses are by default initially created as
                            drafts...
                        </p>
                    </div>

                    <div className="form-column">
                        <div className="form-group">
                            <label>Short description</label>
                            <textarea rows="2"
                                      placeholder="Will be shown on course cards!"/>
                        </div>

                        <div className="form-end-group">
                            <div className="form-group">
                                <label>Price</label>
                                <input type="number" className="no-spinner"
                                       placeholder="$ 0.00"/>
                            </div>

                            <div className="form-btn-group">

                            <button type="submit"
                                        className="create-course-btn">
                                    Create Course
                                </button>
                                <button type="#" className="cancel-course-btn">
                                    Go Back
                                </button>
                            </div>
                        </div>


                    </div>


                </form>
            </div>
        </div>
    );
}