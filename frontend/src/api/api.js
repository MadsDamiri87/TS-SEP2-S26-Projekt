const BASE_URL = "http://localhost:8080/api";

export async function api(path, options = {}) {
    const isFormData = options.body instanceof FormData;

    const response = await fetch(`${BASE_URL}${path}`, {
        method: options.method || "GET",
        headers: {
            ...(isFormData ? {} : { "Content-Type": "application/json" }),
            ...(options.headers || {}),
        },
        body: options.body
            ? isFormData
                ? options.body
                : JSON.stringify(options.body)
            : undefined,
    });

    const contentType = response.headers.get("content-type") || "";
    const data = contentType.includes("application/json")
        ? await response.json()
        : await response.text();

    if (!response.ok) {
        throw {
            status: response.status,
            message:
                typeof data === "string"
                    ? data
                    : data?.message || `Request failed with status ${response.status}`,
            error: typeof data === "object" ? data?.error : null,
            fieldErrors: typeof data === "object" ? data?.fieldErrors : null,
            field: typeof data === "object" ? data?.field : null,
            timeStamp: typeof data === "object" ? data?.timeStamp : null,
        };
    }

    return data;
}