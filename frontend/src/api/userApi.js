import { api } from "./api.js";

export function getUserProfile() {
    const userDetails = JSON.parse(localStorage.getItem("userDetails"));
    const userId = userDetails?.userId;

    if (!userId) {
        console.error("No userId found in userDetails:", userDetails);
        return;
    }

    return api(`/user/${userId}`).then((data) => {
        return data;
    });
}

export function updateUserProfile(username, email, phoneNumber, name) {
    const userDetails = JSON.parse(localStorage.getItem("userDetails"));
    const userId = userDetails?.userId;

    if (!userId) {
        console.error("No userId found in userDetails:", userDetails);
        return;
    }

    return api(`/user/${userId}`, {
        method: "PUT",
        body: { username, email, phoneNumber, name }
    }).then((data) => {
        // Opdater localStorage med nye værdier
        localStorage.setItem("userDetails", JSON.stringify({
            ...userDetails,
            username: data.username,
            email: data.email
        }));
        return data;
    });
}