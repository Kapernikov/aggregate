CREATE USER aggregate WITH PASSWORD 'aggregate';
CREATE DATABASE aggregate WITH OWNER aggregate;
GRANT ALL PRIVILEGES ON DATABASE aggregate TO aggregate;
\connect aggregate;
CREATE SCHEMA aggregate;
GRANT ALL PRIVILEGES ON SCHEMA aggregate TO aggregate;
