import { api } from "./api.js";

export function createLesson(moduleId, title, description) {
    const body = { moduleId, title, description };

    return api("/lessons", {
        method: "POST",
        body: body
    }).then((data) => {
        return data;
    });
}

export function getLessonById(lessonId) {
    return api(`/lessons/lesson/${lessonId}`)
        .then((data) => {
            return data;
        });
}

export function getAllByModuleId(moduleId) {
    return api(`/lessons/${moduleId}`)
        .then((data) => {
            return data;
        });
}

export function updateLesson(lessonId, moduleId, title, description) {
    const body = { moduleId, title, description };

    return api(`/lessons/${lessonId}`, {
        method: "PUT",
        body: body
    }).then((data) => {
        return data;
    });
}

export function deleteLesson(lessonId) {
    return api(`/lessons/${lessonId}`, {
        method: "DELETE"
    }).then((data) => {
        return data;
    });
}