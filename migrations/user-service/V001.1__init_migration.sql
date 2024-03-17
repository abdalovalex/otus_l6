CREATE TABLE IF NOT EXISTS public."user"
(
    id         int4 NOT NULL,
    username   varchar(255) NULL,
    phone      varchar(255) NULL,
    birthday   date NULL,
    CONSTRAINT user_pkey PRIMARY KEY (id)
);