-- Drop OTP columns
ALTER TABLE users DROP COLUMN IF EXISTS otp_hash;
ALTER TABLE users DROP COLUMN IF EXISTS otp_expiry;

-- Add password column if it doesn't exist
ALTER TABLE users ADD COLUMN IF NOT EXISTS password VARCHAR(255);

-- Make password NOT NULL (Note: this might fail if there are already users, but it's a new DB)
-- If there are users, you'd need to set a default password first
UPDATE users SET password = 'temporary_password' WHERE password IS NULL;
ALTER TABLE users ALTER COLUMN password SET NOT NULL;
