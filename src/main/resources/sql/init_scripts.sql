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
    UNIQUE (name, discipline_id)
);


/* create table task */
CREATE TABLE task_table
(
    id          SERIAL PRIMARY KEY,
    title       varchar(255) NOT NULL,
    description text         NOT NULL,
    task_type   varchar(255) NOT NULL,
    complexity  varchar(255) NOT NULL,
    is_enabled  boolean      NOT NULL DEFAULT true
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



/*--------------------------------------------------*/
/* scripts for SQL_task and CODE_task functionality */
/*--------------------------------------------------*/

drop table if exists sql_stream_table;
drop table if exists sql_task_table;
drop table if exists sql_group_table;

drop table if exists code_stream_table;
drop table if exists correct_code_table;
drop table if exists code_task_table;


/* --- SQL task tables --- */

CREATE TABLE sql_group_table
(
    id     SERIAL PRIMARY KEY,
    name       varchar(255) NOT NULL,
    image_path text         NOT NULL,
    UNIQUE(name)
);

CREATE TABLE sql_task_table
(
    id     SERIAL PRIMARY KEY,
    title       varchar(255) NOT NULL,
    description text         NOT NULL,
    complexity  varchar(255) NOT NULL,
    is_enabled   boolean      NOT NULL DEFAULT true,
    correct_statement text NOT NULL,
    sql_group_id int NOT NULL REFERENCES sql_group_table (id)
);

CREATE TABLE sql_stream_table
(
    sql_task_id   int NOT NULL REFERENCES sql_task_table (id),
    stream_id int NOT NULL REFERENCES stream_table (id)
);



/* --- Code task tables --- */

CREATE TABLE code_task_table
(
    id     SERIAL PRIMARY KEY,
    title       varchar(255) NOT NULL,
    description text         NOT NULL,
    complexity  varchar(255) NOT NULL,
    technology  varchar(255) NOT NULL,
    is_enabled   boolean      NOT NULL DEFAULT true,
    signature text NOT NULL
);

CREATE TABLE correct_code_table
(
    id     SERIAL PRIMARY KEY,
    code_task_id int NOT NULL REFERENCES code_task_table (id),
    input text NOT NULL,
    output text NOT NULL
);

CREATE TABLE code_stream_table
(
    code_task_id   int NOT NULL REFERENCES code_task_table (id),
    stream_id int NOT NULL REFERENCES stream_table (id)
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

/* Populate sql_group_table with some test data */
INSERT INTO sql_group_table(name,image_path) VALUES ('Sql Group #1', 'Path 1');
INSERT INTO sql_group_table(name,image_path) VALUES ('Sql Group #2', 'Path 2');
INSERT INTO sql_group_table(name,image_path) VALUES ('Sql Group #3', 'Path 3');


/* Insert the super admin */
-- insert into user_table (username, email, password, role) values('endavamainadmin', 'mainadmin@mail.com', '$2a$10$0WJ4XuVBhhXk6QvUujVsP.6JGwdZf4/jbBssgmLGRjW08w.4jxetK', 'SUPER_ADMIN')

