    import { useNavigate } from "react-router-dom";
    import "./CourseCard.css";
    import { useEffect, useState } from "react";
    import { BuyPopup } from "../popup/buyform/BuyPopup.jsx";
    import { getCourseById } from "../../api/courseApi.js";

    export function CourseCard({
                                   courseId,
                                   title,
                                   shortDescription,
                                   price,
                                   isEnrolled
                               }) {
        const navigate = useNavigate();

        const [course, setCourse] = useState(null);
        const[enrolled, setEnrolled] = useState(isEnrolled)
        const [isBuyPopupOpen, setIsBuyPopOpen] = useState(false);


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
        }, [courseId, isEnrolled]);

        useEffect(() => {
            setEnrolled(isEnrolled);
        }, [isEnrolled]);

        function handleReadMore() {
            navigate(`/course/${courseId}/${title}`);
        }

        function handleBuyCourse(event) {
            event.stopPropagation();
            setIsBuyPopOpen(true);
        }

        return (
            <>
                <div className="course-card" onClick={handleReadMore}>
                    <h3 className="course-card-title">
                        {title}
                    </h3>

                    <p className="course-card-description">
                        {shortDescription}
                    </p>

                    {!enrolled && (
                        <p className="course-card-price">
                            {"$" + price}
                        </p>
                    )}

                    <div className="course-card-actions">
                        {enrolled ? (
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

                                <button
                                    type="button"
                                    className="highlight-btn"
                                    onClick={handleBuyCourse}
                                >
                                    Purchase
                                </button>
                            </>
                        )}
                    </div>
                </div>

                {isBuyPopupOpen && course && (
                    <BuyPopup
                        course={course}
                        onClose={() => {
                            setIsBuyPopOpen(false)
                            setEnrolled(true)
                        }}
                    />
                )}
            </>
        );
    }