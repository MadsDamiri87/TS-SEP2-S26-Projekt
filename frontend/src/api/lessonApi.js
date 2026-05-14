import { api } from "./api.js";

export function createLesson(moduleId, title, description) {
    const body = { moduleId, title, description };

    return api("/lessons", {
        method: "POST",
        body: body
    });
}

export function getLessonById(lessonId) {
    return api(`/lessons/lesson/${lessonId}`);
}

export function getAllLessonsByModuleId(moduleId) {
    return api(`/lessons/${moduleId}`);
}

export function updateLesson(lessonId, moduleId, title, description) {
    const body = { moduleId, title, description };

    return api(`/lessons/${lessonId}`, {
        method: "PUT",
        body: body
    });
}

export function deleteLesson(lessonId) {
    return api(`/lessons/${lessonId}`, {
        method: "DELETE"
    });
}