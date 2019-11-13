/* create table t_user */
CREATE TABLE t_user
(
    user_id  SERIAL PRIMARY KEY,
    userName varchar(50)  NOT NULL,
    eMail    varchar(255) NOT NULL,
    pass     varchar(255) NOT NULL,
    isActive boolean      NOT NULL DEFAULT true,
    role     varchar(50)  NOT NULL
);

/* create table discipline */
CREATE TABLE discipline
(
    discipline_id SERIAL PRIMARY KEY,
    disc_name     varchar(50) NOT NULL,
    UNIQUE (disc_name)
);

/* create table stream */
CREATE TABLE stream
(
    stream_id  SERIAL PRIMARY KEY,
    streamName varchar(50) NOT NULL,
    discip_id  int         NOT NULL REFERENCES discipline (discipline_id)
);


/* create table task */
CREATE TABLE task
(
    task_id     SERIAL PRIMARY KEY,
    title       varchar(255) NOT NULL,
    description text         NOT NULL,
    task_type   varchar(255) NOT NULL,
    complexity  varchar(255) NOT NULL,
    isEnabled   boolean      NOT NULL DEFAULT true
);

/* create many-to-many table task_stream */
CREATE TABLE task_stream
(
    task_id   int NOT NULL REFERENCES task (task_id),
    stream_id int NOT NULL REFERENCES stream (stream_id)
);

/* create table answer_option */
CREATE TABLE answer_option
(
    ao_id               SERIAL PRIMARY KEY,
    task_id             int     NOT NULL REFERENCES task (task_id),
    answer_option_value text    NOT NULL,
    isCorrect           boolean NOT NULL
);

/*-----------------------------------*/
/* scripts for initial DB population */
/*-----------------------------------*/

/* populate discipline */
INSERT INTO discipline(disc_name) VALUES ('APPLICATIONS_MANAGEMENT');
INSERT INTO discipline(disc_name) VALUES ('DEVELOPMENT');
INSERT INTO discipline(disc_name) VALUES ('TESTING');

/* populate stream */
INSERT INTO stream(streamname, discip_id)
VALUES ('JAVA', (SELECT d.discipline_id FROM discipline d WHERE d.disc_name = 'APPLICATIONS_MANAGEMENT'));
INSERT INTO stream(streamname, discip_id)
VALUES ('DEVOPS', (SELECT d.discipline_id FROM discipline d WHERE d.disc_name = 'APPLICATIONS_MANAGEMENT'));
INSERT INTO stream(streamname, discip_id)
VALUES ('ANALYSTS', (SELECT d.discipline_id FROM discipline d WHERE d.disc_name = 'APPLICATIONS_MANAGEMENT'));
INSERT INTO stream(streamname, discip_id)
VALUES ('JAVA', (SELECT d.discipline_id FROM discipline d WHERE d.disc_name = 'DEVELOPMENT'));
INSERT INTO stream(streamname, discip_id)
VALUES ('DOT_NET', (SELECT d.discipline_id FROM discipline d WHERE d.disc_name = 'DEVELOPMENT'));
INSERT INTO stream(streamname, discip_id)
VALUES ('WEB', (SELECT d.discipline_id FROM discipline d WHERE d.disc_name = 'DEVELOPMENT'));
INSERT INTO stream(streamname, discip_id)
VALUES ('MOBILE', (SELECT d.discipline_id FROM discipline d WHERE d.disc_name = 'DEVELOPMENT'));
INSERT INTO stream(streamname, discip_id)
VALUES ('AUTOMATION_TESTING', (SELECT d.discipline_id FROM discipline d WHERE d.disc_name = 'TESTING'));
INSERT INTO stream(streamname, discip_id)
VALUES ('MANUAL_TESTING', (SELECT d.discipline_id FROM discipline d WHERE d.disc_name = 'TESTING'));