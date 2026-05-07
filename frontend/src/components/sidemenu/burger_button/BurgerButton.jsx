import { FaBars } from "react-icons/fa";
import { FaXmark } from "react-icons/fa6";
import "./BurgerButton.css";

export function BurgerButton({ isOpen, toggleMenu }) {
    return (
        <button
            type="button"
            onClick={toggleMenu}
            className="burger-button"
            aria-label={isOpen ? "Close menu" : "Open menu"}
        >
            {isOpen ? (
                <FaXmark size={24} />
            ) : (
                <FaBars size={24} />
            )}
        </button>
    );
}