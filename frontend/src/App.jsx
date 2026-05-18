import './App.css'
import "./styles.css"
import {MobileMenu} from "./components/sidemenu/mobile_menu/MobileMenu.jsx";
import {DesktopMenu} from "./components/sidemenu/desktop_menu/DesktopMenu.jsx";
import {LoginModal} from "./components/popup/login/LoginModal.jsx";

import {useEffect, useState} from "react";
import {BrowserRouter, Routes, Route} from "react-router-dom";

import {LandingPage} from "./pages/landingpage/LandingPage.jsx";
import {CreateCoursePage} from "./pages/createCoursePage/CreateCoursePage.jsx";
import {
    CourseBuilderPage
} from "./pages/coursebuilderpage/CourseBuilderPage.jsx";
import {ProfilePage} from "./pages/profilepage/ProfilePage.jsx";
import {Error404Page} from "./pages/404page/Error404Page.jsx";
import {AccessDeniedPage} from "./pages/accessdeniedpage/AccessDeniedPage.jsx";
import {CourseDetailPage} from "./pages/coursedetailpage/CourseDetailPage.jsx";

import {EditCoursePage} from "./pages/editcoursepage/EditCoursePage.jsx";
import {EditLessonPage} from "./pages/editLessonPage/EditLessonPage.jsx";
import {BuyPopup} from "./components/popup/buyform/BuyPopup.jsx";
import {MyCourseLibrary} from "./pages/courseslibrary/MyCourseLibrary.jsx";

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
        window.addEventListener("authChange", checkLoginStatus);
        window.addEventListener("storage", checkLoginStatus);
        return () => {
            window.removeEventListener("resize", handleResize);
            window.removeEventListener("authChange", checkLoginStatus);
            window.removeEventListener("storage", checkLoginStatus);
        };
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
                        <Route path="/" element={<LandingPage/>}/>
                        <Route path="/create-course"
                               element={<CreateCoursePage/>}/>
                        <Route path="/course-builder"
                               element={<CourseBuilderPage/>}/>
                        <Route path="/edit-course/:courseId"
                               element={<EditCoursePage/>}/>
                        <Route path="/profile" element={<ProfilePage/>}/>
                        <Route path="/access-denied"
                               element={<AccessDeniedPage/>}/>
                        <Route path="/course/:courseId/:courseTitle"
                               element={<CourseDetailPage/>}/>
                        <Route path="/edit-lesson/:lessonId"
                               element={<EditLessonPage/>}/>
                        <Route path="/my-course-library" element={<MyCourseLibrary />} />


                        <Route path="*" element={<Error404Page/>}/>
                    </Routes>
                </main>
            </div>

            <LoginModal
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