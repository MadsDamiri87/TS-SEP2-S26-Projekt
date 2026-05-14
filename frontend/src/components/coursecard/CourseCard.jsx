import { useNavigate} from "react-router-dom";
import "./CourseCard.css";


export function CourseCard({ courseId, title, shortDescription, price}) {
    const navigate = useNavigate();

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
            <p className="course-card-price">
                {"$" + price}
            </p>
            <div className="course-card-actions">
                <button type="button">
                    Read more
                </button>
                <button type="button" className="highlight-btn">
                    Purchase
                </button>
            </div>
        </div>
    );
}
