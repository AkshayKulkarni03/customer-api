DROP TABLE IF EXISTS CUSTOMER;
DROP TABLE IF EXISTS ADDRESS;

CREATE TABLE CUSTOMER(
    ID VARCHAR(255) PRIMARY KEY,
    AGE INTEGER,
    FIRST_NAME VARCHAR(255),
    LAST_NAME VARCHAR(255)
);

CREATE TABLE ADDRESS(
    CITY VARCHAR(255),
    HOUSE_NUMBER VARCHAR(255),
    STREET VARCHAR(255),
    ZIP_CODE VARCHAR(255),
    CUSTOMER_ID VARCHAR(255) PRIMARY KEY
);