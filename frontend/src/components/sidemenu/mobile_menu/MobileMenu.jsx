import "./MobileMenu.css";
import { useState } from "react";

import { LoginMenu } from "../login_menu/LoginMenu.jsx";
import { BurgerButton } from "../burger_button/BurgerButton.jsx";
import { MenuButton } from "../menu_button/MenuButton.jsx";

export function MobileMenu({
                               openLoginModal,
                               openRegisterModal,
                               isLoggedIn,
                               checkLoginStatus
                           }) {
    const [isOpen, setIsOpen] = useState(false);

    const isCourseProvider = getIsCourseProvider();

    const toggleMenu = () => {
        setIsOpen((prev) => !prev);
    };

    const closeMenu = () => {
        setIsOpen(false);
    };

    const handleLogout = () => {
        localStorage.removeItem("userDetails");
        window.location.reload();
    };

    function getUsername() {
        const userDetails = JSON.parse(localStorage.getItem("userDetails"));
        if (userDetails == null) return "";
        return userDetails.username;
    }

    return (
        <>
            {isOpen && (
                <div
                    className="mobile-menu-backdrop"
                    onClick={closeMenu}
                />
            )}

            <div className={`mobile-menu-wrapper ${isOpen ? "mobile-menu-open" : ""}`}>
                <BurgerButton isOpen={isOpen} toggleMenu={toggleMenu} />

                <nav className="mobile-menu-content">
                    {isLoggedIn && (
                        <>
                            <section className="mobile-menu-section profile-section">
                                <MenuButton
                                    buttonText={"Hello, " + getUsername() + "!"}
                                    iconSrc="/icons/user.png"
                                    linkTo="/profile"
                                    onClick={closeMenu}
                                />
                            </section>

                            <div className="mobile-menu-divider" />

                            <section className="mobile-menu-section">
                                <MenuButton
                                    buttonText="Home"
                                    iconSrc="/icons/home.png"
                                    linkTo="/"
                                    onClick={closeMenu}
                                />

                                <MenuButton
                                    buttonText="Course Library"
                                    iconSrc="/icons/courseLibrary.png"
                                    linkTo="/course-library"
                                    onClick={closeMenu}
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
                                            onClick={closeMenu}
                                        />

                                        <MenuButton
                                            buttonText="Course Builder"
                                            iconSrc="/icons/courseBuilder.png"
                                            linkTo="/course-builder"
                                            onClick={closeMenu}
                                        />
                                    </section>
                                </>
                            )}

                            <div className="mobile-menu-spacer" />

                            <section className="mobile-menu-section">
                                <MenuButton
                                    buttonText="Create Course"
                                    iconSrc="/icons/createCourse.png"
                                    linkTo="/create-course"
                                    onClick={closeMenu}
                                />
                            </section>

                            <div className="mobile-menu-divider" />

                            <section className="mobile-menu-section bottom-section">
                                <MenuButton
                                    buttonText="Contact Support"
                                    iconSrc="/icons/contactSupport.png"
                                    linkTo="/support"
                                    onClick={closeMenu}
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

                    {!isLoggedIn && isOpen && (
                        <div className="mobile-login-menu-area">
                            <LoginMenu
                                onOpenLoginModal={() => {
                                    closeMenu();
                                    openLoginModal();
                                }}
                                onOpenRegisterModal={() => {
                                    closeMenu();
                                    openRegisterModal();
                                }}
                                onLogin={checkLoginStatus}
                                isOpenMenu={true}
                            />
                        </div>
                    )}
                </nav>
            </div>
        </>
    );
}

function getIsCourseProvider() {
    const userDetails = JSON.parse(localStorage.getItem("userDetails"));
    return userDetails?.isCourseProvider === true;
}