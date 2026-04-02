ALTER TABLE User_AuthToken ADD COLUMN expiry_date TIMESTAMP NULL;
-- Set existing tokens to expire in 90 days from now
UPDATE User_AuthToken SET expiry_date = DATE_ADD(NOW(), INTERVAL 90 DAY) WHERE expiry_date IS NULL;
