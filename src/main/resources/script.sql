
drop table if exists client;
CREATE TABLE client(
    id bigserial NOT NULL,
    first_name varchar(255) NOT NULL,
    middle_Initial varchar(255) NOT NULL,
    tipo_reg varchar(255) NOT NULL,
    filler varchar(255) NOT NULL,
    CONSTRAINT client_pkey PRIMARY KEY(id)
);

drop table if exists customer2;

CREATE TABLE customer2(
    id bigserial NOT NULL,
    first_name varchar(255) NOT NULL,
    middle_Initial varchar(255) NOT NULL,
    last_name varchar(255) NOT NULL,
    tipo_reg varchar(255) NOT NULL,
    CONSTRAINT customer2_pkey PRIMARY KEY(id)
);
