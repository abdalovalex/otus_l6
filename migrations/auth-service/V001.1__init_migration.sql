CREATE TABLE IF NOT EXISTS public."user"
(
    id         SERIAL NOT NULL,
    username   varchar(255) NULL,
    password   varchar(255) NULL,
    email      varchar(255) NULL,
    first_name varchar(255) NULL,
    last_name  varchar(255) NULL,
    CONSTRAINT user_pkey PRIMARY KEY (id),
    CONSTRAINT username_uq UNIQUE(username)
);