import "./LandingPage.css"
import {CourseCard} from "../../components/coursecard/CourseCard.jsx";

export function LandingPage() {
    return (
        <div className="container">
            <h1>
                LearnHub
            </h1>
            <p>Discover courses across programming, design, business, and more. Learn at your own pace with structured
                lessons, practical projects, and content made to help you build real skills.</p>

            <h2>Popular courses</h2>
            <div className="course-container">
                <CourseCard/>
            </div>
        </div>
    )
}