-- Add hashed_token column for secure token storage
-- During migration period, both auth_token and hashed_token are used for lookup
-- New tokens will populate both columns; eventually auth_token column can be dropped
ALTER TABLE User_AuthToken ADD COLUMN hashed_token VARCHAR(64) NULL;
CREATE INDEX idx_hashed_token ON User_AuthToken(hashed_token);
