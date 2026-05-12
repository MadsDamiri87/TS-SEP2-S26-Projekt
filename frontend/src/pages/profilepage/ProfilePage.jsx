import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCheck, faPenToSquare } from "@fortawesome/free-solid-svg-icons";
import { useState, useEffect } from "react";
import "./ProfilePage.css";
import { getUserProfile, updateUserProfile } from "../../api/userApi.js";
import PurchasedCourses from "../../components/purchasedcourses/PurchasedCourses.jsx";
import CreatedCoursesForProfile from "../../components/createdcourses/CreatedCoursesForProfile.jsx";
import { useNavigate } from "react-router-dom";

export function ProfilePage() {
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [name, setName] = useState("");
  const [displayName, setDisplayName] = useState("");

  const [editingUsername, setEditingUsername] = useState(false);
  const [editingEmail, setEditingEmail] = useState(false);
  const [editingPhoneNumber, setEditingPhoneNumber] = useState(false);
  const [editingName, setEditingName] = useState(false);

  const [userDetails] = useState(() => {
    try {
      return JSON.parse(localStorage.getItem("userDetails"));
    } catch {
      return null;
    }
  });

  const [errors, setErrors] = useState({
    username: "",
    email: "",
  });

  useEffect(() => {
    if (!userDetails) {
      navigate("/access-denied");
      return;
    }

    getUserProfile()
      .then((data) => {
        setUsername(data.username);
        setEmail(data.email);
        setPhoneNumber(data.phoneNumber);
        setName(data.name);
        setDisplayName(data.name);
      })
      .catch((err) => {
        console.error("Failed to fetch profile", err);
      });
  }, []);

  if (!userDetails) {
    return <div>Log ind for at se din profil</div>;
  }

  async function handleSave(stopEditing) {
    try {
      setErrors({ name: "", username: "", email: "", phoneNumber: "" });
      await updateUserProfile(username, email, phoneNumber, name);
      setDisplayName(name);
      if (typeof stopEditing === "function") stopEditing(false);
    } catch (error) {
      if (error.fieldErrors) {
        setErrors(error.fieldErrors);
      } else if (error.message) {
        const msg = error.message.toLowerCase();
        if (msg.includes("username")) {
          setErrors((prev) => ({ ...prev, username: error.message }));
        } else if (msg.includes("email")) {
          setErrors((prev) => ({ ...prev, email: error.message }));
        } else {
          setErrors((prev) => ({ ...prev, username: error.message }));
        }
      } else {
        setErrors((prev) => ({ ...prev, username: "Something went wrong. Please try again." }));
      }
    }
  }

  return (
    <div className="page-container">
      <div>
        <h1>Profile</h1>
        <p>
          Get an overview of your information and your courses on this page.
        </p>
      </div>

      <div className="flex-row">
        <div className="flex-column">
          <div className="section-heading">
            <h3>
              Here is your profile information
              {displayName ? `, ${displayName}` : ""}
            </h3>
            <div>
              {editingName ? (
                <>
                  <span className="transparent">Name: </span>
                  <input
                    value={name}
                    maxLength={200}
                    onChange={(e) => {
                                          setName(e.target.value);
                                          setErrors((prev) => ({ ...prev, name: "" }));
                                        }}
                  />
                  <FontAwesomeIcon
                    className="fa-icon fa-icon-highlighted"
                    icon={faCheck}
                    onClick={() => handleSave(setEditingName)}
                  />
                  <p className="errorMessage">{errors.name}</p>
                </>
              ) : (
                <>
                  <span className="transparent">Name: </span>
                  {name ? name : "Add your name"}
                  <FontAwesomeIcon
                    className="fa-icon"
                    icon={faPenToSquare}
                    onClick={() => setEditingName(true)}
                  />
                </>
              )}
            </div>
            <div>
              {editingUsername ? (
                <>
                  <span className="transparent">Username: </span>
                  <input
                    value={username}
                    maxLength={30}
                    onChange={(e) => {
                      setUsername(e.target.value);
                      setErrors((prev) => ({ ...prev, username: "" }));
                    }}
                    pattern="^.{5,30}$"
                    title="Username must be between 5 and 30 characters"
                    required
                  />
                  <FontAwesomeIcon
                    className="fa-icon fa-icon-highlighted"
                    icon={faCheck}
                    onClick={() => handleSave(setEditingUsername)}
                  />
                  <p className="errorMessage">{errors.username}</p>
                </>
              ) : (
                <>
                  <span className="transparent">Username: </span>
                  {username}
                  <FontAwesomeIcon
                    className="fa-icon"
                    icon={faPenToSquare}
                    onClick={() => setEditingUsername(true)}
                  />
                </>
              )}
            </div>
            <div>
              {editingEmail ? (
                <>
                  <span className="transparent">Email: </span>
                  <input
                    value={email}
                    maxLength={100}
                    onChange={(e) => {
                      setEmail(e.target.value);
                      setErrors((prev) => ({ ...prev, email: "" }));
                    }}
                    required
                  />
                  <FontAwesomeIcon
                    className="fa-icon fa-icon-highlighted"
                    icon={faCheck}
                    onClick={() => handleSave(setEditingEmail)}
                  />
                  <p className="errorMessage">{errors.email}</p>
                </>
              ) : (
                <>
                  <span className="transparent">Email: </span>
                  {email}
                  <FontAwesomeIcon
                    className="fa-icon"
                    icon={faPenToSquare}
                    onClick={() => setEditingEmail(true)}
                  />
                </>
              )}
            </div>
            <div>
              {editingPhoneNumber ? (
                <>
                  <span className="transparent">Phone: </span>
                  <input
                    value={phoneNumber}
                    maxLength={20}
                    onChange={(e) => {
                                          setPhoneNumber(e.target.value);
                                          setErrors((prev) => ({ ...prev, phoneNumber: "" }));
                                        }}
                  />
                  <FontAwesomeIcon
                    className="fa-icon fa-icon-highlighted"
                    icon={faCheck}
                    onClick={() => handleSave(setEditingPhoneNumber)}
                  />
                  <p className="errorMessage">{errors.phoneNumber}</p>
                </>
              ) : (
                <>
                  <span className="transparent">Phone: </span>
                  {phoneNumber ? phoneNumber : "Add your phone number"}
                  <FontAwesomeIcon
                    className="fa-icon"
                    icon={faPenToSquare}
                    onClick={() => setEditingPhoneNumber(true)}
                  />
                </>
              )}
            </div>
          </div>
          <CreatedCoursesForProfile />
        </div>
        <div>
          <PurchasedCourses />
        </div>
      </div>
    </div>
  );
}