import "./MyCourseLibrary.css";
import {useEffect, useState} from "react";
import {getAllEnrolledCourses} from "../../api/courseApi.js";
import {EnrolledCourseCard} from "../../components/enrolledcoursecard/EnrolledCourseCard.jsx";

export function MyCourseLibrary() {

    const [myCourses, setMyCourses] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {

        async function fetchMyCourses() {

            try {
                const userDetails = JSON.parse(localStorage.getItem("userDetails"));

                if (!userDetails) {
                    return;
                }

                const data = await getAllEnrolledCourses(userDetails.userId);

                setMyCourses(data);

            } catch (error) {
                console.error("Error fetching enrolled courses", error);

            } finally {
                setLoading(false);
            }
        }

        fetchMyCourses();

    }, []);

    if (loading) {
        return (
            <div className="loading-state">
                Loading courses...
            </div>
        );
    }

    return (
        <div className="course-library-full-bleed">
            <div className="course-library-page">
                <div className="page-container">

                    <div className="library-header">

                        <div>
                            <h1>My Courses</h1>

                            <p className="library-subtitle">
                                Continue building your future.
                            </p>
                        </div>

                        <div className="dashboard-stat-card">

                            <div className="dashboard-stat-icon">
                                🎓
                            </div>

                            <div className="dashboard-stat-content">
                                <h2>{myCourses.length}</h2>

                                <p>Enrolled Courses</p>

                                <span className="dashboard-stat-subtext">
                            Keep learning!
                        </span>
                            </div>

                        </div>

                    </div>

                    <div className="courses-grid">

                        {myCourses.length > 0 ? (

                            myCourses.map((course) => (

                                <EnrolledCourseCard
                                    key={course.courseId}
                                    courseId={course.courseId}
                                    title={course.title}
                                    shortDescription={course.shortDescription}
                                />
                            ))

                        ) : (

                            <div className="empty-library">

                                <h2>No enrolled courses yet</h2>

                                <p>
                                    You have not enrolled in any courses yet.
                                </p>

                            </div>

                        )}

                    </div>

                </div>
            </div>
        </div>
    );
}