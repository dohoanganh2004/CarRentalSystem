-- Tạo database
CREATE DATABASE CarRentalSystem;
USE CarRentalSystem;

-- 1. Bảng Permission
CREATE TABLE Permission (
    PermissionId INT AUTO_INCREMENT PRIMARY KEY,
    PermissionName VARCHAR(100),
    Link VARCHAR(500)
);

-- 2. Bảng Role
CREATE TABLE Role (
    RoleID INT AUTO_INCREMENT PRIMARY KEY,
    RoleName VARCHAR(20)
);

-- 3. Bảng Role - Permission
CREATE TABLE Role_Permission (
    PermissionId INT,
    RoleID INT,
    PRIMARY KEY(PermissionId, RoleID),
    FOREIGN KEY (PermissionId) REFERENCES Permission(PermissionId),
    FOREIGN KEY (RoleID) REFERENCES Role(RoleID)
);

-- 4. Bảng User
CREATE TABLE User (
    UserID INT AUTO_INCREMENT PRIMARY KEY,
    FullName VARCHAR(100) NOT NULL,
    DateOfBirth DATE,
    NationalIDNo VARCHAR(20) UNIQUE,
    PhoneNo VARCHAR(20),
    Email VARCHAR(100) UNIQUE NOT NULL,
    Password VARCHAR(255) NOT NULL,
    Address VARCHAR(255),
    DrivingLicense VARCHAR(50),
    Wallet DECIMAL(10,2) DEFAULT 0.0,
    RoleID INT,
    Status BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (RoleID) REFERENCES Role(RoleID)
);

-- 5. Bảng RefreshToken
CREATE TABLE RefreshToken (
    TokenID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT,
    Token TEXT,
    ExpiryDate DATETIME,
    IsRevoked BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (UserID) REFERENCES User(UserID)
);

-- 6. Bảng CarOwner
CREATE TABLE CarOwner (
    CarOwnerID INT PRIMARY KEY,
    FOREIGN KEY (CarOwnerID) REFERENCES User(UserID) ON DELETE CASCADE
);

-- 7. Bảng Customer
CREATE TABLE Customer (
    CustomerID INT PRIMARY KEY,
    FOREIGN KEY (CustomerID) REFERENCES User(UserID) ON DELETE CASCADE
);

-- 8. Bảng CarImage
CREATE TABLE CarImage (
    CarImageID INT AUTO_INCREMENT PRIMARY KEY,
    CarImageLink TEXT
);

-- 9. Bảng Car
CREATE TABLE Car (
    CarID INT AUTO_INCREMENT PRIMARY KEY,
    CarOwnerID INT,
    Name VARCHAR(100),
    LicensePlate VARCHAR(20) UNIQUE,
    Brand VARCHAR(50),
    Model VARCHAR(50),
    Color VARCHAR(30),
    NumberOfSeats INT,
    ProductionYears YEAR,
    TransmissionType VARCHAR(50),
    FuelType VARCHAR(50),
    Mileage INT,
    FuelConsumption DECIMAL(5,2),
    BasePrice DECIMAL(10,2),
    Deposit DECIMAL(10,2),
    Address VARCHAR(255),
    Description TEXT,
    AdditionalFunctions TEXT,
    TermsOfUse TEXT,
    CarImageID INT,
    Status  VARCHAR(50),
    FOREIGN KEY (CarOwnerID) REFERENCES CarOwner(CarOwnerID),
    FOREIGN KEY (CarImageID) REFERENCES CarImage(CarImageID)
);

-- 10. Bảng Booking
CREATE TABLE Booking (
    BookingNo INT AUTO_INCREMENT PRIMARY KEY,
    CustomerID INT,
    CarID INT,
    StartDateTime DATETIME,
    EndDateTime DATETIME,
    DriversInformation TEXT,
    PaymentMethod VARCHAR(50),
    Status VARCHAR(50),
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID),
    FOREIGN KEY (CarID) REFERENCES Car(CarID)
);

-- 11. Bảng Feedback
CREATE TABLE Feedback (
    FeedbackID INT AUTO_INCREMENT PRIMARY KEY,
    BookingNo INT,
    Ratings INT CHECK (Ratings >= 1 AND Ratings <= 5),
    Content TEXT,
    DateTime DATETIME,
    FOREIGN KEY (BookingNo) REFERENCES Booking(BookingNo)
);
-- Sua bang CarImage
ALTER TABLE CarImage 
DROP COLUMN CarImageLink,
ADD COLUMN FrontImageUrl VARCHAR(255),
ADD COLUMN BackImageUrl VARCHAR(255),
ADD COLUMN LeftImageUrl VARCHAR(255),
ADD COLUMN RightImageUrl VARCHAR(255);
-- Sua bang Car
ALTER TABLE car
ADD COLUMN RegistrationPaperUrl VARCHAR(255),
ADD COLUMN CertificateOfInspectionUrl VARCHAR(255),
ADD COLUMN InsuranceUrl VARCHAR(255);


-- Insert Role
INSERT INTO Role (RoleName) VALUES 
('Admin'),
('CarOwner'),
('Customer');


INSERT INTO Booking (CustomerID, CarID, StartDateTime, EndDateTime, DriversInformation, PaymentMethod, Status)
VALUES
(3, 1, '2025-07-01 08:00:00', '2025-07-03 08:00:00', 'Driver A', 'Credit Card', 'Completed'),
(3, 1, '2025-07-04 09:00:00', '2025-07-06 09:00:00', 'Driver B', 'Cash', 'Completed'),
(19, 1, '2025-07-07 10:00:00', '2025-07-08 18:00:00', 'Driver C', 'Bank Transfer', 'Completed');

INSERT INTO Feedback (BookingNo, Ratings, Content, DateTime)
VALUES
(2, 5, 'Dịch vụ tuyệt vời, xe mới và sạch sẽ.', '2025-07-03 12:00:00'),
(3, 4, 'Xe chạy ổn định, tài xế thân thiện.', '2025-07-06 14:00:00'),
(4, 3, 'Xe khá ổn nhưng hơi trễ giờ giao.', '2025-07-08 19:00:00');



 select  * from RefreshToken
 select * from user
 select * from car
 select * from CarOwner
 select * from Customer
 select * from CarImage
 select * from Booking
 
 select * from feedback
