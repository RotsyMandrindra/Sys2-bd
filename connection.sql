-- Créer la base de données sysdb (si elle n'existe pas déjà)
CREATE DATABASE sysdb;

-- Se connecter à la base de données
\c sysdb

-- Créer l'extension uuid-ossp (si ce n'est pas déjà fait)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Créer la table connection
CREATE TABLE connection (
    id uuid PRIMARY KEY DEFAULT uuid_generate_v1(),
    first_name varchar(50) NOT NULL,
    connection_datetime timestamp with time zone DEFAULT current_date
);
