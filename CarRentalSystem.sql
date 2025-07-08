CREATE DATABASE CarRentalSystem;

USE CarRentalSystem;
CREATE TABLE Permission (
PermissionId INT AUTO_INCREMENT PRIMARY KEY,
PermissionName VARCHAR(100),
Link VARCHAR(500)
);



CREATE TABLE Role(
RoleID INT AUTO_INCREMENT PRIMARY KEY,
RoleName VARCHAR(20)
);

CREATE TABLE Role_Permission (
PermissionId INT,
RoleID INT,
PRIMARY KEY(PermissionId,RoleID),
FOREIGN KEY (PermissionId) REFERENCES Permission(PermissionId),
FOREIGN KEY (RoleID) REFERENCES Role(RoleID)
);

CREATE TABLE User (
  UserID INT AUTO_INCREMENT PRIMARY KEY,
  FullName VARCHAR(50),
  Email VARCHAR(100),
  Password VARCHAR(255),
  RoleID INT,
  Status BOOLEAN,
  FOREIGN KEY (RoleID) REFERENCES Role(RoleID)
);

CREATE TABLE RefreshToken (
    TokenID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT,
    Token TEXT,
    ExpiryDate DATETIME,
    IsRevoked BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (UserID) REFERENCES User(UserID)
);


CREATE TABLE CarOwner (
    CarOwnerID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(100),
    DateOfBirth DATE,
    NationalIDNo VARCHAR(20),
    PhoneNo VARCHAR(20),
    Email VARCHAR(100),
     Address VARCHAR(255),
    DrivingLicense VARCHAR(50),
    Wallet DECIMAL(10, 2),
    UserID INT,
    FOREIGN KEY (UserID) REFERENCES User(UserID)
  
);


CREATE TABLE Customer (
    CustomerID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(100),
    DateOfBirth DATE,
    NationalIDNo VARCHAR(20),
    PhoneNo VARCHAR(20),
    Email VARCHAR(100),
    Address VARCHAR(255),
    DrivingLicense VARCHAR(50),
    Wallet DECIMAL(10, 2),
    UserID INT,
    FOREIGN KEY (UserID) REFERENCES User(UserID)
    
);

CREATE TABLE CarImage(
CarImageID INT AUTO_INCREMENT PRIMARY KEY,
CarImageLink TEXT
);



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
    FOREIGN KEY (CarOwnerID) REFERENCES CarOwner(CarOwnerID),
    FOREIGN KEY ( CarImageID) REFERENCES CarImage(CarImageID)
);


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


CREATE TABLE Feedback (
    FeedbackID INT AUTO_INCREMENT PRIMARY KEY,
    BookingNo INT,
    Ratings INT CHECK (Ratings >= 1 AND Ratings <= 5),
    Content TEXT,
    DateTime DATETIME,
    FOREIGN KEY (BookingNo) REFERENCES Booking(BookingNo)
);

INSERT INTO Role (RoleName) VALUES 
('Admin'),
('CarOwner'),
('Customer');


INSERT INTO User (FullName, Email, Password, RoleID, Status) VALUES 
('Nguyen Van A', 'admin@example.com', 'admin123', 1, TRUE),
('Tran Van B', 'owner@example.com', 'owner123', 2, TRUE),
('Le Thi C', 'customer@example.com', 'customer123', 3, TRUE);

INSERT INTO CarOwner (Name, DateOfBirth, NationalIDNo, PhoneNo, Email, Address, DrivingLicense, Wallet, UserID) VALUES 
('Tran Van B', '1985-05-20', '123456789', '0909123456', 'owner@example.com', '123 Le Loi, HCM', 'B123456', 1000000.00, 2);


INSERT INTO Customer (Name, DateOfBirth, NationalIDNo, PhoneNo, Email, Address, DrivingLicense, Wallet, UserID) VALUES 
('Le Thi C', '1990-08-15', '987654321', '0912345678', 'customer@example.com', '456 Tran Hung Dao, HCM', 'C654321', 500000.00, 3);


INSERT INTO CarImage (CarImageLink) VALUES 
('https://example.com/images/car1.jpg'),
('https://example.com/images/car2.jpg');


INSERT INTO Car (CarOwnerID, Name, LicensePlate, Brand, Model, Color, NumberOfSeats, ProductionYears, TransmissionType, FuelType, Mileage, FuelConsumption, BasePrice, Deposit, Address, Description, AdditionalFunctions, TermsOfUse, CarImageID) VALUES 
(1, 'Toyota Vios', '51A-12345', 'Toyota', 'Vios E', 'Black', 5, 2020, 'Automatic', 'Petrol', 30000, 6.5, 500000.00, 1000000.00, '123 Le Lai, HCM', 'Clean, good condition', 'GPS, Bluetooth', 'No smoking, return with full tank', 1);


INSERT INTO Booking (CustomerID, CarID, StartDateTime, EndDateTime, DriversInformation, PaymentMethod, Status) VALUES 
(1, 1, '2025-06-01 08:00:00', '2025-06-05 18:00:00', 'Customer will drive', 'Credit Card', 'Confirmed');

INSERT INTO Feedback (BookingNo, Ratings, Content, DateTime) VALUES 
(1, 5, 'Great experience! The car was clean and the owner was helpful.', '2025-06-06 10:00:00');





