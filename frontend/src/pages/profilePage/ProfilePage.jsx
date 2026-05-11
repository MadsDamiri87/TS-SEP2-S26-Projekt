import {FontAwesomeIcon} from "@fortawesome/react-fontawesome"
import {
    faCheck,
    faCircleUser,
    faPenToSquare
} from "@fortawesome/free-solid-svg-icons"
import {useState, useEffect} from "react"
import "./profilePage.css"
import {getUserProfile, updateUserProfile} from "../../api/userApi.js"
import PurchasedCourses
    from "../../components/purchasedcourses/PurchasedCourses.jsx"
import CreatedCoursesForProfile
    from "../../components/createdcourses/CreatedCoursesForProfile.jsx"

export function ProfilePage() {
    const userDetails = JSON.parse(localStorage.getItem("userDetails"));

    if (!userDetails) {
        return <div>Du er ikke logget ind</div>
    }

    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [phoneNumber, setPhoneNumber] = useState("");
    const [name, setName] = useState("");

    const [editingUsername, setEditingUsername] = useState(false);
    const [editingEmail, setEditingEmail] = useState(false);
    const [editingPhoneNumber, setEditingPhoneNumber] = useState(false);
    const [editingName, setEditingName] = useState(false);

    useEffect(() => {
        getUserProfile().then((data) => {
            setUsername(data.username);
            setEmail(data.email);
            setPhoneNumber(data.phoneNumber);
            setName(data.name);
        });
    }, []);

    async function handleSave(stopEditing) {
        try {
            await updateUserProfile(username, email, phoneNumber, name);
            stopEditing(false);
        } catch (error) {
            console.error("Failed to update profile:", error);
            alert("There was an error saving your information. Please try again.");
        }
    }

    return (
        <div className="page-container">
            <div>
                <h1>
                    Profile
                </h1>
                <p>Get an overview of your information and your courses on this
                    page.</p>
            </div>

            <div className="flex-row">
                <div className="flex-column">
                    <div className="section-heading">
                        <h3>{name ? name : ""}</h3>
                        <p>
                            {editingName
                                ? <>
                                    <input value={name}
                                           onChange={(e) => setName(e.target.value)}/>
                                    <FontAwesomeIcon className="fa-icon" icon={faCheck}
                                                     onClick={() => handleSave(setEditingName)}/>

                                </>
                                :
                                <>
                                    Name: {name ? name : "Add your name"}
                                    <FontAwesomeIcon className="fa-icon" icon={faPenToSquare}
                                                     onClick={() => setEditingName(true)}/>
                                </>
                            }
                        </p>
                        <p>
                            {editingUsername
                                ? <>
                                    <input value={username}
                                           onChange={(e) => setUsername(e.target.value)}/>
                                    <FontAwesomeIcon className="fa-icon" icon={faCheck}
                                                     onClick={() => handleSave(setEditingUsername)}/>
                                </>
                                :
                                <>
                                    Username: {username}
                                    <FontAwesomeIcon className="fa-icon" icon={faPenToSquare}
                                                     onClick={() => setEditingUsername(true)}/>
                                </>
                            }
                        </p>
                        <p>
                            {editingEmail
                                ? <>
                                    <input value={email}
                                           onChange={(e) => setEmail(e.target.value)}/>
                                    <FontAwesomeIcon className="fa-icon" icon={faCheck}
                                                     onClick={handleSave}/>
                                </>
                                :
                                <>
                                    Email: {email}
                                    <FontAwesomeIcon className="fa-icon" icon={faCheck}
                                                     onClick={() => handleSave(setEditingEmail)}/>
                                </>
                            }
                        </p>
                        <p>
                            {editingPhoneNumber
                                ? <>
                                    <input value={phoneNumber}
                                           onChange={(e) => setPhoneNumber(e.target.value)}/>
                                    <FontAwesomeIcon className="fa-icon" icon={faCheck}
                                                     onClick={handleSave}/>
                                </>
                                :
                                <>
                                    Phone: {phoneNumber}
                                    <FontAwesomeIcon className="fa-icon" icon={faCheck}
                                                     onClick={() => handleSave(setEditingPhoneNumber())}/>
                                </>
                            }
                        </p>
                    </div>
                    <CreatedCoursesForProfile/>
                </div>
                <div>
                    <PurchasedCourses/>
                </div>
            </div>
        </div>
    );
}