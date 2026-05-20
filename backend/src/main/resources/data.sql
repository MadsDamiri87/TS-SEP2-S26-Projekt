-- ============================================================
-- Demo seed data for LMS project
-- Password for all users: password
--
-- This file seeds:
--   - 4 course providers
--   - 10 students
--   - 10 courses
--   - 30 modules
--   - 90 lessons
--   - 55 enrollments
--
-- It intentionally does NOT seed contents, because contents require
-- real physical files in your /data folder.
-- ============================================================

-- ============================================================
-- USERS
-- ============================================================

INSERT INTO users (
    user_id,
    username,
    hashed_password,
    email,
    phone_number,
    name,
    is_administrator,
    is_course_provider,
    is_course_participant
)
VALUES
-- Course providers
(1, 'provider', '$2a$10$9fPXpM35.WPnu1wCGyePO.zT/XkUBXJx0tfW2L1yqZIqaWo/3b8Zq', 'anna.provider@test.dk', '11111111', 'Anna Mikkelsen', false, true, false),
(2, 'provider2', '$2a$10$9fPXpM35.WPnu1wCGyePO.zT/XkUBXJx0tfW2L1yqZIqaWo/3b8Zq', 'marcus.provider@test.dk', '22222222', 'Marcus Jensen', false, true, false),
(3, 'provider3', '$2a$10$9fPXpM35.WPnu1wCGyePO.zT/XkUBXJx0tfW2L1yqZIqaWo/3b8Zq', 'sofia.provider@test.dk', '33333333', 'Sofia Nielsen', false, true, false),
(4, 'provider4', '$2a$10$9fPXpM35.WPnu1wCGyePO.zT/XkUBXJx0tfW2L1yqZIqaWo/3b8Zq', 'emil.provider@test.dk', '44444444', 'Emil Larsen', false, true, false),

-- Students
(5, 'student1', '$2a$10$9fPXpM35.WPnu1wCGyePO.zT/XkUBXJx0tfW2L1yqZIqaWo/3b8Zq', 'alma@student.dk', '55555501', 'Alma Pedersen', false, false, true),
(6, 'student2', '$2a$10$9fPXpM35.WPnu1wCGyePO.zT/XkUBXJx0tfW2L1yqZIqaWo/3b8Zq', 'oscar@student.dk', '55555502', 'Oscar Hansen', false, false, true),
(7, 'student3', '$2a$10$9fPXpM35.WPnu1wCGyePO.zT/XkUBXJx0tfW2L1yqZIqaWo/3b8Zq', 'freja@student.dk', '55555503', 'Freja Andersen', false, false, true),
(8, 'student4', '$2a$10$9fPXpM35.WPnu1wCGyePO.zT/XkUBXJx0tfW2L1yqZIqaWo/3b8Zq', 'noah@student.dk', '55555504', 'Noah Christensen', false, false, true),
(9, 'student', '$2a$10$9fPXpM35.WPnu1wCGyePO.zT/XkUBXJx0tfW2L1yqZIqaWo/3b8Zq', 'ida@student.dk', '55555505', 'Ida Sørensen', false, false, true),
(10, 'student6', '$2a$10$9fPXpM35.WPnu1wCGyePO.zT/XkUBXJx0tfW2L1yqZIqaWo/3b8Zq', 'victor@student.dk', '55555506', 'Victor Rasmussen', false, false, true),
(11, 'student7', '$2a$10$9fPXpM35.WPnu1wCGyePO.zT/XkUBXJx0tfW2L1yqZIqaWo/3b8Zq', 'emma@student.dk', '55555507', 'Emma Jørgensen', false, false, true),
(12, 'student8', '$2a$10$9fPXpM35.WPnu1wCGyePO.zT/XkUBXJx0tfW2L1yqZIqaWo/3b8Zq', 'william@student.dk', '55555508', 'William Møller', false, false, true),
(13, 'student9', '$2a$10$9fPXpM35.WPnu1wCGyePO.zT/XkUBXJx0tfW2L1yqZIqaWo/3b8Zq', 'clara@student.dk', '55555509', 'Clara Thomsen', false, false, true),
(14, 'student10', '$2a$10$9fPXpM35.WPnu1wCGyePO.zT/XkUBXJx0tfW2L1yqZIqaWo/3b8Zq', 'mikkel@student.dk', '55555510', 'Mikkel Holm', false, false, true);

-- ============================================================
-- COURSES
-- ============================================================

INSERT INTO courses (
    id,
    owner_id,
    title,
    short_description,
    description,
    price,
    is_published,
    last_edited
)
VALUES
    (1, 1, 'Java Programming Fundamentals', 'Learn Java from the ground up.', 'A beginner-friendly course covering Java syntax, variables, control flow, methods, classes, and object-oriented programming. The course is designed for students who want a solid foundation before moving into backend development.', 99.00, true, NOW()),
    (2, 1, 'Object-Oriented Design in Java', 'Design better Java applications.', 'Learn how to structure Java applications using encapsulation, inheritance, polymorphism, interfaces, and clean class design. The course focuses on writing code that is easier to understand, test, and maintain.', 129.00, true, NOW()),
    (3, 2, 'Spring Boot REST APIs', 'Build backend APIs with Spring Boot.', 'A practical course about controllers, services, repositories, DTOs, validation, error handling, and RESTful API design in Spring Boot. Students build realistic backend features step by step.', 149.00, true, NOW()),
    (4, 2, 'JPA and Hibernate Essentials', 'Learn database persistence with JPA.', 'Understand entities, relationships, repositories, transactions, cascading, lazy loading, and common Hibernate problems. This course is ideal for students building database-driven Java applications.', 139.00, true, NOW()),
    (5, 3, 'React for Beginners', 'Build interactive frontend applications.', 'Learn React components, props, state, events, lists, conditional rendering, and basic project structure with Vite. The course focuses on practical frontend development for beginners.', 119.00, true, NOW()),
    (6, 3, 'Modern React with Routing and APIs', 'Connect React apps to real backends.', 'A course covering React Router, fetch APIs, loading states, error handling, forms, reusable components, and common frontend architecture patterns used in real projects.', 149.00, true, NOW()),
    (7, 4, 'Database Design with PostgreSQL', 'Design relational databases properly.', 'Learn tables, primary keys, foreign keys, normalization, constraints, indexes, and practical SQL with PostgreSQL. The course helps students design databases that match application requirements.', 109.00, true, NOW()),
    (8, 4, 'Git and GitHub Workflow', 'Work better with branches and pull requests.', 'Learn Git basics, branching, merging, conflict handling, pull requests, and collaboration workflows using GitHub. The course is made for students working on team projects.', 79.00, true, NOW()),
    (9, 1, 'Software Testing with JUnit and Mockito', 'Write better automated tests.', 'Learn unit testing, test structure, mocking, service testing, integration test basics, and common testing patterns. The course focuses on confidence, readability, and maintainable tests.', 129.00, true, NOW()),
    (10, 2, 'Full Stack Project Bootcamp', 'Build a complete frontend and backend project.', 'A project-based course where students build a full stack application using Spring Boot, React, PostgreSQL, and REST APIs. The course connects backend, frontend, database, and testing into one workflow.', 199.00, true, NOW());

-- ============================================================
-- MODULES
-- ============================================================

INSERT INTO modules (
    module_id,
    course_id,
    name,
    description,
    order_number
)
VALUES
    (1, 1, 'Getting Started with Java', 'Set up Java, understand the project structure, and write your first programs.', 1),
    (2, 1, 'Control Flow and Methods', 'Learn conditions, loops, methods, parameters, and return values.', 2),
    (3, 1, 'Introduction to Classes', 'Understand classes, objects, fields, constructors, and methods.', 3),

    (4, 2, 'Core OOP Principles', 'Learn encapsulation, inheritance, polymorphism, and abstraction.', 1),
    (5, 2, 'Interfaces and Composition', 'Use interfaces and composition to build flexible Java applications.', 2),
    (6, 2, 'Designing Clean Classes', 'Practice responsibility-driven design and reduce duplicated logic.', 3),

    (7, 3, 'Spring Boot Basics', 'Create a Spring Boot project and understand the basic layers.', 1),
    (8, 3, 'REST Controllers and DTOs', 'Build REST endpoints and transfer data using request and response DTOs.', 2),
    (9, 3, 'Services and Repositories', 'Connect controllers, services, repositories, and business logic.', 3),

    (10, 4, 'Entity Mapping', 'Map Java classes to database tables using JPA annotations.', 1),
    (11, 4, 'Relationships', 'Work with many-to-one, one-to-many, and many-to-many relationships.', 2),
    (12, 4, 'Transactions and Fetching', 'Understand transactions, lazy loading, cascading, and common pitfalls.', 3),

    (13, 5, 'React Fundamentals', 'Learn components, JSX, props, and rendering.', 1),
    (14, 5, 'State and Events', 'Use state, event handlers, and controlled inputs.', 2),
    (15, 5, 'Lists and Conditional UI', 'Render lists, conditionally show UI, and split components.', 3),

    (16, 6, 'Routing with React Router', 'Create pages, routes, dynamic route parameters, and navigation.', 1),
    (17, 6, 'Fetching Backend Data', 'Use fetch, loading states, and error handling.', 2),
    (18, 6, 'Reusable UI Components', 'Build reusable cards, modals, forms, and layout components.', 3),

    (19, 7, 'Relational Database Basics', 'Understand tables, rows, columns, primary keys, and foreign keys.', 1),
    (20, 7, 'Constraints and Relationships', 'Design reliable schemas with constraints and relational links.', 2),
    (21, 7, 'Practical SQL Queries', 'Write SELECT, JOIN, INSERT, UPDATE, and DELETE statements.', 3),

    (22, 8, 'Git Basics', 'Learn commits, status, logs, and local repository workflow.', 1),
    (23, 8, 'Branches and Merges', 'Use branches, merge changes, and resolve conflicts.', 2),
    (24, 8, 'GitHub Collaboration', 'Work with remote repositories, pull requests, and reviews.', 3),

    (25, 9, 'Testing Fundamentals', 'Learn test structure, assertions, and Arrange-Act-Assert.', 1),
    (26, 9, 'Mocking with Mockito', 'Mock repositories and dependencies in service tests.', 2),
    (27, 9, 'Integration Testing Basics', 'Understand Spring Boot integration testing and database setup.', 3),

    (28, 10, 'Project Planning', 'Plan entities, use cases, API endpoints, and frontend pages.', 1),
    (29, 10, 'Backend Implementation', 'Build the backend using Spring Boot, JPA, services, and controllers.', 2),
    (30, 10, 'Frontend Integration', 'Connect React pages to the backend and create a complete user flow.', 3);

-- ============================================================
-- LESSONS
-- ============================================================

INSERT INTO lessons (
    lesson_id,
    module_id,
    title,
    description,
    order_number
)
VALUES
    (1, 1, 'Installing Java and IntelliJ', 'Install the tools needed to write and run Java applications.', 1),
    (2, 1, 'Your First Java Program', 'Write a simple Hello World program and understand the main method.', 2),
    (3, 1, 'Variables and Data Types', 'Learn about strings, numbers, booleans, and basic variable usage.', 3),

    (4, 2, 'If Statements', 'Use conditions to control how your program behaves.', 1),
    (5, 2, 'Loops', 'Repeat logic using for loops and while loops.', 2),
    (6, 2, 'Writing Methods', 'Create reusable methods with parameters and return values.', 3),

    (7, 3, 'Creating Classes', 'Define classes with fields and methods.', 1),
    (8, 3, 'Constructors', 'Initialize objects using constructors.', 2),
    (9, 3, 'Working with Objects', 'Create objects and use them in small programs.', 3),

    (10, 4, 'Encapsulation', 'Protect internal state using private fields and public methods.', 1),
    (11, 4, 'Inheritance', 'Reuse behavior through parent and child classes.', 2),
    (12, 4, 'Polymorphism', 'Use common types to work with different implementations.', 3),

    (13, 5, 'Interfaces', 'Define contracts with Java interfaces.', 1),
    (14, 5, 'Composition', 'Build objects by combining smaller objects.', 2),
    (15, 5, 'Choosing the Right Design', 'Compare inheritance and composition in practical examples.', 3),

    (16, 6, 'Single Responsibility', 'Give each class a clear and focused purpose.', 1),
    (17, 6, 'Reducing Duplication', 'Refactor repeated code into reusable methods and classes.', 2),
    (18, 6, 'Readable Code Structure', 'Organize code so it is easier to maintain and test.', 3),

    (19, 7, 'Creating a Spring Boot Project', 'Use Spring Initializr to create a backend project.', 1),
    (20, 7, 'Understanding the Project Structure', 'Learn what controllers, services, repositories, and entities are.', 2),
    (21, 7, 'Running the Application', 'Start the application and test a simple endpoint.', 3),

    (22, 8, 'Creating a REST Controller', 'Build your first GET and POST endpoints.', 1),
    (23, 8, 'Request DTOs', 'Receive data safely from the frontend.', 2),
    (24, 8, 'Response DTOs', 'Return clean data without exposing entities directly.', 3),

    (25, 9, 'Service Layer Logic', 'Move business logic out of controllers and into services.', 1),
    (26, 9, 'Repository Methods', 'Use Spring Data JPA repositories to load and save data.', 2),
    (27, 9, 'Handling Not Found Errors', 'Throw useful exceptions when resources cannot be found.', 3),

    (28, 10, 'Creating Entities', 'Map Java objects to database tables.', 1),
    (29, 10, 'Generated IDs', 'Use identity-generated primary keys.', 2),
    (30, 10, 'Column Constraints', 'Add nullable, unique, and length constraints.', 3),

    (31, 11, 'Many-to-One Relationships', 'Connect many child entities to one parent entity.', 1),
    (32, 11, 'One-to-Many Relationships', 'Model collections of related child entities.', 2),
    (33, 11, 'Bidirectional Helper Methods', 'Keep both sides of a relationship in sync.', 3),

    (34, 12, 'Transactional Service Methods', 'Use transactions for multi-step database changes.', 1),
    (35, 12, 'Lazy Loading Problems', 'Understand when lazy-loaded data can fail.', 2),
    (36, 12, 'Cascade and Orphan Removal', 'Control how child entities are saved and deleted.', 3),

    (37, 13, 'What is React?', 'Understand components and why React is useful.', 1),
    (38, 13, 'Writing JSX', 'Write HTML-like syntax inside JavaScript.', 2),
    (39, 13, 'Using Props', 'Pass data from parent components to child components.', 3),

    (40, 14, 'useState Basics', 'Store and update component state.', 1),
    (41, 14, 'Handling Click Events', 'Respond to user interactions.', 2),
    (42, 14, 'Controlled Inputs', 'Connect form inputs to React state.', 3),

    (43, 15, 'Rendering Lists', 'Use map to display arrays of data.', 1),
    (44, 15, 'Keys in React', 'Use stable keys when rendering lists.', 2),
    (45, 15, 'Conditional Rendering', 'Show or hide UI based on state.', 3),

    (46, 16, 'Setting Up Routes', 'Create basic routes for pages.', 1),
    (47, 16, 'Navigation', 'Use navigation links and programmatic navigation.', 2),
    (48, 16, 'Dynamic Routes', 'Use route parameters for detail pages.', 3),

    (49, 17, 'Calling a Backend API', 'Fetch JSON data from a REST endpoint.', 1),
    (50, 17, 'Loading and Error States', 'Show useful feedback while data loads or fails.', 2),
    (51, 17, 'Submitting Forms', 'Send POST and PUT requests from React.', 3),

    (52, 18, 'Reusable Cards', 'Create flexible card components for repeated UI.', 1),
    (53, 18, 'Modal Components', 'Build popups for confirmations and forms.', 2),
    (54, 18, 'Page Layout Components', 'Create reusable layout patterns for pages.', 3),

    (55, 19, 'Tables and Columns', 'Understand the basic structure of relational databases.', 1),
    (56, 19, 'Primary Keys', 'Use primary keys to identify rows.', 2),
    (57, 19, 'Foreign Keys', 'Connect rows across tables.', 3),

    (58, 20, 'Unique Constraints', 'Prevent duplicate data where it should not exist.', 1),
    (59, 20, 'Relationship Design', 'Choose the right table relationships for your data.', 2),
    (60, 20, 'Order Numbers', 'Store ordered data such as modules and lessons.', 3),

    (61, 21, 'Basic SELECT Queries', 'Read data from one table.', 1),
    (62, 21, 'Joining Tables', 'Combine related data using joins.', 2),
    (63, 21, 'Insert and Update Data', 'Create and modify rows using SQL.', 3),

    (64, 22, 'Initializing a Repository', 'Start tracking a project with Git.', 1),
    (65, 22, 'Making Commits', 'Save changes with clear commit messages.', 2),
    (66, 22, 'Viewing History', 'Inspect previous commits and changes.', 3),

    (67, 23, 'Creating Branches', 'Create branches for new features.', 1),
    (68, 23, 'Merging Branches', 'Merge completed work back into another branch.', 2),
    (69, 23, 'Resolving Conflicts', 'Handle files changed by multiple people.', 3),

    (70, 24, 'Using Remote Repositories', 'Push and pull code from GitHub.', 1),
    (71, 24, 'Pull Requests', 'Review and discuss changes before merging.', 2),
    (72, 24, 'Team Workflow', 'Use branches and pull requests in a group project.', 3),

    (73, 25, 'Why We Test', 'Understand the purpose of automated tests.', 1),
    (74, 25, 'Arrange Act Assert', 'Structure tests in a readable way.', 2),
    (75, 25, 'Testing Expected Results', 'Use assertions to verify behavior.', 3),

    (76, 26, 'Mocking Repositories', 'Replace database dependencies with mocks.', 1),
    (77, 26, 'Testing Service Methods', 'Verify service behavior in isolation.', 2),
    (78, 26, 'Verifying Interactions', 'Check that dependencies were called correctly.', 3),

    (79, 27, 'Spring Boot Test Setup', 'Create an integration test context.', 1),
    (80, 27, 'Test Database Strategy', 'Choose a safe database setup for tests.', 2),
    (81, 27, 'Cleaning Test Data', 'Avoid tests affecting each other.', 3),

    (82, 28, 'Planning Entities', 'Decide which entities your project needs.', 1),
    (83, 28, 'Planning Endpoints', 'Design the API routes your frontend will call.', 2),
    (84, 28, 'Planning Pages', 'Sketch the frontend pages and user flows.', 3),

    (85, 29, 'Creating the Backend Layers', 'Implement entities, repositories, services, and controllers.', 1),
    (86, 29, 'Connecting to PostgreSQL', 'Configure the backend to use a real database.', 2),
    (87, 29, 'Testing the API', 'Use HTTP requests to verify backend behavior.', 3),

    (88, 30, 'Creating the Frontend Pages', 'Build pages for listing, viewing, and managing data.', 1),
    (89, 30, 'Connecting Frontend to Backend', 'Call backend endpoints from React.', 2),
    (90, 30, 'Finishing the User Flow', 'Polish the full application flow from login to completed actions.', 3);

-- ============================================================
-- ENROLLMENTS
-- ============================================================
-- Popularity:
-- Course 10 = 10 enrollments
-- Course 3  = 9 enrollments
-- Course 5  = 8 enrollments
-- Course 1  = 7 enrollments
-- Course 6  = 6 enrollments
-- Course 4  = 5 enrollments
-- Course 7  = 4 enrollments
-- Course 9  = 3 enrollments
-- Course 2  = 2 enrollments
-- Course 8  = 1 enrollment

INSERT INTO enrollments (
    enrollment_id,
    user_id,
    course_id,
    enrolled_at
)
VALUES
    (1, 5, 10, NOW()),
    (2, 6, 10, NOW()),
    (3, 7, 10, NOW()),
    (4, 8, 10, NOW()),
    (5, 9, 10, NOW()),
    (6, 10, 10, NOW()),
    (7, 11, 10, NOW()),
    (8, 12, 10, NOW()),
    (9, 13, 10, NOW()),
    (10, 14, 10, NOW()),

    (11, 5, 3, NOW()),
    (12, 6, 3, NOW()),
    (13, 7, 3, NOW()),
    (14, 8, 3, NOW()),
    (15, 9, 3, NOW()),
    (16, 10, 3, NOW()),
    (17, 11, 3, NOW()),
    (18, 12, 3, NOW()),
    (19, 13, 3, NOW()),

    (20, 5, 5, NOW()),
    (21, 6, 5, NOW()),
    (22, 7, 5, NOW()),
    (23, 8, 5, NOW()),
    (24, 9, 5, NOW()),
    (25, 10, 5, NOW()),
    (26, 11, 5, NOW()),
    (27, 12, 5, NOW()),

    (28, 5, 1, NOW()),
    (29, 6, 1, NOW()),
    (30, 7, 1, NOW()),
    (31, 8, 1, NOW()),
    (32, 9, 1, NOW()),
    (33, 10, 1, NOW()),
    (34, 11, 1, NOW()),

    (35, 5, 6, NOW()),
    (36, 6, 6, NOW()),
    (37, 7, 6, NOW()),
    (38, 8, 6, NOW()),
    (39, 9, 6, NOW()),
    (40, 10, 6, NOW()),

    (41, 5, 4, NOW()),
    (42, 6, 4, NOW()),
    (43, 7, 4, NOW()),
    (44, 8, 4, NOW()),
    (45, 9, 4, NOW()),

    (46, 5, 7, NOW()),
    (47, 6, 7, NOW()),
    (48, 7, 7, NOW()),
    (49, 8, 7, NOW()),

    (50, 5, 9, NOW()),
    (51, 6, 9, NOW()),
    (52, 7, 9, NOW()),

    (53, 5, 2, NOW()),
    (54, 6, 2, NOW()),

    (55, 5, 8, NOW());

-- ============================================================
-- RESET POSTGRES IDENTITY SEQUENCES
-- ============================================================
-- These use pg_get_serial_sequence, so they should work even if
-- PostgreSQL generated slightly different sequence names.

SELECT setval(pg_get_serial_sequence('users', 'user_id'), (SELECT MAX(user_id) FROM users));
SELECT setval(pg_get_serial_sequence('courses', 'id'), (SELECT MAX(id) FROM courses));
SELECT setval(pg_get_serial_sequence('modules', 'module_id'), (SELECT MAX(module_id) FROM modules));
SELECT setval(pg_get_serial_sequence('lessons', 'lesson_id'), (SELECT MAX(lesson_id) FROM lessons));
SELECT setval(pg_get_serial_sequence('enrollments', 'enrollment_id'), (SELECT MAX(enrollment_id) FROM enrollments));
