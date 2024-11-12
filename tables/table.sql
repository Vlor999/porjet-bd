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
)

CREATE TABLE 'Produit'
(
    idProduit INT PRIMARY KEY,
    nomProduit VARCHAR(255),
    prixRevient INT CHECK (prixRevient > 0),
    stock INT,
    caractéristique Caractéristiques, 
    FOREIGN KEY (caractéristique) REFERENCES Caractéristiques(nomCar),
)

CREATE TABLE 'Catégorie'
(
    nomCat VARCHAR(255) PRIMARY KEY,
    descrCat VARCHAR(255),
)

CREATE TABLE 'SalleDeVente'
(
    idSalle INT PRIMARY KEY,
    catégorie nomCat NOT NULL,
    description VARCHAR(255),
    FOREIGN KEY (catégorie) REFERENCES Catégorie(nomCat),
)

CREATE TABLE 'Vente'
(
    idVente INT PRIMARY KEY,
    typeVente INT, --mettre un type ici
    prixDepart INT CHECK (prixDepart > 0),
    revocable BOOLEAN NOT NULL,
    limiteOffres INT CHECK (limiteOffres > 0),
    durée INT CHECK (durée > 0),
    produit idProduit NOT NULL,
    idSalle idSalle NOT NULL,
    FOREIGN KEY (produit) REFERENCES Produit(idProduit),
    FOREIGN KEY (idSalle) REFERENCES SalleDeVente(idSalle),
)

CREATE TABLE 'Offre'
(
    idOffre INT PRIMARY KEY,
    email email,
    vente idVente,
    prixOffre INT CHECK (prixOffre > 0),
    dateOffre DATE,
    heureOffre TIME,
    quantité INT CHECK (quantité > 0),
    FOREIGN KEY (email) REFERENCES Utilisateur(email),
    FOREIGN KEY (vente) REFERENCES Vente(idVente),
)

