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
    Phone    VARCHAR(20)         NULL,
    Status   NVARCHAR(20)        NULL,
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
    Id        INT           NOT NULL,
    Name      NVARCHAR(50)  NOT NULL,
    Address   NVARCHAR(100) NOT NULL,
    Ward      NVARCHAR(50)  NOT NULL,
    District  NVARCHAR(50)  NOT NULL,
    Province  NVARCHAR(50)  NOT NULL,
    Latitude  FLOAT         NOT NULL,
    Longitude FLOAT         NOT NULL,
    NearBy    NVARCHAR(200) NULL,
    isFree    BIT           NOT NULL,
    OpenTime  TIME          NOT NULL,
    CloseTime TIME          NOT NULL,
    CompanyId INT           NOT NULL,
    Status    NVARCHAR(20)  NOT NULL
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
    TotalQuantity INT                 NULL,
)
GO

CREATE TABLE [Service]
(
    Id        INT IDENTITY (1, 1) NOT NULL,
    Name      NVARCHAR(50)        NOT NULL,
    Price     INT                 NOT NULL,
    Turn      INT                 NOT NULL,
    TurnPrice INT                 NOT NULL
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
    Turn            INT                 NULL,
    TurnPrice       INT                 NULL,
    CheckoutTime    DATETIME            NULL
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

CREATE TABLE [Transaction]
(
    TransactionNo INT         NOT NULL,
    PaymentId     INT         NOT NULL,
    BankCode      VARCHAR(20) NOT NULL,
    BankTranNo    VARCHAR(20) NOT NULL,
    CardType      VARCHAR(20) NOT NULL,
    PayDate       DATETIME    NOT NULL,
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
    Status    NVARCHAR(20)        NULL,
    CheckInId INT                 NULL
)
GO

CREATE TABLE [RatingImage]
(
    Id          INT IDENTITY (1, 1) NOT NULL,
    RatingId    INT                 NOT NULL,
    ImageSource TEXT                NOT NULL
)
GO

CREATE TABLE [CommonComment]
(
    Id     INT IDENTITY (1,1) NOT NULL,
    Name   NVARCHAR(100)      NOT NULL,
    Status NVARCHAR(20)       NOT NULL
)

CREATE TABLE [RatingCommonComment]
(
    Id              INT IDENTITY (1,1) NOT NULL,
    RatingId        INT                NULL,
    CommonCommentId INT                NULL
)

CREATE TABLE [SensitiveWord]
(
    Id   INT IDENTITY (1, 1) NOT NULL,
    Word NVARCHAR(50)        NOT NULL
)
GO

CREATE TABLE [Announcement]
(
    Id          INT IDENTITY (1, 1) NOT NULL,
    Title       NVARCHAR(255)       NULL,
    Url         TEXT                NULL,
    ImageSource TEXT                NULL,
    StartDate   DATE                NULL,
    EndDate     DATE                NULL,
    Type        NVARCHAR(20)        NULL,
    Description NTEXT               NULL
)

CREATE TABLE [Report]
(
    Id         INT IDENTITY (1,1) NOT NULL,
    ToiletId   INT                NOT NULL,
    Message    NVARCHAR(100)      NOT NULL,
    Status     NVARCHAR(20)       NULL,
    CreateDate DATETIME           NULL
)

CREATE TABLE [Suggestion]
(
    Id            INT IDENTITY (1,1) NOT NULL,
    ToiletId      INT                NULL,
    Message       NTEXT              NULL,
    IsAccepted    BIT                NULL,
    StartDate     DATE               NULL,
    EndDate       DATE               NULL,
    ActualCount   INT                NULL,
    ExpectedCount FLOAT              NULL,
    Streak        INT                NULL,
    IsLow         BIT                NULL
)

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
ALTER TABLE [Transaction]
    ADD CONSTRAINT PK_Transaction PRIMARY KEY (TransactionNo);
ALTER TABLE [Payment]
    ADD CONSTRAINT PK_Payment PRIMARY KEY (Id);
ALTER TABLE [Rating]
    ADD CONSTRAINT PK_Rating PRIMARY KEY (Id);
ALTER TABLE [RatingImage]
    ADD CONSTRAINT PK_RatingImage PRIMARY KEY (Id);
ALTER TABLE [CommonComment]
    ADD CONSTRAINT PK_CommonComment PRIMARY KEY (Id);
ALTER TABLE [RatingCommonComment]
    ADD CONSTRAINT PK_RatingCommonComment PRIMARY KEY (Id);
ALTER TABLE [SensitiveWord]
    ADD CONSTRAINT PK_SensitiveWord PRIMARY KEY (Id);
ALTER TABLE [Announcement]
    ADD CONSTRAINT PK_Announcement PRIMARY KEY (Id);
ALTER TABLE [Report]
    ADD CONSTRAINT PK_Report PRIMARY KEY (Id);
ALTER TABLE [Suggestion]
    ADD CONSTRAINT PK_Suggestion PRIMARY KEY (Id);
ALTER TABLE [Configuration]
    ADD CONSTRAINT PK_Configuration PRIMARY KEY (Id);

--- UNIQUE ---
ALTER TABLE [Account]
    ADD CONSTRAINT UNIQUE_Username UNIQUE (Username);
ALTER TABLE [SensitiveWord]
    ADD CONSTRAINT UNIQUE_Word UNIQUE (Word);
ALTER TABLE [Transaction]
    ADD CONSTRAINT UNIQUE_PaymentId UNIQUE (PaymentId);

-- FOREIGN KEY ---
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

ALTER TABLE [Report]
    ADD CONSTRAINT FK_Report_Toilet
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

ALTER TABLE [Transaction]
    ADD CONSTRAINT FK_Transaction_Payment
        FOREIGN KEY (PaymentId) REFERENCES Payment (Id);

ALTER TABLE [Rating]
    ADD CONSTRAINT FK_Rating_Toilet
        FOREIGN KEY (ToiletId) REFERENCES Toilet (Id);

ALTER TABLE [Rating]
    ADD CONSTRAINT FK_Rating_Account
        FOREIGN KEY (AccountId) REFERENCES Account (Id);

ALTER TABLE [Rating]
    ADD CONSTRAINT FK_Rating_CheckIn
        FOREIGN KEY (CheckInId) REFERENCES CheckIn (Id);

ALTER TABLE [RatingImage]
    ADD CONSTRAINT FK_RatingImage_Rating
        FOREIGN KEY (RatingId) REFERENCES Rating (Id);

ALTER TABLE [RatingCommonComment]
    ADD CONSTRAINT FK_RatingCommonComment_CommonComment
        FOREIGN KEY (CommonCommentId) REFERENCES CommonComment (Id);

ALTER TABLE [RatingCommonComment]
    ADD CONSTRAINT FK_RatingCommonComment_Rating
        FOREIGN KEY (CommonCommentId) REFERENCES Rating (Id);

ALTER TABLE [Suggestion]
    ADD CONSTRAINT FK_Suggestion_Toilet
        FOREIGN KEY (ToiletId) REFERENCES Toilet (Id);

------------------------------ FULL-TEXT SEARCH ------------------------------
CREATE FULLTEXT CATALOG sensitive_words_ctl WITH ACCENT_SENSITIVITY = OFF

CREATE FULLTEXT INDEX ON SensitiveWord
    (
     Word Language 1066
        )
    KEY INDEX UNIQUE_Word ON sensitive_words_ctl
    WITH CHANGE_TRACKING AUTO;
GO

------------------------------ INSERT VALUE ------------------------------
INSERT INTO Company (Name, Logo, Address, Ward, District, Province, Phone)
VALUES (N'Toilet Map', N'https://drive.google.com/file/d/1qmWrHPZ6e-XA8NZtaT9UVWXRXwpMXXA2/view?usp=sharing',
        N'Lô E2a-7, Đường D1', N'Phường Long Thạnh Mỹ', N'Quận Thủ Đức', N'Thành phố Hồ Chí Minh', N'(028) 7300 5588');
INSERT INTO Company (Name, Logo, Address, Ward, District, Province, Phone)
VALUES (N'Công ty dịch vụ công ích quận 1', 'https://dichvucongichquan1.com/wp-content/uploads/2021/12/logo.svg',
        N'28-30 Nguyễn Thái Bình', N'Phường Nguyễn Thái Bình', N'Quận 1', N'Thành phố Hồ Chí Minh', '(028) 38.215.611')
GO

INSERT INTO [Role] (Name)
VALUES ('Admin'),
       ('Manager'),
       ('Staff'),
       ('Toilet'),
       ('User')
GO

INSERT Account (Username, Password, Status, RoleId, CompanyId)
VALUES (N'admin-toilet-map', N'$2a$10$HyWQElRRRZNVsdzqVqT3V.Rib8kkAq4ZLdc8CVGnoUhxj/0dlxlDK', N'Đang hoạt động', 1, 1)
INSERT Account (Username, Password, Status, RoleId, CompanyId)
VALUES (N'manager-1', N'$2a$10$/dH1LFY1VeSe9aQoibV8puAthiOjM/7Cb0NwnDfSA40wUxnagEkRG', N'Đang hoạt động', 2, 2)
INSERT Account (Username, Password, Status, RoleId, CompanyId)
VALUES (N'staff-1', N'$2a$10$NJT/cGta.EQBUewK/C6u6..GLrVlcCXEkalQZxcN0VBevt8IW4z8u', N'Đang hoạt động', 3, 1)
INSERT Account (Username, Password, Status, RoleId, CompanyId)
VALUES (N'toilet-1', N'$2a$10$4uC1cx1vwiuZKgRtuQqyd.CaYVXPzyWlyJGXalk5EMKmg/tpUbGv2', N'Đang hoạt động', 4, 2)
INSERT Account (Username, Password, Status, RoleId, CompanyId)
VALUES (N'toilet-2', N'$2a$10$1I/8VJEfF/FaYCQGYnbgBuMx7J7Ejb7XIyvkvJ1RRlpVu4GiHlHjC', N'Đang hoạt động', 4, 2)
INSERT Account (Username, Password, Status, RoleId, CompanyId)
VALUES (N'0849666957', NULL, N'Đang hoạt động', 5, NULL)
INSERT Account (Username, Password, Status, RoleId, CompanyId)
VALUES (N'0834101001', NULL, N'Đang hoạt động', 5, NULL)
INSERT Account (Username, Password, Status, RoleId, CompanyId)
VALUES (N'0921220406', NULL, N'Đang hoạt động', 5, NULL)

INSERT Toilet (Id, Name, Address, Ward, District, Province, Latitude, Longitude, NearBy, isFree, OpenTime, CloseTime,
               CompanyId, Status)
VALUES (4, N'Nhà vệ sinh lưu động số 1', N'447 Lê Văn Việt', N'Tăng Nhơn Phú A', N'Thủ Đức',
        N'Hồ Chí Minh', 10.845111568659927, 106.79249010613532,
        N'Gần CircleK, gần Phúc Long, gần khu trung tâm mua sắm, gần chợ', 0, CAST(N'08:00:00' AS Time),
        CAST(N'22:00:00' AS Time), 2, N'Đang hoạt động')
INSERT Toilet (Id, Name, Address, Ward, District, Province, Latitude, Longitude, NearBy, isFree, OpenTime, CloseTime,
               CompanyId, Status)
VALUES (5, N'Nhà vệ sinh lưu động số 2', N'77 Nguyễn Huệ', N'Bến Nghé', N'Quận 1', N'Thành phố Hồ Chí Minh',
        10.773215070315723, 106.70414522538253, N'Gần Circle K, gần Katinat', 0, CAST(N'08:00:00' AS Time), CAST(N'22:00:00' AS Time), 2, N'Đang hoạt động')

INSERT UserInfo (AccountId, FullName, Gmail, Avatar, AccountBalance, AccountTurn, DefaultPayment)
VALUES (6, N'Huỳnh Lê Thủy Tiên', NULL, NULL, 350000, 100, N'Số lượt')
INSERT UserInfo (AccountId, FullName, Gmail, Avatar, AccountBalance, AccountTurn, DefaultPayment)
VALUES (7, N'Nguyễn Đào Đức Quân', NULL, NULL, 350000, 100, N'Số lượt')
INSERT UserInfo (AccountId, FullName, Gmail, Avatar, AccountBalance, AccountTurn, DefaultPayment)
VALUES (8, N'Nguyễn Lâm Thúy Phượng', NULL, NULL, 350000, 100, N'Số lượt')

INSERT INTO Facility (Name, Type)
VALUES (N'Phòng vệ sinh', N'Phòng'),
       (N'Phòng tắm', N'Phòng'),
       (N'Phòng vệ sinh dành cho người khuyết tật', N'Phòng'),
       (N'Vòi xịt', N'Trang thiết bị'),
       (N'Máy sấy tay', N'Trang thiết bị'),
       (N'Giấy vệ sinh', N'Trang thiết bị'),
       (N'Nước rửa tay', N'Trang thiết bị')
GO

INSERT INTO ToiletFacility (ToiletId, FacilityId, Quantity, Description)
VALUES (4, 1, 8, NULL),
       (4, 3, 1, NULL),
       (4, 4, 1, NULL)
INSERT INTO ToiletFacility (ToiletId, FacilityId, Quantity, Description)
VALUES (5, 1, 4, NULL)
INSERT INTO ToiletFacility (ToiletId, FacilityId, Quantity, Description)
VALUES (5, 2, 4, NULL)
INSERT INTO ToiletFacility (ToiletId, FacilityId, Quantity, Description)
VALUES (5, 3, 4, NULL)

INSERT INTO Service (Name, Price, Turn, TurnPrice)
VALUES (N'Đi vệ sinh (tiểu tiện)', 5000, 1, 3000),
       (N'Đi vệ sinh (đại tiện)', 10000, 2, 6000),
       (N'Đi tắm', 15000, 3, 9000)
GO

INSERT ToiletService (ToiletId, ServiceId)
VALUES (4, 1)
INSERT ToiletService (ToiletId, ServiceId)
VALUES (4, 2)
INSERT ToiletService (ToiletId, ServiceId)
VALUES (4, 3)
INSERT ToiletService (ToiletId, ServiceId)
VALUES (5, 1)
INSERT ToiletService (ToiletId, ServiceId)
VALUES (5, 2)
INSERT ToiletService (ToiletId, ServiceId)
VALUES (5, 3)

INSERT INTO ToiletImage (ToiletId, ImageSource)
VALUES (4,
        N'https://dichvucongichquan1.com/wp-content/uploads/2021/04/z2469130019572_1b2874d47ba76fa3b7089d0ffa4b72c7.jpg')
INSERT INTO ToiletImage (ToiletId, ImageSource)
VALUES (4,
        N'https://dichvucongichquan1.com/wp-content/uploads/2021/04/z2469130681021_b9303b13544929365e1810b07c7e3dff.jpg')
INSERT INTO ToiletImage (ToiletId, ImageSource)
VALUES (5, N'https://anh.eva.vn/upload/2-2015/images/2015-05-13/1431482470-ava.jpg')
INSERT INTO ToiletImage (ToiletId, ImageSource)
VALUES (4,
        N'https://static.asianpaints.com/content/dam/asianpaintsbeautifulhomes/spaces/bathrooms/modern-toilet-design-ideas-for-contemporary-homes/Title-modern-toile-design-idea.jpg')
INSERT INTO ToiletImage (ToiletId, ImageSource)
VALUES (4, N'https://nhavesinhdidongwctoilet.com/upload/images/bao-gia-ban-nha-ve-sinh-cong-cong.jpg')
INSERT INTO ToiletImage (ToiletId, ImageSource)
VALUES (4, N'https://showroominax.vn/hl_uploads/tin-tuc/2022_06/nha-ve-sinh-cong-cong.jpg')

INSERT Combo (TotalTurn, Price)
VALUES (5, 15000)
INSERT Combo (TotalTurn, Price)
VALUES (10, 30000)
INSERT Combo (TotalTurn, Price)
VALUES (20, 60000)
INSERT Combo (TotalTurn, Price)
VALUES (30, 90000)
INSERT Combo (TotalTurn, Price)
VALUES (50, 150000)
INSERT Combo (TotalTurn, Price)
VALUES (100, 300000)
INSERT Combo (TotalTurn, Price)
VALUES (150, 450000)
INSERT Combo (TotalTurn, Price)
VALUES (200, 600000)

INSERT Announcement (Title, Url, ImageSource, StartDate, EndDate, Type, Description)
VALUES (N'Instagram', N'com.instagram.android',
        N'https://firebasestorage.googleapis.com/v0/b/toilet-map-img.appspot.com/o/ads%2Fpngtree-three-dimensional-instagram-icon-png-image_9015419.png?alt=media&token=6ab57e62-70c4-423e-8b56-f02054399113',
        NULL, NULL, N'External-App', N'Miễn phí toàn hệ thống nhà vệ sinh')
INSERT Announcement (Title, Url, ImageSource, StartDate, EndDate, Type, Description)
VALUES (N'Mừng dịp lễ 30/4 và 1/5, Giỗ Tổ Hùng Vương 2023 - Miễn phí toàn bộ hệ thống', NULL,
        N'https://scontent.fsgn8-4.fna.fbcdn.net/v/t39.30808-6/351344290_258164923533905_1733312311989743227_n.jpg?_nc_cat=105&ccb=1-7&_nc_sid=730e14&_nc_ohc=J6ZId1q5l7AAX_O5ZW-&_nc_ht=scontent.fsgn8-4.fna&oh=00_AfBsWH0TvDoa1vnx3HIMA_fDCzSwVnU_ml4uObdVpXFtKQ&oe=6482789E',
        CAST(N'2023-04-29' AS Date), CAST(N'2023-05-03' AS Date), N'Internal', N'Năm 2023, giáo viên được nghỉ dịp lễ 30/4 và 1/5, Giỗ Tổ Hùng Vương từ 4 – 5 ngày (tùy theo chế độ làm việc nghỉ ngày 1 hay 2 ngày cuối tuần).
Do đó, hệ thống nhà vệ sinh công cộng sẽ thực hiện giảm giá kể từ ngày 29/04 đến hết ngày 03/05. Ưu đãi được áp dụng trên tất cả các nhà vệ sinh của hệ thống Toilet Map, bao gồm Dịch vụ Công ích Quận 1 và các đơn vị khác.')
INSERT Announcement (Title, Url, ImageSource, StartDate, EndDate, Type, Description)
VALUES (N'Đổ rác', N'https://thanhtoan.dichvucongichquan1.com/payment?dv=CI1',
        N'https://firebasestorage.googleapis.com/v0/b/toilet-map-img.appspot.com/o/ads%2Ft%E1%BA%A3i%20xu%E1%BB%91ng.png?alt=media&token=bde9266d-a926-46bd-b230-257ba1fa771c',
        NULL, NULL, N'External', N'CÔNG TY TNHH MỘT THÀNH VIÊN DỊCH VỤ CÔNG ÍCH QUẬN 1')
INSERT Announcement (Title, Url, ImageSource, StartDate, EndDate, Type, Description)
VALUES (N'Facebook', N'com.facebook.katana',
        N'https://firebasestorage.googleapis.com/v0/b/toilet-map-img.appspot.com/o/ads%2FFacebook_Logo_(2019).png?alt=media&token=dd637cd0-7a12-4f7f-9722-413a63050955',
        NULL, NULL, N'External-App', N'Description')
INSERT Announcement (Title, Url, ImageSource, StartDate, EndDate, Type, Description)
VALUES (N'Đi toilet nhận ngay Voucher siêu khét', NULL,
        N'https://firebasestorage.googleapis.com/v0/b/toilet-map-img.appspot.com/o/ads%2FIn-voucher-quang-cao-spa.png?alt=media&token=d4da24ab-d93e-4161-a6d6-5c81b3d6bf03',
        CAST(N'2023-06-01' AS Date), CAST(N'2023-06-03' AS Date), N'Internal', N'Deal siêu hot!!!
Kể từ ngày 06/06 đến ngày 10/06, nhân dịp khai trương ra mắt hệ thống, Toilet Map mang đến ưu đãi độc quyền cho bạn: Checkin tại Nhà vệ sinh Quận 1, nhận ngay voucher giảm giá 50% hóa đơn Starbucks.
Ưu đãi có giới hạn số lượng. Nhanh chân đi toilet nào.')
INSERT Announcement (Title, Url, ImageSource, StartDate, EndDate, Type, Description)
VALUES (N'Khuyến mãi 7.7', NULL,
        N'https://firebasestorage.googleapis.com/v0/b/toilet-map-img.appspot.com/o/ads%2Fstock-vector-toilet-vector-banner-design-use-for-bathroom-and-toilets-1499754035.jpg?alt=media&token=ee9835ff-6e40-4569-bceb-b155903987e5',
        CAST(N'2023-06-03' AS Date), CAST(N'2023-06-04' AS Date), N'Internal',
        N'Giảm ngay nửa giá tiền cho tất cả dịch vụ.')

INSERT CommonComment (Name, Status)
VALUES (N'Thái độ nhân viên kém', N'Hiển thị')
INSERT CommonComment (Name, Status)
VALUES (N'Nhà vệ sinh bẩn, hôi', N'Hiển thị')
INSERT CommonComment (Name, Status)
VALUES (N'Trang thiết bị hư hỏng', N'Hiển thị')
INSERT CommonComment (Name, Status)
VALUES (N'Thiếu nước - giấy vệ sinh', N'Hiển thị')
INSERT CommonComment (Name, Status)
VALUES (N'Tệ nạn xã hội', N'Không hiển thị')

INSERT SensitiveWord (Word)
VALUES (N'do mo')
INSERT SensitiveWord (Word)
VALUES (N'ew')
INSERT SensitiveWord (Word)
VALUES (N'Gớm')
INSERT SensitiveWord (Word)
VALUES (N'hôi hám')