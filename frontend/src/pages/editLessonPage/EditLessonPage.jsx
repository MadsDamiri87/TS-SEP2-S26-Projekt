import "./EditLessonPage.css";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import {
    deleteContent,
    getAllContentByLessonId,
    uploadContent
} from "../../api/contentApi.js";
import { ContentPreview } from "../../components/content/ContentPreview.jsx";
import { ContentListItem } from "../../components/content/ContentListItem.jsx";
import { ContentUploadModal } from "../../components/modal/ContentUploadModal.jsx";
import { ConfirmDeleteModal } from "../../components/modal/ConfirmDeleteModal.jsx";

export function EditLessonPage() {
    const navigate = useNavigate();
    const { lessonId } = useParams();

    const [contents, setContents] = useState([]);
    const [selectedContent, setSelectedContent] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState("");

    const [showUploadModal, setShowUploadModal] = useState(false);
    const [contentToDelete, setContentToDelete] = useState(null);

    async function loadContents({ selectLatest = false } = {}) {
        setIsLoading(true);

        try {
            const loadedContents = await getAllContentByLessonId(lessonId);

            const sortedContents = [...loadedContents].sort((a, b) =>
                a.orderNumber - b.orderNumber
            );

            setContents(sortedContents);

            if (sortedContents.length === 0) {
                setSelectedContent(null);
                setErrorMessage("");
                return;
            }

            if (selectLatest) {
                setSelectedContent(sortedContents[sortedContents.length - 1]);
            } else {
                setSelectedContent(previousSelectedContent => {
                    if (!previousSelectedContent) {
                        return sortedContents[0];
                    }

                    return sortedContents.find(content =>
                        content.contentId === previousSelectedContent.contentId
                    ) ?? sortedContents[0];
                });
            }

            setErrorMessage("");
        } catch (error) {
            console.error("Could not load content:", error);
            setErrorMessage("Could not load lesson content.");
        } finally {
            setIsLoading(false);
        }
    }

    useEffect(() => {
        loadContents();
    }, [lessonId]);

    function openUploadModal() {
        setErrorMessage("");
        setShowUploadModal(true);
    }

    function closeUploadModal() {
        setErrorMessage("");
        setShowUploadModal(false);
    }

    async function handleUpload(file) {
        setErrorMessage("");

        try {
            await uploadContent(lessonId, file);
            await loadContents({ selectLatest: true });

            setShowUploadModal(false);
        } catch (error) {
            console.error("Could not upload content:", error);
            setErrorMessage("Could not upload content.");
        }
    }

    function handleDelete(contentId) {
        const content = contents.find(content => content.contentId === contentId);

        if (!content) {
            return;
        }

        setErrorMessage("");
        setContentToDelete(content);
    }

    async function confirmDeleteContent() {
        if (!contentToDelete) {
            return;
        }

        setErrorMessage("");

        try {
            await deleteContent(contentToDelete.contentId);
            await loadContents();

            setContentToDelete(null);
        } catch (error) {
            console.error("Could not delete content:", error);
            setErrorMessage("Could not delete content.");
        }
    }

    if (isLoading) {
        return (
            <main className="edit-lesson-page">
                <p>Loading lesson content...</p>
            </main>
        );
    }

    return (
        <main className="edit-lesson-page">
            <section className="edit-lesson-header">
                <div>
                    <h1>Edit Lesson Content</h1>
                    <p>
                        Upload videos, images, and markdown files. Select content from the list to preview it.
                    </p>
                </div>

                <button
                    type="button"
                    className="back-button"
                    onClick={() => navigate(-1)}
                >
                    Back
                </button>
            </section>

            {errorMessage && (
                <p className="edit-lesson-error">{errorMessage}</p>
            )}

            <section className="edit-lesson-content">
                <section className="content-preview-panel">
                    <div className="content-preview-header">
                        <h2>Preview</h2>

                        {selectedContent && (
                            <span>{selectedContent.originalFileName}</span>
                        )}
                    </div>

                    <ContentPreview content={selectedContent} />
                </section>

                <section className="content-list-panel">
                    <div className="content-list-header">
                        <div>
                            <h2>Lesson content</h2>
                            <span>{contents.length === 1 ? "1 item" : `${contents.length} items`}</span>
                        </div>

                        <button
                            type="button"
                            onClick={openUploadModal}
                        >
                            +
                        </button>
                    </div>

                    <div className="content-list">
                        {contents.length === 0 ? (
                            <p className="empty-content-message">
                                No content uploaded yet.
                            </p>
                        ) : (
                            contents.map(content => (
                                <ContentListItem
                                    key={content.contentId}
                                    content={content}
                                    isSelected={selectedContent?.contentId === content.contentId}
                                    onSelect={setSelectedContent}
                                    onDelete={handleDelete}
                                />
                            ))
                        )}
                    </div>
                </section>
            </section>

            {showUploadModal && (
                <ContentUploadModal
                    onCancel={closeUploadModal}
                    onConfirm={handleUpload}
                />
            )}

            {contentToDelete && (
                <ConfirmDeleteModal
                    title="Delete Content"
                    message={`Are you sure you want to delete "${contentToDelete.originalFileName}"?`}
                    onCancel={() => setContentToDelete(null)}
                    onConfirm={confirmDeleteContent}
                />
            )}
        </main>
    );
}