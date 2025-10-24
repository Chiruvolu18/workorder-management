-- init-db.sql
CREATE DATABASE userauth_db;
CREATE DATABASE workorder_db;
-- Optionally grant all privileges to your application user
GRANT ALL PRIVILEGES ON DATABASE userauth_db TO postgres;
GRANT ALL PRIVILEGES ON DATABASE workorder_db TO postgres;