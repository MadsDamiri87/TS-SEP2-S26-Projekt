import {useNavigate} from "react-router-dom";
import "./CourseCard.css";


export function CourseCard({courseId, title, shortDescription, price, variant = "default"}) {
    const navigate = useNavigate();

    const isEnrolled = variant === "enrolled"

    const handleReadMore = () => {
        navigate(`/course/${courseId}`);
    };

    return (
        <div className="course-card">
            <h3 className="course-card-title">
                {title}
            </h3>
            <p className="course-card-description">
                {shortDescription}
            </p>
            {!isEnrolled && (
                <p className="course-card-price">
                    {"$" + price}
                </p>
            )}
            <div className="course-card-actions">
                {isEnrolled ? (
                    <div className="continue-learning">
            <span className="continue-text">
                Continue Course
            </span>
                        <span className="continue-arrow">
                →
            </span>
                    </div>
                ) : (
                    <>
                        <button type="button" onClick={handleReadMore}>
                            Read more
                        </button>
                        <button type="button" className="highlight-btn">
                            Purchase
                        </button>
                    </>
                )}
            </div>
        </div>
    );
}
