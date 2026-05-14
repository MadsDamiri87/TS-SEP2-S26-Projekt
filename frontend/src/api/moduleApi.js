import {api} from "./api.js";

export function createModule(courseId, name, description) {
    const body = {courseId, name, description}

    return api("/modules", {
        method: "POST",
        body: body
    }).then((data) => {
        return data
    })
}

export function getById(moduleId) {
    return api(`/modules/module/${moduleId}`)
        .then((data) => {
            return data
        })
}

export function getAllByCourseId(courseId) {
    return api(`/modules/${courseId}`)
        .then((data) => {
            return data
        })
}

export function updateModule(moduleId, courseId, name, description) {
    const body = {courseId, name, description}

    return api(`modules/${moduleId}`, {
        method: "PUT",
        body: body
    }).then((data) => {
        return data
    })
}

export function deleteModule(moduleId) {
    return api(`modules/${moduleId}`, {
        method: "DELETE"
    })
        .then((data) => {
            return data
        })
}