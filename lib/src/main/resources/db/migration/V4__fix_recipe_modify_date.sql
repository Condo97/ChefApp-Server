-- Add modify_date column if it doesn't exist (it may have been overwritten by creation_date mapping)
ALTER TABLE Recipe ADD COLUMN IF NOT EXISTS modify_date TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
-- Copy creation_date to modify_date for existing records where modify_date is null
UPDATE Recipe SET modify_date = creation_date WHERE modify_date IS NULL;
