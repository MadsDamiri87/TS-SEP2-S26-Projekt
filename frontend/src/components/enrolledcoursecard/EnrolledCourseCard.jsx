import "./EnrolledCourseCard.css";
import {useNavigate} from "react-router-dom";

export function EnrolledCourseCard({
                                       courseId,
                                       title,
                                       shortDescription
                                   }) {

    const navigate = useNavigate();

    function handleOpenCourse() {
        navigate(`/courses/${courseId}`);
    }

    return (

        <div className="enrolled-course-card"
            onClick={handleOpenCourse}>

            <div className="enrolled-course-card-content">

                <div className="enrolled-course-card-header">
                    <h2>{title}</h2>
                </div>

                <p className="enrolled-course-description">
                    {shortDescription}
                </p>

                <div className="enrolled-course-footer">
                    <div className="continue-learning">
        <span className="continue-text">
            Continue Course
        </span>

                        <span className="continue-arrow">
            &gt;&gt;
        </span>
                    </div>
                </div>

            </div>


        </div>
    );
}