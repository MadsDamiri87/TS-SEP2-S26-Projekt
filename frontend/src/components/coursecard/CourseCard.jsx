import "./CourseCard.css";
import { useEffect, useState } from "react";

export function CourseCard({ courseId }) {
  const [course, setCourse] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    async function fetchCourse() {
      const data = await fetch(`/api/courses/${courseId}`).then((r) =>
        r.json(),
      );
      setCourse(data);
      setIsLoading(false);
    }
    fetchCourse();
  }, [courseId]);
  return (
    <div className="course-card">
      <h2 className="course-card-title">
        {isLoading ? "The best course in the world" : course.title}
      </h2>
      <p className="course-card-description">
        {isLoading
          ? "You will learn everything and become as clever as the worlds leading president by just signing up for this course!"
          : course.description}
      </p>
      <p className="course-card-price">
        {isLoading ? "1.000.000" : `${course.price} kr.`}
      </p>
      <div className="course-card-actions">
        <button type="button" disabled={isLoading}>
          Read more
        </button>
        <button type="button" className="highlight-btn">
          Purchase
        </button>
      </div>
    </div>
  );
}
