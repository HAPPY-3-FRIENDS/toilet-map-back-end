CREATE DATABASE ToiletMap
GO

-- DROP DATABASE ToiletMap
-- GO

USE ToiletMap
GO

------------------------------ CREATE TABLE ------------------------------
CREATE TABLE [Account](
    Id INT IDENTITY(1, 1) NOT NULL,
    Username VARCHAR(20) NOT NULL,
    Password VARCHAR(20) NULL,
    Status NVARCHAR(20) NULL,
    RoleId INT NOT NULL
)
GO

CREATE TABLE [UserInfo](
   Id INT IDENTITY(1, 1) NOT NULL,
   AccountId INT NOT NULL,
   FullName NVARCHAR(100) NOT NULL,
   Phone VARCHAR(10) NOT NULL,
   Gmail VARCHAR(22) NULL,
   Avatar VARCHAR(100) NULL,
   AccountBalance FLOAT NOT NULL,
   AccountTurn INT NOT NULL,
   DefaultPayment NVARCHAR(20) NOT NULL
)
GO

CREATE TABLE [Role](
    Id INT IDENTITY(1, 1) NOT NULL,
    Name VARCHAR(10) NOT NULL
)
GO

CREATE TABLE [Toilet](
    Id INT IDENTITY(1, 1) NOT NULL,
    Name NVARCHAR(50) NOT NULL,
    Address NVARCHAR(100) NOT NULL,
    District NVARCHAR(50) NOT NULL,
    Province NVARCHAR(50) NOT NULL,
    Status NVARCHAR(20) NULL,
    CompanyId INT NOT NULL
)
GO

CREATE TABLE [Company](
    Id INT IDENTITY(1, 1) NOT NULL,
    Name NVARCHAR(50) NOT NULL,
    Address NVARCHAR(100) NOT NULL,
    District NVARCHAR(50) NOT NULL,
    Province NVARCHAR(50) NOT NULL,
    Phone VARCHAR(20) NULL
)
GO

CREATE TABLE [Service](
    Id INT IDENTITY(1, 1) NOT NULL,
    Name NVARCHAR(50) NOT NULL,
    Price FLOAT NOT NULL
)
GO

CREATE TABLE [ToiletService](
    Id INT IDENTITY(1, 1) NOT NULL,
    ServiceId INT NOT NULL,
    ToiletId INT NOT NULL
)
GO

CREATE TABLE [CheckIn](
    Id INT IDENTITY(1, 1) NOT NULL,
    AccountId INT NOT NULL,
    ToiletServiceId INT NOT NULL,
    DateTime DATETIME NOT NULL,
    PaymentType NVARCHAR(20) NOT NULL,
    Balance FLOAT NULL,
    Turn INT NULL
)
GO

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

------------------------------ INSERT VALUE ------------------------------
INSERT INTO Role (Name) VALUES (N'Admin')
GO

INSERT INTO Role (Name) VALUES (N'Manager')
GO

INSERT INTO Role (Name) VALUES (N'Staff')
GO

INSERT INTO Role (Name) VALUES (N'User')
GO

INSERT INTO Account (Username, Password, Status, RoleId) VALUES (N'0849666957', null, null, 4)
GO

INSERT INTO Account (Username, Password, Status, RoleId) VALUES (N'dvciq1', N'123', null, 2)
GO

INSERT INTO Account (Username, Password, Status, RoleId) VALUES (N'nvsld', N'123', null, 3)
GO

INSERT INTO UserInfo (AccountId, FullName, Phone, Gmail, Avatar, AccountBalance, AccountTurn, DefaultPayment)
VALUES (1, N'Huỳnh Lê Thủy Tiên', '0849666957', null, null, 20000, 20, N'BALANCE')
GO

INSERT INTO Company (Name, Address, District, Province, Phone)
VALUES (N'Công ty dịch vụ công ích quận 1', N'28-30 Nguyễn Thái Bình, P. Nguyễn Thái Bình', N'Quận 1', N'Thành phố Hồ Chí Minh', N'(028) 38.215.611')
GO

INSERT INTO Toilet (Name, Address, District, Province, Status, CompanyId)
VALUES (N'Nhà vệ sinh lưu động', N'44 Trần Đình Xu, phường Cô Giang', N'Quận 1', N'Thành phố Hồ Chí Minh', null, 1)
GO

INSERT INTO Service (Name, Price) VALUES (N'Đi vệ sinh (tiểu tiện)', 2000)
GO

INSERT INTO Service (Name, Price) VALUES (N'Đi vệ sinh (đại tiện)', 4000)
GO

INSERT INTO Service (Name, Price) VALUES (N'Đi tắm', 8000)
GO

INSERT INTO ToiletService (ServiceId, ToiletId) VALUES (1, 1)
GO

INSERT INTO ToiletService (ServiceId, ToiletId) VALUES (2, 1)
GO

INSERT INTO ToiletService (ServiceId, ToiletId) VALUES (3, 1)
GO

INSERT INTO CheckIn (AccountId, ToiletServiceId, DateTime, PaymentType, Balance, Turn)
VALUES (1, 2, N'2023-03-09 15:22:52.000', N'BALANCE', 2000, null)
GO

INSERT INTO CheckIn (AccountId, ToiletServiceId, DateTime, PaymentType, Balance, Turn)
VALUES (1, 2, N'2023-03-10 09:44:29.000', N'TURN', null, 4)
GO