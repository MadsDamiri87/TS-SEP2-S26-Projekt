import "./AccessDeniedPage.css"
import {useNavigate} from "react-router-dom";

export function AccessDeniedPage() {
    const navigate = useNavigate();

    return (
        <div className="page-container">
            <div>
                <h1>You do not have access to this page</h1>

                <p>
                    This page is only available to users with the required permissions.
                </p>

                <p>
                    If you think this is a mistake, try logging in again or return to the homepage.
                </p>

                <button className="cancel-course-btn" type="button"
                        onClick={() => navigate("/")}>
                    Go Back
                </button>
            </div>
        </div>
    );
}