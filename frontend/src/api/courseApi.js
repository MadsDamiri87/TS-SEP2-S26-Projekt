import {api} from "./api.js";

export function createCourse(ownerId, title, shortDescription, description, price) {
    const body = {ownerId, title, shortDescription, description, price}
    console.log("Create course request body:", body)
    return api("/courses/create", {
        method: "POST",
        body: body
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

export function getAllPublishedCourses() {
    return api("/courses")
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


