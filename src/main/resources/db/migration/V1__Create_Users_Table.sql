CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE TB_USERS
(
    user_id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username         VARCHAR(50)  NOT NULL UNIQUE,
    email            VARCHAR(50)  NOT NULL UNIQUE,
    password         VARCHAR(100) NOT NULL,
    full_name        VARCHAR(150) NOT NULL,
    user_status      VARCHAR(20)  NOT NULL,
    user_type        VARCHAR(20)  NOT NULL,
    phone_number     VARCHAR(20),
    image_url        VARCHAR(255),
    creation_date    TIMESTAMP    NOT NULL,
    last_update_date TIMESTAMP    NOT NULL
);