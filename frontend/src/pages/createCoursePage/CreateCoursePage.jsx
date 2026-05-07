import "./createCourse.css"

export function CreateCoursePage() {
    return (
        <div className="page-container">
           <section className="create-course-hero">
            <div>
                <h1>
                    Create your own course!
                </h1>
                <p>On this page you are able to create your own course from scratch! You will need
                    to fill in all shown fields and by confirming creation, your course will be
                    created as a draft by default, which you then can go edit via your side-menu
                    through "Course Builder". <br/>The course is by default a draft (unpublished) so you
                    have the opportunity to add modules and lessons at a later time! <br/> <br/> Enjoy and
                        have fun! </p>
            </div>
           </section>
            <section className="create-course-section">

                <div className="section-heading">
                    <h2>Create Course</h2>
                    <p>
                        Start by filling in the basic information for your course.
                    </p>
                </div>

                <div className="form-container">
                    <form className="formular">

                        <div className="form-group">
                            <label>Title</label>
                            <input
                                type="text"
                                placeholder="Enter course title"
                            />
                        </div>

                        <div className="form-group">
                            <label>Short description</label>
                            <input
                                type="text"
                                placeholder="Enter a short description"
                            />
                        </div>

                        <div className="form-group">
                            <label>Description</label>
                            <textarea
                                rows="5"
                                placeholder="Enter course description"
                            />
                        </div>

                        <div id="price" className="form-group">
                            <label>Price</label>
                            <input
                                type="number"
                                className="no-spinner"
                                placeholder="$ 0.00"
                            />
                        </div>

                        <button
                            type="submit"
                            className="create-course-btn"
                        >
                            Create Course
                        </button>

                    </form>
                </div>
            </section>
        </div>
    );
}