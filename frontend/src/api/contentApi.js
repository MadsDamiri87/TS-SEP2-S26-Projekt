import { api } from "./api.js";

export function getAllContentByLessonId(lessonId) {
    return api(`/contents/lesson/${lessonId}`);
}

export function uploadContent(lessonId, file) {
    const body = new FormData();
    body.append("lessonId", lessonId);
    body.append("file", file);

    return api("/contents", {
        method: "POST",
        body: body
    });
}

export function deleteContent(contentId) {
    return api(`/contents/${contentId}`, {
        method: "DELETE"
    });
}

export function getContentFileUrl(contentId) {
    return `http://localhost:8080/api/contents/${contentId}/file`;
}