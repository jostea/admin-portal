drop table if exists task_stream_table;
drop table if exists answer_option_table;
drop table if exists stream_table;
drop table if exists task_table;
drop table if exists user_table;
drop table if exists discipline_table;

/* create table t_user */
CREATE TABLE user_table
(
    id        SERIAL PRIMARY KEY,
    username  varchar(50)  NOT NULL,
    email     varchar(255) NOT NULL,
    password  varchar(255) NOT NULL,
    is_active boolean      NOT NULL DEFAULT true,
    role      varchar(50)  NOT NULL,
    UNIQUE (username),
    UNIQUE (email)
);

/* create table discipline */
CREATE TABLE discipline_table
(
    id   SERIAL PRIMARY KEY,
    name varchar(50) NOT NULL,
    UNIQUE (name)
);

/* create table stream */
CREATE TABLE stream_table
(
    id            SERIAL PRIMARY KEY,
    name          varchar(50) NOT NULL,
    discipline_id int         NOT NULL REFERENCES discipline_table (id),
    UNIQUE (name,discipline_id)
);


/* create table task */
CREATE TABLE task_table
(
    id     SERIAL PRIMARY KEY,
    title       varchar(255) NOT NULL,
    description text         NOT NULL,
    task_type   varchar(255) NOT NULL,
    complexity  varchar(255) NOT NULL,
    is_enabled   boolean      NOT NULL DEFAULT true
);

/* create many-to-many table task_stream */
CREATE TABLE task_stream_table
(
    task_id   int NOT NULL REFERENCES task_table (id),
    stream_id int NOT NULL REFERENCES stream_table (id)
);

/* create table answer_option */
CREATE TABLE answer_option_table
(
    id                  SERIAL PRIMARY KEY,
    task_id             int     NOT NULL REFERENCES task_table (id),
    answer_option_value text    NOT NULL,
    is_correct          boolean NOT NULL
);

/*-----------------------------------*/
/* scripts for initial DB population */
/*-----------------------------------*/

/* populate discipline */
INSERT INTO discipline_table(name)
VALUES ('APPLICATIONS_MANAGEMENT');
INSERT INTO discipline_table(name)
VALUES ('DEVELOPMENT');
INSERT INTO discipline_table(name)
VALUES ('TESTING');

/* populate stream */
INSERT INTO stream_table(name, discipline_id)
VALUES ('JAVA', (SELECT d.id FROM discipline_table d WHERE d.name = 'APPLICATIONS_MANAGEMENT'));
INSERT INTO stream_table(name, discipline_id)
VALUES ('DEVOPS', (SELECT d.id FROM discipline_table d WHERE d.name = 'APPLICATIONS_MANAGEMENT'));
INSERT INTO stream_table(name, discipline_id)
VALUES ('ANALYSTS', (SELECT d.id FROM discipline_table d WHERE d.name = 'APPLICATIONS_MANAGEMENT'));
INSERT INTO stream_table(name, discipline_id)
VALUES ('JAVA', (SELECT d.id FROM discipline_table d WHERE d.name = 'DEVELOPMENT'));
INSERT INTO stream_table(name, discipline_id)
VALUES ('DOT_NET', (SELECT d.id FROM discipline_table d WHERE d.name = 'DEVELOPMENT'));
INSERT INTO stream_table(name, discipline_id)
VALUES ('WEB', (SELECT d.id FROM discipline_table d WHERE d.name = 'DEVELOPMENT'));
INSERT INTO stream_table(name, discipline_id)
VALUES ('MOBILE', (SELECT d.id FROM discipline_table d WHERE d.name = 'DEVELOPMENT'));
INSERT INTO stream_table(name, discipline_id)
VALUES ('AUTOMATION_TESTING', (SELECT d.id FROM discipline_table d WHERE d.name = 'TESTING'));
INSERT INTO stream_table(name, discipline_id)
VALUES ('MANUAL_TESTING', (SELECT d.id FROM discipline_table d WHERE d.name = 'TESTING'));


/* Insert the super admin */
-- insert into user_table (username, email, password, role) values('endavamainadmin', 'mainadmin@mail.com', '$2a$10$0WJ4XuVBhhXk6QvUujVsP.6JGwdZf4/jbBssgmLGRjW08w.4jxetK', 'SUPER_ADMIN')

