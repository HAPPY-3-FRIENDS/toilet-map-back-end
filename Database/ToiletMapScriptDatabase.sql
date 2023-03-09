CREATE DATABASE ToiletMap
GO;

USE ToiletMap
GO;

------------------------------ CREATE TABLE ------------------------------
CREATE TABLE [Account](
    Id INT IDENTITY(1, 1) NOT NULL,
    Username VARCHAR(20) NOT NULL,
    Status NVARCHAR(20) NULL,
    RoleId INT NOT NULL
)
GO;

CREATE TABLE [UserInfo](
   Id INT IDENTITY(1, 1) NOT NULL,
   AccountId INT NOT NULL,
   Gmail VARCHAR(22) NULL,
   Avatar VARCHAR(100) NULL,
   AccountBalance FLOAT NOT NULL,
   AccountTurn INT NOT NULL,
   DefaultPayment NVARCHAR(20) NOT NULL
)
GO;

CREATE TABLE [Role](
    Id INT IDENTITY(1, 1) NOT NULL,
    Name VARCHAR(10) NOT NULL
)
GO;

CREATE TABLE [Toilet](
    Id INT IDENTITY(1, 1) NOT NULL,
    Name NVARCHAR(50) NOT NULL,
    Address NVARCHAR(100) NOT NULL,
    District NVARCHAR(20) NOT NULL,
    Province NVARCHAR(20) NOT NULL,
    Status NVARCHAR(20) NULL,
    CompanyId INT NOT NULL
)
GO;

CREATE TABLE [Company](
    Id INT IDENTITY(1, 1) NOT NULL,
    Name NVARCHAR(50) NOT NULL,
    Address NVARCHAR(100) NOT NULL,
    District NVARCHAR(20) NOT NULL,
    Province NVARCHAR(20) NOT NULL,
    Phone VARCHAR(10) NULL
)
GO;

CREATE TABLE [Service](
    Id INT IDENTITY(1, 1) NOT NULL,
    Name NVARCHAR(50) NOT NULL,
    Price FLOAT NOT NULL
)
GO;

CREATE TABLE [ToiletService](
    Id INT IDENTITY(1, 1) NOT NULL,
    ServiceId INT NOT NULL,
    ToiletId INT NOT NULL
)
GO;

CREATE TABLE [CheckIn](
    Id INT IDENTITY(1, 1) NOT NULL,
    AccountId INT NOT NULL,
    ToiletServiceId INT NOT NULL,
    DateTime DATETIME NOT NULL,
    PaymentType NVARCHAR(20) NOT NULL,
    Balance FLOAT NULL,
    Turn INT NULL
)
GO;

------------------------------ CREATE CONSTRAINT ------------------------------
--- PRIMARY KEY ---
ALTER TABLE [Account] ADD CONSTRAINT PK_Account PRIMARY KEY (Id);
ALTER TABLE [UserInfo] ADD CONSTRAINT PK_UserInfo PRIMARY KEY (Id);
ALTER TABLE [Role] ADD CONSTRAINT PK_Role PRIMARY KEY (Id);
ALTER TABLE [Toilet] ADD CONSTRAINT PK_Toilet PRIMARY KEY (Id);
ALTER TABLE [Company] ADD CONSTRAINT PK_Company PRIMARY KEY (Id);
ALTER TABLE [Service] ADD CONSTRAINT PK_Service PRIMARY KEY (Id);
ALTER TABLE [ToiletService] ADD CONSTRAINT PK_ToiletService PRIMARY KEY (Id);
ALTER TABLE [CheckIn] ADD CONSTRAINT PK_CheckIn PRIMARY KEY (Id);

--- FOREIGN KEY ---
ALTER TABLE [Account] ADD CONSTRAINT FK_Account_Role
    FOREIGN KEY (RoleId) REFERENCES Role(Id);

ALTER TABLE [UserInfo] ADD CONSTRAINT FK_UserInfo_Account
    FOREIGN KEY (AccountId) REFERENCES Account(Id);

ALTER TABLE [Toilet] ADD CONSTRAINT FK_Toilet_Company
    FOREIGN KEY (CompanyId) REFERENCES Company(Id);

ALTER TABLE [ToiletService] ADD CONSTRAINT FK_ToiletService_Toilet
    FOREIGN KEY (ToiletId) REFERENCES Toilet(Id);

ALTER TABLE [ToiletService] ADD CONSTRAINT FK_ToiletService_Service
    FOREIGN KEY (ServiceId) REFERENCES Service(Id);

ALTER TABLE [CheckIn] ADD CONSTRAINT FK_CheckIn_Account
    FOREIGN KEY (AccountId) REFERENCES Account(Id);

ALTER TABLE [CheckIn] ADD CONSTRAINT FK_CheckIn_ToiletService
    FOREIGN KEY (ToiletServiceId) REFERENCES ToiletService(Id);