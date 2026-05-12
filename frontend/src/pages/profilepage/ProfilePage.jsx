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
      await updateUserProfile(username, email, phoneNumber, name);
      setDisplayName(name);
      if (typeof stopEditing === "function") {
        stopEditing(false);
      }
    } catch (error) {
      console.error("Failed to update profile:", error);
      alert("There was an error saving your information. Please try again.");
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
              Her er dine profiloplysninger
              {displayName ? `, ${displayName}` : ""}
            </h3>
            <p>
              {editingName ? (
                <>
                  <span className="transparent">Name: </span>
                  <input
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                  />
                  <FontAwesomeIcon
                    className="fa-icon fa-icon-highlighted"
                    icon={faCheck}
                    onClick={() => handleSave(setEditingName)}
                  />
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
            </p>
            <p>
              {editingUsername ? (
                <>
                  <span className="transparent">Username: </span>
                  <input
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    pattern="^.{5,30}$"
                    title="Username must be between 5 and 30 characters"
                    required
                  />
                  <FontAwesomeIcon
                    className="fa-icon fa-icon-highlighted"
                    icon={faCheck}
                    onClick={() => handleSave(setEditingUsername)}
                  />
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
            </p>
            <p>
              {editingEmail ? (
                <>
                  <span className="transparent">Email: </span>
                  <input
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                  />
                  <FontAwesomeIcon
                    className="fa-icon fa-icon-highlighted"
                    icon={faCheck}
                    onClick={() => handleSave(setEditingEmail)}
                  />
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
            </p>
            <p>
              {editingPhoneNumber ? (
                <>
                  <span className="transparent">Phone: </span>
                  <input
                    value={phoneNumber}
                    onChange={(e) => setPhoneNumber(e.target.value)}
                  />
                  <FontAwesomeIcon
                    className="fa-icon fa-icon-highlighted"
                    icon={faCheck}
                    onClick={() => handleSave(setEditingPhoneNumber)}
                  />
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
            </p>
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
