INSERT INTO quotes VALUES (0, 'You must accept that you might fail.');
INSERT INTO quotes VALUES (1, 'Everyone enjoys doing the kind of work for which he is best suited.');
INSERT INTO orders (productId, customerId, amountInKg, trackNumber, orderStatus, orderDate) VALUES (1, 1, 150, 'LV0987654321BE', 'PENDING', '2023-11-23');
INSERT INTO orders (productId, customerId, amountInKg, trackNumber, orderStatus, orderDate) VALUES (2, 2, 250, 'LV0987654321BE', 'PENDING', '2023-11-24');
INSERT INTO products (name, pricePerKg, amountInStockInKg) VALUES ('Iron (Fe)', 2.50, 1500);
INSERT INTO products (name, pricePerKg, amountInStockInKg) VALUES ('Zinc (Zn)', 2.50, 1500);
INSERT INTO tiers (name, monthlyLimitInKg, additionalDisposalPerKg, freePickUps, pickupFeePerKg, paymentPerMonth) VALUES ('Basic', 200, 1.50, 0, 1.50, 99);
INSERT INTO tiers (name, monthlyLimitInKg, additionalDisposalPerKg, freePickUps, pickupFeePerKg, paymentPerMonth) VALUES ('Premium', 300, 1.50, 1, 1.50, 129);
INSERT INTO tiers (name, monthlyLimitInKg, additionalDisposalPerKg, freePickUps, pickupFeePerKg, paymentPerMonth) VALUES ('Raccoon', 600, 1.00, 5, 1.50, 249);
INSERT INTO tiers (id, name, monthlyLimitInKg, additionalDisposalPerKg, freePickUps, pickupFeePerKg, paymentPerMonth) VALUES (0 ,'None', 0, 0, 0, 0, 0);
INSERT INTO customers (name, address, tierId, garbageDisposed, amountLeftToDispose, pickupsLeft) VALUES ('Adam', 'Vakekerkweg 69', 1, 0, 200, 0);
INSERT INTO customers (name, address, tierId, garbageDisposed, amountLeftToDispose, pickupsLeft) VALUES ('Tom', 'Hoge hul 69', 3, 0, 600, 5);
INSERT INTO customers (name, address, tierId, garbageDisposed, amountLeftToDispose, pickupsLeft) VALUES ('Bram', 'Kaaistraat 69', 2, 0, 300, 1);
INSERT INTO pickups (customerId, dateOfRequest, requestedPickupDate, status) VALUES (1, '2023-11-23', '2023-11-23', 'PENDING');

