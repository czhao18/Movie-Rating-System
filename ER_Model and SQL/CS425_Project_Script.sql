CREATE TABLE MOVIE(
    title VARCHAR(256),
    releaseYear int,
    lasting VARCHAR(256) not null,
    pgRate VARCHAR(10),
    PRIMARY KEY(title, releaseYear),
    CHECK(releaseYear>0));
    
CREATE TABLE GENRE(
    title VARCHAR(256),
    releaseYear int,
    genre VARCHAR(256),
    PRIMARY KEY(title,releaseYear,genre),
    FOREIGN KEY(title,releaseYear)REFERENCES MOVIE(title,releaseYear));
    
CREATE TABLE PEOPLE(
    pID CHAR(6) not null,
    pName VARCHAR(256) not null,
    birthDate DATE,
    gender CHAR(1),
    pJob VARCHAR(256),
    PRIMARY KEY(pID,pJob),
    CHECK(pJob IN('actor','director','writer') AND gender IN('F','M')));
    
CREATE TABLE USERS(
    userID CHAR(6) not null,
    uName VARCHAR(256) not null,
    userClass VARCHAR(6),
    PRIMARY KEY(userID,userClass),
    CHECK(userClass IN('staff', 'member')));
    
CREATE TABLE ROOM(
    rNumber int CHECK(rNumber>0) PRIMARY KEY,
    capacity int CHECK(capacity>0));
    
CREATE TABLE CREATOR(
    pID CHAR(6) not null,
    pJob VARCHAR(256),
    title VARCHAR(256),
    releaseYear int,
    PRIMARY KEY(pID,pJob,title,releaseYear),
    FOREIGN KEY(pID,pJob)REFERENCES PEOPLE(pID,pJob),
    FOREIGN KEY(title,releaseYear)REFERENCES MOVIE(title,releaseYear),
    CHECK(pJob IN('director','writer'))); 
/*
We combined 'write' and 'direct' these two relationships together as CREATOR
schema, cause the only difference between two relationshipis is that these two 
kinds of people have seperate roles in movie creation.
This is different from our original ER model.
*/
    
CREATE TABLE ACT(
    pID CHAR(6) not null,
    pJob VARCHAR(256),
    title VARCHAR(256),
    releaseYear int,
    movieRole VARCHAR(256),
    CHECK(pJob IN('actor')),
    PRIMARY KEY(pID,title,releaseYear,movieRole),
    FOREIGN KEY(pID,pJob)REFERENCES PEOPLE(pID,pJob),
    FOREIGN KEY(title,releaseYear)REFERENCES MOVIE(title,releaseYear));
/*
This is 'Act' relationship
*/
    
CREATE TABLE RATEPEOPLE(
    pID CHAR(6) not null,
    pJob VARCHAR(256),
    userID CHAR(6) not null,
    userClass VARCHAR(6),
    rate int CHECK(rate>=0 and rate<=10),
    PRIMARY KEY(pID,userID),
    FOREIGN KEY(pID,pJob)REFERENCES PEOPLE(pID,pJob),
    FOREIGN KEY(userID,userClass)REFERENCES USERS(userID,userClass));
    
CREATE TABLE RATEMOVIE(
    title VARCHAR(256),
    releaseYear int,
    userID CHAR(6) not null,
    userClass VARCHAR(6),
    rate int CHECK(rate>=0 and rate<=10),
    PRIMARY KEY(title, releaseYear,userID),
    FOREIGN KEY(title,releaseYear)REFERENCES MOVIE(title,releaseYear),
    FOREIGN KEY(userID,userClass)REFERENCES USERS(userID,userClass));
    
CREATE TABLE SCHEDULE(
    title VARCHAR(256),
    releaseYear int,
    rNumber int REFERENCES ROOM,
    movieTime TIMESTAMP,
    scheduleID CHAR(6) PRIMARY KEY,
    FOREIGN KEY(title, releaseYear) REFERENCES MOVIE);
  
/*
We added an additional attribute in SCHEDULE schema which is scheduleID.
*/
      
CREATE TABLE TICKET(
    tID CHAR(6) PRIMARY KEY,
    scheduleID CHAR(6) REFERENCES SCHEDULE);
    
/*
Each ticket is connected to a scheduleID, each scheduleID contains all the 
movie's informations, and room information; 
Each tuple in TICKET schema only has tID and scheduleID.
*/
    
CREATE TABLE BOOK(
    userID CHAR(6) not null,
    userClass VARCHAR(6),
    tID CHAR(6),
    payMethod VARCHAR(256) CHECK(payMethod IN ('credit','debit')),
    PRIMARY KEY(userID,tID),
    FOREIGN KEY(userID,userClass)REFERENCES USERS(userID,userClass),
    FOREIGN KEY(tID)REFERENCES TICKET(tID));
    