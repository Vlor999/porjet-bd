-- Création des tables

CREATE TABLE 'Utilisateur'
(
    email VARCHAR(255) PRIMARY KEY,
    nom VARCHAR(255),
    prenom VARCHAR(255),
    adressePostale VARCHAR(255),
)

CREATE TABLE 'Caractéristiques'
(
    nomCar VARCHAR(255) PRIMARY KEY,
    valeurCar VARCHAR(255),
    idCar INT,
)

CREATE TABLE 'Produit'
(
    idProduit INT PRIMARY KEY,
    nomProduit VARCHAR(255),
    prixRevient INT,
    stock INT,
    caractéristique Caractéristiques, 
)

CREATE TABLE 'Catégorie'
(
    nomCat VARCHAR(255) PRIMARY KEY,
    descrCat VARCHAR(255),
)

CREATE TABLE 'SalleDeVente'
(
    idSalle INT PRIMARY KEY,
    catégorie nomCat,
    description VARCHAR(255),
)

CREATE TABLE 'Vente'
(
    idVente INT PRIMARY KEY,
    typeVente INT, --mettre un type ici
    prixDepart INT,
    revocable BOOLEAN,
    limiteOffres INT,
    durée INT,
    produit idProduit,
    idSalle idSalle,
)

CREATE TABLE 'Offre'
(
    idOffre INT PRIMARY KEY,
    email email,
    vente idVente,
    prixOffre INT,
    dateOffre DATE,
    heureOffre TIME,
    quantité INT,
)

