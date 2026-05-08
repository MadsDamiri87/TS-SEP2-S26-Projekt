import { useState } from "react";
import "./DesktopMenu.css";

import { UserDetails } from "../user_details/UserDetails.jsx";
import { LoginMenu } from "../login_menu/LoginMenu.jsx";
import { LogoutButton } from "../logout_button/LogoutButton.jsx";
import { MenuButton } from "../menu_button/MenuButton.jsx";
import {useNavigate} from "react-router-dom";

export function DesktopMenu({
                                openLoginModal,
                                openRegisterModal,
                                isLoggedIn,
                                checkLoginStatus
                            }) {
    const navigate = useNavigate()
    const [isMenuOpened, setIsMenuOpened] = useState(false);

    const isCourseProvider = getIsCourseProvider();

    const handleLogout = () => {
        localStorage.removeItem("userDetails")
        navigate("/")
        window.location.reload()
    }

    function getUsername() {
        let userDetails = JSON.parse(localStorage.getItem("userDetails"))
        if (userDetails == null) return ""
        return userDetails.username
    }

    return (
        <aside
            className="desktop-menu-wrapper"
            onMouseEnter={() => setIsMenuOpened(true)}
            onMouseLeave={() => setIsMenuOpened(false)}
        >
            {isLoggedIn && (
                <>
                    <section className="desktop-menu-section profile-section">
                        <MenuButton
                            buttonText={"Hello, " + getUsername() + "!"}
                            iconSrc="/icons/user.png"
                            linkTo="/profile"
                        />
                    </section>

                    <div className="desktop-menu-divider" />

                    <section className="desktop-menu-section">
                        <MenuButton
                            buttonText="Home"
                            iconSrc="/icons/home.png"
                            linkTo="/"
                        />

                        <MenuButton
                            buttonText="Course Library"
                            iconSrc="/icons/courseLibrary.png"
                            linkTo="/course-library"
                        />
                    </section>

                    {isCourseProvider && (
                        <>
                            <div className="desktop-menu-divider" />

                            <section className="desktop-menu-section">
                                <MenuButton
                                    buttonText="Business Insights"
                                    iconSrc="/icons/businessInsights.png"
                                    linkTo="/business-insights"
                                />

                                <MenuButton
                                    buttonText="Course Builder"
                                    iconSrc="/icons/courseBuilder.png"
                                    linkTo="/course-builder"
                                />
                            </section>
                        </>
                    )}

                    <div className="desktop-menu-spacer" />

                    <section className="desktop-menu-section">
                        <MenuButton
                            buttonText="Create Course"
                            iconSrc="/icons/createCourse.png"
                            linkTo="/create-course"
                        />
                    </section>

                    <div className="desktop-menu-divider" />

                    <section className="desktop-menu-section bottom-section">
                        <MenuButton
                            buttonText="Contact Support"
                            iconSrc="/icons/contactSupport.png"
                            linkTo="/support"
                        />

                        <MenuButton
                            buttonText="Logout"
                            iconSrc="/icons/logout.png"
                            linkTo="/"
                            onClick={handleLogout}
                        />
                    </section>
                </>
            )}

            {!isLoggedIn && isMenuOpened && (
                <div className="login-menu-area">
                    <LoginMenu
                        onOpenLoginModal={openLoginModal}
                        onOpenRegisterModal={openRegisterModal}
                    />
                </div>
            )}
        </aside>
    );
}

function getIsCourseProvider() {
    const userDetails = JSON.parse(localStorage.getItem("userDetails"));
    return userDetails?.isCourseProvider === true;
}