import { api } from "./api.js";

export function createModule(courseId, name, description) {
    const body = { courseId, name, description };

    return api("/modules", {
        method: "POST",
        body: body
    });
}

export function getModuleById(moduleId) {
    return api(`/modules/module/${moduleId}`);
}

export function getAllModulesByCourseId(courseId) {
    return api(`/modules/${courseId}`);
}

export function updateModule(moduleId, courseId, name, description) {
    const body = { courseId, name, description };

    return api(`/modules/${moduleId}`, {
        method: "PUT",
        body: body
    });
}

export function deleteModule(moduleId) {
    return api(`/modules/${moduleId}`, {
        method: "DELETE"
    });
}