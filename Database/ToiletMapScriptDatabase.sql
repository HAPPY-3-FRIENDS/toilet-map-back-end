CREATE DATABASE ToiletMap
GO

USE ToiletMap
GO

------------------------------ CREATE TABLE ------------------------------
CREATE TABLE [Company]
(
    Id       INT IDENTITY (1, 1) NOT NULL,
    Name     NVARCHAR(50)        NOT NULL,
    Logo     TEXT                NULL,
    Address  NVARCHAR(100)       NOT NULL,
    Ward     NVARCHAR(50)        NOT NULL,
    District NVARCHAR(50)        NOT NULL,
    Province NVARCHAR(50)        NOT NULL,
    Phone    VARCHAR(20)         NULL
)
GO

CREATE TABLE [Role]
(
    Id   INT IDENTITY (1, 1) NOT NULL,
    Name VARCHAR(10)         NOT NULL
)
GO

CREATE TABLE [Account]
(
    Id        INT IDENTITY (1, 1) NOT NULL,
    Username  VARCHAR(20)         NOT NULL,
    Password  CHAR(60)            NULL,
    Status    NVARCHAR(20)        NOT NULL,
    RoleId    INT                 NOT NULL,
    CompanyId INT                 NULL
)
GO

CREATE TABLE [UserInfo]
(
    AccountId      INT           NOT NULL,
    FullName       NVARCHAR(100) NOT NULL,
    Gmail          VARCHAR(255)  NULL,
    Avatar         VARCHAR(MAX)  NULL,
    AccountBalance INT           NOT NULL,
    AccountTurn    INT           NOT NULL,
    DefaultPayment NVARCHAR(20)  NOT NULL
)
GO

CREATE TABLE [Toilet]
(
    Id        INT                 NOT NULL,
    Name      NVARCHAR(50)        NOT NULL,
    Address   NVARCHAR(100)       NOT NULL,
    Ward      NVARCHAR(50)        NOT NULL,
    District  NVARCHAR(50)        NOT NULL,
    Province  NVARCHAR(50)        NOT NULL,
    Latitude  FLOAT               NOT NULL,
    Longitude FLOAT               NOT NULL,
    NearBy    NVARCHAR(200)       NULL,
    isFree    BIT                 NOT NULL,
    OpenTime  TIME                NOT NULL,
    CloseTime TIME                NOT NULL,
    CompanyId INT                 NOT NULL,
    Status    NVARCHAR(20)        NOT NULL
)
GO

CREATE TABLE [Facility]
(
    Id   INT IDENTITY (1, 1) NOT NULL,
    Name NVARCHAR(200)       NOT NULL,
    Type NVARCHAR(20)        NOT NULL
)
GO

CREATE TABLE [ToiletFacility]
(
    Id          INT IDENTITY (1, 1) NOT NULL,
    ToiletId    INT                 NOT NULL,
    FacilityId  INT                 NOT NULL,
    Quantity    INT                 NOT NULL,
    Description NTEXT               NULL
)
GO

CREATE TABLE [Service]
(
    Id    INT IDENTITY (1, 1) NOT NULL,
    Name  NVARCHAR(50)        NOT NULL,
    Price INT                 NOT NULL,
    Turn  INT                 NOT NULL
)
GO

CREATE TABLE [ToiletService]
(
    Id        INT IDENTITY (1, 1) NOT NULL,
    ToiletId  INT                 NOT NULL,
    ServiceId INT                 NOT NULL
)
GO

CREATE TABLE [ToiletImage]
(
    Id          INT IDENTITY (1, 1) NOT NULL,
    ToiletId    INT                 NOT NULL,
    ImageSource TEXT                NOT NULL
)
GO

CREATE TABLE [CheckIn]
(
    Id              INT IDENTITY (1, 1) NOT NULL,
    AccountId       INT                 NOT NULL,
    ToiletServiceId INT                 NOT NULL,
    DateTime        DATETIME            NOT NULL,
    PaymentMethod   NVARCHAR(20)        NOT NULL,
    Balance         INT                 NULL,
    Turn            INT                 NULL
)
GO

CREATE TABLE [Combo]
(
    Id        INT IDENTITY (1, 1) NOT NULL,
    TotalTurn INT                 NOT NULL,
    Price     INT                 NOT NULL
)
GO

CREATE TABLE [Order]
(
    Id            INT IDENTITY (1, 1) NOT NULL,
    AccountId     INT                 NOT NULL,
    TotalTurn     INT                 NOT NULL,
    TotalPrice    INT                 NOT NULL,
    PaymentMethod NVARCHAR(20)        NOT NULL,
    DateTime      DATETIME            NOT NULL
)
GO

CREATE TABLE [Transaction]
(
    Id INT IDENTITY (1, 1) NOT NULL
)
GO

CREATE TABLE [Payment]
(
    Id          INT IDENTITY (1, 1) NOT NULL,
    AccountId   INT                 NOT NULL,
    Total       INT                 NOT NULL,
    Method      NVARCHAR(100)       NOT NULL,
    CreatedDate DATETIME            NOT NULL,
    Status      NVARCHAR(20)        NULL
)
GO

CREATE TABLE [Rating]
(
    Id        INT IDENTITY (1, 1) NOT NULL,
    Star      INT                 NOT NULL,
    Comment   NTEXT               NOT NULL,
    AccountId INT                 NOT NULL,
    ToiletId  INT                 NOT NULL,
    DateTime  DATETIME            NOT NULL,
    Status    NVARCHAR(20)        NULL
)
GO

CREATE TABLE [RatingImage]
(
    Id          INT IDENTITY (1, 1) NOT NULL,
    RatingId    INT                 NOT NULL,
    ImageSource TEXT                NOT NULL
)
GO

CREATE TABLE [SensitiveWord]
(
    Id   INT IDENTITY (1, 1) NOT NULL,
    Word NVARCHAR(50)        NOT NULL
)
GO

CREATE TABLE [Configuration]
(
    Id INT IDENTITY (1, 1) NOT NULL
)
GO

------------------------------ CREATE CONSTRAINT ------------------------------
--- PRIMARY KEY ---
ALTER TABLE [Company]
    ADD CONSTRAINT PK_Company PRIMARY KEY (Id);
ALTER TABLE [Role]
    ADD CONSTRAINT PK_Role PRIMARY KEY (Id);
ALTER TABLE [Account]
    ADD CONSTRAINT PK_Account PRIMARY KEY (Id);
ALTER TABLE [UserInfo]
    ADD CONSTRAINT PK_UserInfo PRIMARY KEY (AccountId);
ALTER TABLE [Toilet]
    ADD CONSTRAINT PK_Toilet PRIMARY KEY (Id);
ALTER TABLE [Facility]
    ADD CONSTRAINT PK_Facility PRIMARY KEY (Id);
ALTER TABLE [ToiletFacility]
    ADD CONSTRAINT PK_ToiletFacility PRIMARY KEY (Id);
ALTER TABLE [Service]
    ADD CONSTRAINT PK_Service PRIMARY KEY (Id);
ALTER TABLE [ToiletService]
    ADD CONSTRAINT PK_ToiletService PRIMARY KEY (Id);
ALTER TABLE [ToiletImage]
    ADD CONSTRAINT PK_ToiletImage PRIMARY KEY (Id);
ALTER TABLE [CheckIn]
    ADD CONSTRAINT PK_CheckIn PRIMARY KEY (Id);
ALTER TABLE [Combo]
    ADD CONSTRAINT PK_Combo PRIMARY KEY (Id);
ALTER TABLE [Order]
    ADD CONSTRAINT PK_Order PRIMARY KEY (Id);
ALTER TABLE [Payment]
    ADD CONSTRAINT PK_Payment PRIMARY KEY (Id);
ALTER TABLE [Transaction]
    ADD CONSTRAINT PK_Transaction PRIMARY KEY (Id);
ALTER TABLE [Rating]
    ADD CONSTRAINT PK_Rating PRIMARY KEY (Id);
ALTER TABLE [RatingImage]
    ADD CONSTRAINT PK_RatingImage PRIMARY KEY (Id);
ALTER TABLE [SensitiveWord]
    ADD CONSTRAINT PK_SensitiveWord PRIMARY KEY (Id);
ALTER TABLE [Configuration]
    ADD CONSTRAINT PK_Configuration PRIMARY KEY (Id);

--- UNIQUE ---
ALTER TABLE [Account]
    ADD CONSTRAINT UNIQUE_Username UNIQUE (Username);

--- FOREIGN KEY ---
ALTER TABLE [Account]
    ADD CONSTRAINT FK_Account_Role
        FOREIGN KEY (RoleId) REFERENCES Role (Id);

ALTER TABLE [Account]
    ADD CONSTRAINT FK_Account_Company
        FOREIGN KEY (CompanyId) REFERENCES Company (Id);

ALTER TABLE [UserInfo]
    ADD CONSTRAINT FK_UserInfo_Account
        FOREIGN KEY (AccountId) REFERENCES Account (Id);

ALTER TABLE [Toilet]
    ADD CONSTRAINT FK_Toilet_Company
        FOREIGN KEY (CompanyId) REFERENCES Company (Id);

ALTER TABLE [Toilet]
    ADD CONSTRAINT FK_Toilet_Account
        FOREIGN KEY (Id) REFERENCES Account (Id);

ALTER TABLE [ToiletFacility]
    ADD CONSTRAINT FK_ToiletFacility_Toilet
        FOREIGN KEY (ToiletId) REFERENCES Toilet (Id);

ALTER TABLE [ToiletFacility]
    ADD CONSTRAINT FK_ToiletFacility_Facility
        FOREIGN KEY (FacilityId) REFERENCES Facility (Id);

ALTER TABLE [ToiletService]
    ADD CONSTRAINT FK_ToiletService_Toilet
        FOREIGN KEY (ToiletId) REFERENCES Toilet (Id);

ALTER TABLE [ToiletService]
    ADD CONSTRAINT FK_ToiletService_Service
        FOREIGN KEY (ServiceId) REFERENCES Service (Id);

ALTER TABLE [ToiletImage]
    ADD CONSTRAINT FK_ToiletImage_Toilet
        FOREIGN KEY (ToiletId) REFERENCES Toilet (Id);

ALTER TABLE [CheckIn]
    ADD CONSTRAINT FK_CheckIn_Account
        FOREIGN KEY (AccountId) REFERENCES Account (Id);

ALTER TABLE [CheckIn]
    ADD CONSTRAINT FK_CheckIn_ToiletService
        FOREIGN KEY (ToiletServiceId) REFERENCES ToiletService (Id);

ALTER TABLE [Order]
    ADD CONSTRAINT FK_Order_Account
        FOREIGN KEY (AccountId) REFERENCES Account (Id);

ALTER TABLE [Payment]
    ADD CONSTRAINT FK_Payment_Account
        FOREIGN KEY (AccountId) REFERENCES Account (Id);

ALTER TABLE [Rating]
    ADD CONSTRAINT FK_Rating_Toilet
        FOREIGN KEY (ToiletId) REFERENCES Toilet (Id);

ALTER TABLE [RatingImage]
    ADD CONSTRAINT FK_RatingImage_Rating
        FOREIGN KEY (RatingId) REFERENCES Rating (Id);

------------------------------ INSERT VALUE ------------------------------
INSERT INTO Company (Name, Logo, Address, Ward, District, Province, Phone)
VALUES (N'Toilet Map', N'https://drive.google.com/file/d/1qmWrHPZ6e-XA8NZtaT9UVWXRXwpMXXA2/view?usp=sharing',
        N'Lô E2a-7, Đường D1', N'Phường Long Thạnh Mỹ', N'Quận Thủ Đức', N'Thành phố Hồ Chí Minh', N'(028) 7300 5588');
INSERT INTO Company (Name, Logo, Address, Ward, District, Province, Phone)
VALUES (N'Công ty dịch vụ công ích quận 1', 'https://dichvucongichquan1.com/wp-content/uploads/2021/12/logo.svg',
        N'28-30 Nguyễn Thái Bình', N'Phường Nguyễn Thái Bình', N'Quận 1', N'Thành phố Hồ Chí Minh', '(028) 38.215.611')
GO

INSERT INTO Role (Name)
VALUES ('Admin'),
       ('Manager'),
       ('Staff'),
       ('Toilet'),
       ('User')
GO

-- INSERT INTO Toilet (Id, Name, Address, Ward, District, Province, Latitude, Longitude, NearBy, isFree, OpenTime,
--                     CloseTime,
--                     CompanyId, Status)
-- VALUES (4, N'Nhà vệ sinh lưu động số 1 nè hihi mắc toilet quá', N'44/5/7/2/234A xa quá trời xa Trần Đình Xu',
--         N'Cô Giang',
--         N'Quận 1', N'Thành phố Hồ Chí Minh', 10.845254597727745, 106.79238946200942, N'Gần CircleK, gần Phúc Long', 0,
--         N'09:00:00', N'23:00:00', 2, N'Đang hoạt động'),
--        (5, N'Nhà vệ sinh lưu động số 2 pickaboo dui dẻ dui dẻ', N'79 Nguyễn Huệ', N'Bến Nghé', N'Quận 1',
--         N'Thành phố Hồ Chí Minh', 10.8360458, 106.8084369, null, 0, N'09:00:00', N'23:00:00', 2, N'Đang hoạt động');
-- GO

INSERT INTO Facility (Name, Type)
VALUES (N'Phòng vệ sinh', N'Phòng'),
       (N'Phòng tắm', N'Phòng'),
       (N'Phòng vệ sinh dành cho người khuyết tật', N'Phòng'),
       (N'Vòi xịt', N'Trang thiết bị'),
       (N'Máy sấy tay', N'Trang thiết bị'),
       (N'Giấy vệ sinh', N'Trang thiết bị')
GO

-- INSERT INTO ToiletFacility (ToiletId, FacilityId, Quantity, Description)
-- VALUES (4, 1, 8, N'4 phòng vệ sinh cho nữ, 4 phòng vệ sinh cho nam'),
--        (4, 3, 1, null),
--        (4, 4, 1, null)
-- GO

INSERT INTO Service (Name, Price, Turn)
VALUES (N'Đi vệ sinh (tiểu tiện)', 2000, 1),
       (N'Đi vệ sinh (đại tiện)', 4000, 2),
       (N'Đi tắm', 8000, 3)
GO

-- INSERT INTO ToiletService (ToiletId, ServiceId)
-- VALUES (4, 1),
--        (4, 2),
--        (4, 3)
-- GO

-- INSERT INTO ToiletImage (ToiletId, ImageSource)
-- VALUES (4,
--         N'https://dichvucongichquan1.com/wp-content/uploads/2021/04/z2469130019572_1b2874d47ba76fa3b7089d0ffa4b72c7.jpg');
-- INSERT INTO ToiletImage (ToiletId, ImageSource)
-- VALUES (4,
--         N'https://dichvucongichquan1.com/wp-content/uploads/2021/04/z2469130681021_b9303b13544929365e1810b07c7e3dff.jpg');
-- INSERT INTO ToiletImage (ToiletId, ImageSource)
-- VALUES (5, N'https://anh.eva.vn/upload/2-2015/images/2015-05-13/1431482470-ava.jpg');
-- INSERT INTO ToiletImage (ToiletId, ImageSource)
-- VALUES (4,
--         N'https://static.asianpaints.com/content/dam/asianpaintsbeautifulhomes/spaces/bathrooms/modern-toilet-design-ideas-for-contemporary-homes/Title-modern-toile-design-idea.jpg');
-- INSERT INTO ToiletImage (ToiletId, ImageSource)
-- VALUES (4, N'https://nhavesinhdidongwctoilet.com/upload/images/bao-gia-ban-nha-ve-sinh-cong-cong.jpg');
-- INSERT INTO ToiletImage (ToiletId, ImageSource)
-- VALUES (4, N'https://showroominax.vn/hl_uploads/tin-tuc/2022_06/nha-ve-sinh-cong-cong.jpg');

INSERT INTO Combo (TotalTurn, Price)
VALUES (8, 10000),
       (19, 20000),
       (50, 50000),
       (105, 100000)
GO