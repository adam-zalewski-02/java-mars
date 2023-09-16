create table if not exists quotes
(
    id    int auto_increment,
    quote varchar(255)
);

create table if not exists customers
(
    id int auto_increment,
    name varchar(255),
    address varchar(255),
    tierId int,
    garbageDisposed decimal(10,3),
    amountLeftToDispose decimal(10,3),
    pickupsLeft int

);

create table if not exists orders (
    id int auto_increment,
    productId int,
    customerId int,
    amountInKg int,
    trackNumber varchar(255),
    orderStatus varchar(255),
    orderDate date
);

create table if not exists products (
    id int auto_increment,
    name varchar(255),
    pricePerKg decimal(10,2),
    amountInStockInKg decimal(10,2)
);

create table if not exists tiers (
    id int auto_increment,
    name varchar(255),
    monthlyLimitInKg int,
    additionalDisposalPerKg decimal(10,2),
    freePickUps int,
    pickupFeePerKg decimal(10,2),
    paymentPerMonth decimal(10,2)
);

create table if not exists pickups (
    id int auto_increment,
    customerId int,
    dateOfRequest date,
    requestedPickupDate date,
    status varchar(255)
);