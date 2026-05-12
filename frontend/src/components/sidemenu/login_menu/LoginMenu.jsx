import "./LoginMenu.css";
import { MenuButton } from "../menu_button/MenuButton.jsx";

export function LoginMenu({ onOpenLoginModal, onOpenRegisterModal, isOpenMenu }) {
    return (
        <div className="login-menu">
            <MenuButton
                buttonText="Login"
                iconSrc="/icons/login.png"
                onClick={onOpenLoginModal}
            />

            {isOpenMenu && <button
                type="button"
                onClick={onOpenRegisterModal}
                className="register-link"
            >
                Not registered yet?
            </button>}
        </div>
    );
}