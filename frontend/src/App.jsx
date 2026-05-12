import './App.css'
import "./styles.css"
import { MobileMenu } from "./components/sidemenu/mobile_menu/MobileMenu.jsx";
import { DesktopMenu } from "./components/sidemenu/desktop_menu/DesktopMenu.jsx";
import { PopupModal } from "./components/popup/PopupModal.jsx";

import { useEffect, useState } from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";

import {LandingPage} from "./pages/landingpage/LandingPage.jsx";
import {CreateCoursePage} from "./pages/createCoursePage/CreateCoursePage.jsx";
import {CourseBuilderPage} from "./pages/coursebuilderpage/CourseBuilderPage.jsx";
import {ProfilePage} from "./pages/profilepage/ProfilePage.jsx";

function App() {
    const [isMobile, setIsMobile] = useState(window.innerWidth < 768);
    const [showPopup, setShowPopup] = useState(false);
    const [isLoginModalOpen, setIsLoginModalOpen] = useState(true);
    const [isLoggedIn, setIsLoggedIn] = useState(() => {
        return !!localStorage.getItem("userDetails")
    })

    const checkLoginStatus = () => {
        setIsLoggedIn(!!localStorage.getItem("userDetails"))
    }

    useEffect(() => {
        const handleResize = () => setIsMobile(window.innerWidth < 768);
        window.addEventListener("resize", handleResize);
        return () => window.removeEventListener("resize", handleResize);
    }, []);

    const openInLoginMode = () => {
        setIsLoginModalOpen(true);
        setShowPopup(true);
    }

    const openInRegisterMode = () => {
        setIsLoginModalOpen(false);
        setShowPopup(true);
    }


    return (
        <BrowserRouter>
            <div className={isMobile ? "mobile-layout" : "desktop-layout"}>
                {isMobile ? (
                    <MobileMenu
                        openLoginModal={openInLoginMode}
                        openRegisterModal={openInRegisterMode}
                        isLoggedIn={isLoggedIn}
                        checkLoginStatus={checkLoginStatus}
                    />
                ) : (
                    <DesktopMenu
                        openLoginModal={openInLoginMode}
                        openRegisterModal={openInRegisterMode}
                        isLoggedIn={isLoggedIn}
                        checkLoginStatus={checkLoginStatus}
                    />
                )}

                <main className="router-container">
                    <Routes>
                        <Route path="/" element={<LandingPage />} />
                        <Route path="/create-course" element={<CreateCoursePage />} />
                        <Route path="/course-builder" element={<CourseBuilderPage />} />
                        <Route path="/profile" element={<ProfilePage />} />
                    </Routes>
                </main>
            </div>

            <PopupModal
                isOpen={showPopup}
                isLogin={isLoginModalOpen}
                setIsLogin={setIsLoginModalOpen}
                onSubmit={checkLoginStatus}
                onClose={() => setShowPopup(false)}
            />
        </BrowserRouter>
    );
}

export default App;