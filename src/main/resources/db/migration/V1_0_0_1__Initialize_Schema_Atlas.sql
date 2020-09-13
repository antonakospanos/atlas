create table if not exists account
(
    id bigserial not null constraint account_pkey primary key,
    cellphone varchar,
    email varchar,
    external_id uuid,
    name varchar,
    password varchar,
    username varchar
);
create table if not exists alert
(
    id bigserial not null constraint alert_pkey primary key,
    external_id uuid,
    name varchar,
    account_id bigint constraint fk79itrv3n3895ix02y542iees references account
);
create table if not exists device
(
    id bigserial not null constraint device_pkey primary key,
    external_id varchar,
    last_contact timestamp,
    name varchar,
    uptime bigint,
    version varchar
);
create table if not exists account_device_associations
(
    account_id bigint not null constraint fk9bklgnhm5c23dh3y295lo5woo references account,
    device_id bigint not null constraint fktr525os5bw3vl46c1oi5audmm references device,
                              constraint account_device_associations_pkey primary key (account_id, device_id)
);
create table if not exists module
(
    id bigserial not null constraint module_pkey primary key,
    external_id varchar,
    name varchar,
    state varchar,
    type varchar,
    value varchar,
    device_id bigint constraint fkielur4slqxefjd45n0y8faymg references device
);
create table if not exists action
(
    id bigserial not null constraint action_pkey primary key,
    external_id uuid,
    name varchar,
    next_execution timestamp,
    period_in_secods bigint,
    state varchar,
    value varchar,
    account_id bigint constraint fkej1q9wxsh4xrj5j23cwykmr75 references account,
    module_id bigint constraint fktsnq1ev6vem9hdn72dmapn81 references module
);
create table if not exists condition
(
    id bigserial not null constraint condition_pkey primary key,
    action_id bigint constraint fk8wjooul4c5e9r19i477atusrk references action,
    alert_id bigint constraint fkq4wv5pwvggbohcpnjypuo77jp references alert
);
create table if not exists condition_or_statement
(
    id bigserial not null constraint condition_or_statement_pkey primary key,
    condition_id bigint constraint fkb4xmocdn4fmw5bushxf7rfd94 references condition
);
create table if not exists condition_and_statement
(
    id bigserial not null constraint condition_and_statement_pkey primary key,
    condition_or_statement_id bigint constraint fkavhlincc0pf7la4gh9d4yetvk references condition_or_statement
);
create table if not exists condition_statement
(
    id bigserial not null constraint condition_statement_pkey primary key,
    max_value double precision,
    min_value double precision,
    negated boolean,
    state varchar,
    value varchar,
    condition_and_statement_id bigint constraint fkfqlpejlyk41qy8opy9rhm8u0h references condition_and_statement,
    module_id bigint constraint fkb195u4gtnrtkorhgoti6alrsx references module
);

