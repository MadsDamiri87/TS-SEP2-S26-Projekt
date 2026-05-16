import {api} from "./api.js";

export function enrollInCourse(courseId) {
    const userDetails = JSON.parse(localStorage.getItem("userDetails"));
    const userId = userDetails?.userId;

    if (!userId) {
        throw new Error("No user found, please log in again.")
    }

    const
        body = {
            userId: userId,
            courseId: courseId
        };

    return api("/enrollments", {
        method: "POST",
        body: body
    }).then((data) => {
        if (data.userId && !userDetails) {
            localStorage.setItem("userDetails", JSON.stringify({
                userId: data.userId,
                isCourseParticipant: true
            }));
        }
        return data;
    });
}