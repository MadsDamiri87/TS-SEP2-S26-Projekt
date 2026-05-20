import { api } from "./api.js";

export function register(username, password, email) {
    return api("/auth/register", {
        method: "POST",
        body: { username, password, email }
    }).then((data) => {
        if (data?.userId != null) {
            const userDetails = {
                userId: data.userId,
                username: data.username,
                email: data.email,
                isAdministrator: data.isAdministrator,
                isCourseParticipant: data.isCourseParticipant,
                isCourseProvider: data.isCourseProvider
            }
            localStorage.setItem("userDetails", JSON.stringify(userDetails));
            window.dispatchEvent(new Event("authChange"));
        }
        return data;
    });
}

export function login(username, password) {
    return api("/auth/login", {
        method: "POST",
        body: { username, password }
    }).then((data) => {
        if (data?.userId != null) {
            const userDetails = {
                userId: data.userId,
                username: data.username,
                email: data.email,
                isAdministrator: data.isAdministrator,
                isCourseParticipant: data.isCourseParticipant,
                isCourseProvider: data.isCourseProvider
            }
            localStorage.setItem("userDetails", JSON.stringify(userDetails));
            window.dispatchEvent(new Event("authChange"));
        }
        return data;
    });
}