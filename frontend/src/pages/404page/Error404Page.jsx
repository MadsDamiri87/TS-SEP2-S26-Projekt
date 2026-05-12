import {useNavigate} from "react-router-dom";

export function Error404Page() {
    const navigate = useNavigate();

    return (
        <div className="page-container">

            <div>
                <h1>Page not found</h1>

                <p>
                    The page you are looking for does not exist or may have been moved.
                </p>

                <p>
                    Check the URL, or return to the homepage to continue browsing courses.
                </p>

                <button className="cancel-course-btn" type="button"
                        onClick={() => navigate("/")}>
                    Go Back
                </button>
            </div>
        </div>
    );
}