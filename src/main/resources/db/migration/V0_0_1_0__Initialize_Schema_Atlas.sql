create sequence hibernate_sequence
;

create table account
(
  id bigserial not null
    constraint account_pkey
    primary key,
  cellphone varchar(255),
  email varchar(255),
  external_id uuid,
  name varchar(255),
  password varchar(255),
  username varchar(255)
)
;

create table account_device_associations
(
  account_id bigint not null
    constraint fk9bklgnhm5c23dh3y295lo5woo
    references account,
  device_id bigint not null,
  constraint account_device_associations_pkey
  primary key (account_id, device_id)
)
;

create table action
(
  id bigserial not null
    constraint action_pkey
    primary key,
  external_id uuid,
  next_execution timestamp,
  period_in_secods bigint,
  state varchar(255),
  value varchar(255),
  account_id bigint
    constraint fkej1q9wxsh4xrj5j23cwykmr75
    references account,
  module_id bigint
)
;

create table alert
(
  id bigserial not null
    constraint alert_pkey
    primary key,
  external_id uuid,
  account_id bigint
    constraint fk79itrv3n3895ix02y542iees
    references account
)
;

create table condition
(
  id bigserial not null
    constraint condition_pkey
    primary key,
  action_id bigint
    constraint fk8wjooul4c5e9r19i477atusrk
    references action,
  alert_id bigint
    constraint fkq4wv5pwvggbohcpnjypuo77jp
    references alert
)
;

create table condition_and_statement
(
  id bigserial not null
    constraint condition_and_statement_pkey
    primary key,
  condition_or_statement_id bigint
)
;

create table condition_or_statement
(
  id bigserial not null
    constraint condition_or_statement_pkey
    primary key,
  condition_id bigint
    constraint fkb4xmocdn4fmw5bushxf7rfd94
    references condition
)
;

alter table condition_and_statement
  add constraint fkavhlincc0pf7la4gh9d4yetvk
foreign key (condition_or_statement_id) references condition_or_statement
;

create table condition_statement
(
  id bigserial not null
    constraint condition_statement_pkey
    primary key,
  max_value double precision,
  min_value double precision,
  negated boolean,
  state varchar(255),
  value varchar(255),
  condition_and_statement_id bigint
    constraint fkfqlpejlyk41qy8opy9rhm8u0h
    references condition_and_statement,
  module_id bigint
)
;

create table device
(
  id bigserial not null
    constraint device_pkey
    primary key,
  external_id varchar(255),
  last_contact timestamp,
  name varchar(255),
  uptime bigint,
  version varchar(255)
)
;

alter table account_device_associations
  add constraint fktr525os5bw3vl46c1oi5audmm
foreign key (device_id) references device
;

create table module
(
  id bigserial not null
    constraint module_pkey
    primary key,
  external_id varchar(255),
  name varchar(255),
  state varchar(255),
  type varchar(255),
  value varchar(255),
  device_id bigint
    constraint fkielur4slqxefjd45n0y8faymg
    references device
)
;

alter table action
  add constraint fktsnq1ev6vem9hdn72dmapn81
foreign key (module_id) references module
;

alter table condition_statement
  add constraint fkb195u4gtnrtkorhgoti6alrsx
foreign key (module_id) references module
;

