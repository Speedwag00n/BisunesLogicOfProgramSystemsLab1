CREATE TYPE HOTEL_TYPE AS ENUM ('APARTMENT', 'HOTEL', 'GUEST_HOUSES', 'HOSTEL', 'BED_AND_BREAKFASTS', 'HOMESTAYS', 'CAPSULE_HOUSES', 'HOLIDAY_HOMES', 'LOVE_HOTELS', 'VILLAS', 'CAMPSITES', 'MOTELS', 'BOATS', 'COUNTRY_HOUSES', 'CHALETS', 'ECONOMY_HOTELS');

CREATE TYPE FOOD AS ENUM ('SELF_CATERING', 'BREAKFAST', 'THREE_MEALS', 'ALL_INCLUSIVE', 'BREAKFAST_AND_DINNER');

CREATE TABLE COUNTRY
(
    ID   SERIAL        PRIMARY KEY,
    NAME VARCHAR(256)  UNIQUE NOT NULL
);

CREATE TABLE CITY
(
    ID      SERIAL       PRIMARY KEY,
    COUNTRY INTEGER      REFERENCES COUNTRY (ID) ON UPDATE CASCADE ON DELETE CASCADE NOT NULL,
    NAME    VARCHAR(256) NOT NULL
);

CREATE TABLE HOTEL
(
    ID          SERIAL       PRIMARY KEY,
    CITY        INTEGER      REFERENCES CITY (ID) ON UPDATE CASCADE ON DELETE CASCADE NOT NULL,
    NAME        VARCHAR(256) NOT NULL,
    ADDRESS     VARCHAR(256) NOT NULL,
	HOTEL_TYPE  HOTEL_TYPE   NOT NULL,
	STARS       INTEGER      DEFAULT 0 NOT NULL,
    DESCRIPTION TEXT,
    OWNER       VARCHAR(256) NOT NULL
);

CREATE TABLE ROOMS
(
    ID            SERIAL       PRIMARY KEY,
    HOTEL         INTEGER      REFERENCES HOTEL (ID) ON UPDATE CASCADE ON DELETE CASCADE NOT NULL,
    NAME          VARCHAR(128) NOT NULL,
	ROOMS_NUMBER  INTEGER      CHECK (ROOMS_NUMBER > 0) NOT NULL
);

CREATE TABLE CONFIGURATION
(
    ID            SERIAL       PRIMARY KEY,
    ROOMS         INTEGER      REFERENCES ROOMS (ID) ON UPDATE CASCADE ON DELETE CASCADE NOT NULL,
    CAPACITY      INTEGER      CHECK (CAPACITY > 0) NOT NULL,
	FOOD          FOOD,
	PRICE         INTEGER      CHECK (PRICE > 0) NOT NULL
);

CREATE TABLE RESERVATION
(
    ID             SERIAL       PRIMARY KEY,
	NAME           VARCHAR(256) NOT NULL,
	SURNAME        VARCHAR(256) NOT NULL,
	EMAIL          VARCHAR(256) NOT NULL,
	ARRIVAL_DATE   DATE         NOT NULL,
	DEPARTURE_DATE DATE         NOT NULL,
	CONFIGURATION  INTEGER      REFERENCES CONFIGURATION (ID) ON UPDATE CASCADE ON DELETE CASCADE NOT NULL
);

CREATE TABLE USERS
(
    ID             SERIAL       PRIMARY KEY,
    LOGIN          VARCHAR(256) NOT NULL,
    PASSWORD       VARCHAR(256) NOT NULL,
    LAST_LOGOUT    TIMESTAMP    NOT NULL,
    ROLE           VARCHAR(256)
);

CREATE TABLE CONVENIENCE
(
    ID             SERIAL       PRIMARY KEY,
    NAME           VARCHAR(256) NOT NULL
);

CREATE TABLE CONFIGURATION_CONVENIENCE
(
    CONFIGURATION  INTEGER      REFERENCES CONFIGURATION (ID) ON UPDATE CASCADE ON DELETE CASCADE NOT NULL,
    CONVENIENCE    INTEGER      REFERENCES CONVENIENCE (ID) ON UPDATE CASCADE ON DELETE CASCADE NOT NULL,
    PRIMARY KEY (CONFIGURATION, CONVENIENCE)
);

CREATE INDEX HOTEL_INDEX_CITY ON HOTEL USING HASH(CITY);
CREATE INDEX ROOMS_INDEX_HOTEL ON ROOMS USING HASH(HOTEL);
CREATE INDEX CONFIGURATION_INDEX_ROOMS ON CONFIGURATION USING HASH(ROOMS);
CREATE INDEX RESERVATION_INDEX_CONFIGURATION ON RESERVATION USING HASH(CONFIGURATION);