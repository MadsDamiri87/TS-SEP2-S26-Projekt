import {api} from "./api.js";

export function createCourse(courseId, title, shortDescription, description, price) {
    return api("/courses/create", {
        method: "POST",
        body: {courseId, title, shortDescription, description, price}
    }).then((data) => {
        return data
    })
}

export function publishCourse(courseId) {
    return api(
        `/courses/publish/${courseId}`, {
            method: "POST"
        }).then((data) => {
        return data
    })
}

export function unPublishCourse(courseId) {
    return api(`/courses/unPublish/${courseId}`, {
        method: "POST"
    }).then((data) => {
        return data
    })
}

export function getAllPublishedCourses() {
    return api("/courses")
        .then((data) => {
            return data
        })
}

export function getAllCreatedCourses(userId) {
    return api(`/courses/created/${userId}`)
        .then((data) => {
            return data
        })
}

export function getCourseById(courseId) {
    return api(`/courses/${courseId}`)
        .then((data) => {
            return data
        })
}


