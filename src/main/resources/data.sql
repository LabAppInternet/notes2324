INSERT INTO user_lab (id, name, email)
VALUES (1, 'Pere', 'pere@tecnocampus.cat'),
       (2, 'Joan', 'joan@tecnocampus.cat'),
       (3, 'Marta', 'marta@tecnocampus.cat'),
       (4, 'Anna', 'anna@tecnocampus.cat'),
       (5, 'Jordi', 'jordi@tecnocampus.cat');

INSERT INTO note (id, title, content, creation_date, owner_id)
VALUES (1, 'Spring Boot Introduction', 'This note is about the basics of Spring Boot.', DATEADD('DAY', -1, CURRENT_DATE()), 1),
       (2, 'Spring Data JPA', 'This note is about using Spring Data JPA for database operations.', DATEADD('DAY', -1, CURRENT_DATE()), 1),
       (3, 'Spring Security', 'This note is about securing applications with Spring Security.', DATEADD('DAY', -1, CURRENT_DATE()), 1);
INSERT INTO note (id, title, content, creation_date, owner_id)
VALUES (4, 'Spring MVC', 'This note is about the basics of Spring MVC.', DATEADD('DAY', -1, CURRENT_DATE()), 2),
       (5, 'Spring REST', 'This note is about creating RESTful APIs with Spring.', DATEADD('DAY', -1, CURRENT_DATE()), 2);

INSERT INTO tag (name)
VALUES ('Spring Boot'),
       ('Spring MVC'),
       ('Spring Data JPA'),
       ('Spring Security'),
       ('Spring REST'),
       ('Spring Cloud'),
       ('Spring AOP');

INSERT INTO note_tag (note_id, tag_id)
VALUES (1, 'Spring Boot'),
       (1, 'Spring MVC'),
       (1, 'Spring REST'),
       (4, 'Spring Boot'),
       (4, 'Spring Data JPA'),
       (2, 'Spring MVC');

INSERT INTO note_permission (note_id, allowed_id, can_edit, can_view)
VALUES (1, 4, 1, 1),
       (2, 4, 0, 1),
       (3, 4, 1, 1),
       (4, 4, 1, 1);
