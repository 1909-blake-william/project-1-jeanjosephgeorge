DROP USER expense CASCADE;

CREATE USER expense
IDENTIFIED BY p4ssw0rd
DEFAULT TABLESPACE users
TEMPORARY TABLESPACE temp
QUOTA 10M ON users;

GRANT CONNECT TO expense;
GRANT RESOURCE TO expense;
GRANT CREATE SESSION TO expense;
GRANT CREATE TABLE TO expense;
GRANT CREATE VIEW TO expense;

conn expense/p4ssw0rd;

-- PENDING, DENIED OR APPOVED
CREATE TABLE ERS_REIMBURSEMENT_STATUS(
    reimb_status_id number PRIMARY KEY,
    reimb_status varchar(10) NOT NULL
);

-- LODGING, TRAVEL, FOOD OR OTHER
CREATE TABLE ERS_REIMBURSEMENT_TYPE(
    reimb_type_id number PRIMARY KEY,
    reimb_type varchar2(10) NOT NULL
);

-- EMPLOYEE OR FINANCE MANAGER
CREATE TABLE ERS_USER_ROLES(
    ers_user_role_id number PRIMARY KEY,
    user_role varchar2(20) NOT NULL
);


CREATE SEQUENCE REIMB_ID_SEQ;
CREATE TABLE ERS_REIMBURSEMENT(
    reimb_id number PRIMARY KEY,
    reimb_amount number(7,2),
    reimb_submitted number, -- Time of submission
    reimb_resolved number, -- Date it was resolved
    reimb_description varchar2(250),
    reimb_receipt blob,
    reimb_author number,
    reimb_resolver number,
    reimb_status_id number,
    reimb_type_id number,
    CONSTRAINT ers_users_fk_auth FOREIGN KEY (reimb_author) REFERENCES ERS_USERS(ers_users_id),
    CONSTRAINT ers_users_fk_reslvr FOREIGN KEY (reimb_resolver) REFERENCES ERS_USERS(ers_users_id),
    CONSTRAINT ers_reimbursement_status_fk FOREIGN KEY (reimb_status_id) REFERENCES ERS_REIMBURSEMENT_STATUS(reimb_status_id),
    CONSTRAINT ers_reimbursement_type_fk FOREIGN KEY (reimb_type_id) REFERENCES ERS_REIMBURSEMENT_TYPE(reimb_type_id)
);

CREATE SEQUENCE ERS_USERS_ID_SEQ;
CREATE TABLE ERS_USERS(
    ers_users_id number PRIMARY KEY,
    ers_username varchar2(50) UNIQUE NOT NULL,
    ers_password varchar2(50),
    user_first_name varchar2(100),
    user_last_name varchar2(100),
    user_email varchar2(150),
    user_role_id number,
    CONSTRAINT user_roles_fk FOREIGN KEY (user_role_id) REFERENCES ERS_USER_ROLES(ers_user_role_id)
);


-- INSERTING VALUES INTO TABLES

INSERT INTO ERS_REIMBURSEMENT_STATUS (reimb_status_id, reimb_status) VALUES (1, 'Pending');
INSERT INTO ERS_REIMBURSEMENT_STATUS (reimb_status_id, reimb_status) VALUES (2, 'Denied');
INSERT INTO ERS_REIMBURSEMENT_STATUS (reimb_status_id, reimb_status) VALUES (3, 'Approved');

INSERT INTO ERS_REIMBURSEMENT_TYPE (reimb_type_id, reimb_type) VALUES (1, 'Lodging');
INSERT INTO ERS_REIMBURSEMENT_TYPE (reimb_type_id, reimb_type) VALUES (2, 'Travel');
INSERT INTO ERS_REIMBURSEMENT_TYPE (reimb_type_id, reimb_type) VALUES (3, 'Food');
INSERT INTO ERS_REIMBURSEMENT_TYPE (reimb_type_id, reimb_type) VALUES (4, 'Other');

INSERT INTO ERS_USER_ROLES (ers_user_role_id, user_role) VALUES (1, 'Employee');
INSERT INTO ERS_USER_ROLES (ers_user_role_id, user_role) VALUES (2, 'Finance Manager');

-- CREATING USERS

INSERT INTO ERS_USERS (ers_users_id, ers_username, ers_password, user_first_name, user_last_name, user_email, user_role_id)
VALUES (ERS_USERS_ID_SEQ.nextval, 'thejeanman','password','Jean','George','jean.joseph@outlook.com',2);

INSERT INTO ERS_USERS (ers_users_id, ers_username, ers_password, user_first_name, user_last_name, user_email, user_role_id)
VALUES (ERS_USERS_ID_SEQ.nextval, 'bubbles','password','Bubbles','Powerpuff','indestructiblebubbles@gmail.com',1);

INSERT INTO ERS_USERS (ers_users_id, ers_username, ers_password, user_first_name, user_last_name, user_email, user_role_id)
VALUES (ERS_USERS_ID_SEQ.nextval, 'buttercup','password','Buttercup','Powerpuff','cupofbutter@outlook.com',1);

INSERT INTO ERS_USERS (ers_users_id, ers_username, ers_password, user_first_name, user_last_name, user_email, user_role_id)
VALUES (ERS_USERS_ID_SEQ.nextval, 'blossom','password','Blossom','Powerpuff','cherryblossoms@gmail.com',1);

INSERT INTO ERS_USERS (ers_users_id, ers_username, ers_password, user_first_name, user_last_name, user_email, user_role_id)
VALUES (ERS_USERS_ID_SEQ.nextval, 'theprofessor','password','Professor','Utonium','imcrazy@git.com',2);

INSERT INTO ERS_USERS (ers_users_id, ers_username, ers_password, user_first_name, user_last_name, user_email, user_role_id)
VALUES (ERS_USERS_ID_SEQ.nextval, 'mojojojo','password','Mojo','Jojo','mojojojo@thegoodguy.com',1);

INSERT INTO ERS_USERS (ers_users_id, ers_username, ers_password, user_first_name, user_last_name, user_email, user_role_id)
VALUES (ERS_USERS_ID_SEQ.nextval, 'fuzzylumpkins','password','Fuzzy','Lumpkins','fuzzywuzzy@hey.com',1);

--

INSERT INTO ERS_REIMBURSEMENT (reimb_id, reimb_amount, reimb_submitted, reimb_resolved, reimb_description, reimb_receipt, reimb_author, reimb_resolver, reimb_status_id, reimb_type_id)
VALUES (reimb_id_seq.nextval, 450.50, 1573432698, 1573433023, 'Random stuff', NULL, 2, 1, 3, 4);

INSERT INTO ERS_REIMBURSEMENT (reimb_id, reimb_amount, reimb_submitted, reimb_resolved, reimb_description, reimb_receipt, reimb_author, reimb_resolver, reimb_status_id, reimb_type_id)
VALUES (reimb_id_seq.nextval, 500.00, 1573431698, 1573431023, 'Lodging', NULL, 3, 1, 1, 1);

INSERT INTO ERS_REIMBURSEMENT (reimb_id, reimb_amount, reimb_submitted, reimb_resolved, reimb_description, reimb_receipt, reimb_author, reimb_resolver, reimb_status_id, reimb_type_id)
VALUES (reimb_id_seq.nextval, 10500.75, 1573432698, 1573433023, 'Other stuff I cant speak about', NULL, 4, 1, 2, 4);

INSERT INTO ERS_REIMBURSEMENT (reimb_id, reimb_amount, reimb_submitted, reimb_resolved, reimb_description, reimb_receipt, reimb_author, reimb_resolver, reimb_status_id, reimb_type_id)
VALUES (reimb_id_seq.nextval, 1044.55, 1573432698, 1573433023, 'Travelling to the Caymans', NULL, 6, 5, 3, 2);

INSERT INTO ERS_REIMBURSEMENT (reimb_id, reimb_amount, reimb_submitted, reimb_resolved, reimb_description, reimb_receipt, reimb_author, reimb_resolver, reimb_status_id, reimb_type_id)
VALUES (reimb_id_seq.nextval, 4500.25, 1573432698, 1573433023, 'Ate a restaurant', NULL, 7, 5, 1, 3);


commit;