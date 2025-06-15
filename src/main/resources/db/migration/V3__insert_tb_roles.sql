INSERT INTO TB_ROLES (role_id, role_name)
VALUES (gen_random_uuid(), 'ROLE_USER'),
       (gen_random_uuid(), 'ROLE_STUDENT'),
       (gen_random_uuid(), 'ROLE_INSTRUCTOR'),
       (gen_random_uuid(), 'ROLE_ADMIN');
