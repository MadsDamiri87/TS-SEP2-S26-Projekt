import { Link } from "react-router-dom";
import "./MenuButton.css";

export function MenuButton({ buttonText, iconSrc, linkTo, onClick }) {
    const content = (
        <div className="menu-button">
            <div className="menu-button-icon-slot">
                <img
                    className="menu-button-icon"
                    src={iconSrc}
                    alt=""
                    aria-hidden="true"
                />
            </div>

            <span className="menu-button-text">
                {buttonText}
            </span>
        </div>
    );

    return (
        <div className="menu-button-wrapper">
            {linkTo ? (
                <Link
                    to={linkTo}
                    onClick={onClick}
                    title={buttonText}
                    aria-label={buttonText}
                    className="menu-button-link"
                >
                    {content}
                </Link>
            ) : (
                <button
                    type="button"
                    onClick={onClick}
                    title={buttonText}
                    aria-label={buttonText}
                    className="menu-button-link menu-button-clickable"
                >
                    {content}
                </button>
            )}
        </div>
    );
}