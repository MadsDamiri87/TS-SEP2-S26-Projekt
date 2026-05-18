import { useNavigate} from "react-router-dom";
import "./CourseCard.css";
import {useEffect, useState} from "react";
import {BuyPopup} from "../popup/buyform/BuyPopup.jsx";
import {getCourseById} from "../../api/courseApi.js";


export function CourseCard({ courseId, title, shortDescription, price}) {
    const navigate = useNavigate();
    const [course, setCourse] = useState(null);

    const [isBuyPopupOpen, setIsBuyPopOpen] = useState(false);


    const handleReadMore = () => {
        navigate(`/course/${course.courseId}/${course.title}`);

    };

    useEffect(() => {
        async function fetchDetails() {
            try {
                const data = await getCourseById(courseId);
                setCourse(data);
            } catch (error) {
                console.error("Error fetching course", error);
            }
        }

        fetchDetails();
    }, [courseId]);


    function handleBuyCourse() {
        setIsBuyPopOpen(true);
    }

    return (
        <div className="course-card" >
            <h3 className="course-card-title"onClick={handleReadMore}>
                {title}
            </h3>
            <p className="course-card-description" onClick={handleReadMore}>
                {shortDescription}
            </p>
            <p className="course-card-price" onClick={handleReadMore}>
                {"$" + price}
            </p>
            <div className="course-card-actions">
                <button type="button" onClick={handleReadMore}>
                    Read more
                </button>
                <button type="button" className="highlight-btn" onClick={handleBuyCourse}>
                    Purchase
                </button>
            </div>
            {isBuyPopupOpen && (
                <BuyPopup
                    course={course}
                    onClose={() => setIsBuyPopOpen(false)}
                />
            )}
        </div>
    );
}
