import { useEffect, useState } from "react";
import ReactMarkdown from "react-markdown";
import { getContentFileUrl } from "../../api/contentApi.js";

export function ContentPreview({ content }) {
    const [markdownText, setMarkdownText] = useState("");

    useEffect(() => {
        async function loadMarkdown() {
            if (!content || content.contentType !== "MARKDOWN") {
                setMarkdownText("");
                return;
            }

            try {
                const response = await fetch(getContentFileUrl(content.contentId));
                const text = await response.text();

                setMarkdownText(text);
            } catch (error) {
                console.error("Could not load markdown:", error);
                setMarkdownText("Could not load markdown file.");
            }
        }

        loadMarkdown();
    }, [content]);

    if (!content) {
        return (
            <div className="content-preview-empty">
                <p>Select content to preview.</p>
            </div>
        );
    }

    const fileUrl = getContentFileUrl(content.contentId);

    if (content.contentType === "IMAGE") {
        return (
            <div className="content-preview-body">
                <img
                    className="content-preview-image"
                    src={fileUrl}
                    alt={content.originalFileName}
                />
            </div>
        );
    }

    if (content.contentType === "VIDEO") {
        return (
            <div className="content-preview-body">
                <video
                    className="content-preview-video"
                    src={fileUrl}
                    controls
                />
            </div>
        );
    }

    if (content.contentType === "MARKDOWN") {
        return (
            <div className="content-preview-body">
                <div className="content-preview-markdown">
                    <ReactMarkdown>
                        {markdownText}
                    </ReactMarkdown>
                </div>
            </div>
        );
    }

    return (
        <div className="content-preview-empty">
            <p>Unsupported content type.</p>
        </div>
    );
}