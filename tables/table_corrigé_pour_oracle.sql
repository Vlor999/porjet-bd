-- Création des tables

DROP TABLE Offre CASCADE CONSTRAINTS;
DROP TABLE Vente CASCADE CONSTRAINTS;
DROP TABLE Produit CASCADE CONSTRAINTS;
DROP TABLE SalleDeVente CASCADE CONSTRAINTS;
DROP TABLE Caracteristiques CASCADE CONSTRAINTS;
DROP TABLE Categorie CASCADE CONSTRAINTS;
DROP TABLE Utilisateur CASCADE CONSTRAINTS;



CREATE TABLE Categorie (
    NomCat VARCHAR(50) PRIMARY KEY,
    DescrCat VARCHAR(255) NOT NULL
);

CREATE TABLE SalleDeVente (
    IdSalle INT PRIMARY KEY,
    EstMontante INT NOT NULL,
    CHECK (EstMontante IN (0,1)),
    EstOccupee INT NOT NULL,
    CHECK (EstOccupee IN (0,1)),
    EstRevocable INT NOT NULL,
    CHECK (EstMontante IN (0,1)),
    LimiteOffres INT,
    TypeDuree VARCHAR(20) NOT NULL,
    Categorie VARCHAR(50) NOT NULL,
    FOREIGN KEY (Categorie) REFERENCES Categorie(NomCat),
    CHECK (LimiteOffres > 0 or LimiteOffres = -1),
    CHECK (TypeDuree IN ('limitee', 'illimitee'))
);

CREATE TABLE Produit (
    IdProduit INT PRIMARY KEY,
    NomProduit VARCHAR(100) NOT NULL,
    PrixRevient FLOAT NOT NULL,
    Stock INT NOT NULL,
    DispoProduit INT NOT NULL,
    CHECK (DispoProduit IN (0,1)),
    NomCat VARCHAR(50) NOT NULL,
    FOREIGN KEY (NomCat) REFERENCES Categorie(NomCat),
    IdSalle INT NOT NULL,
    FOREIGN KEY (IdSalle) REFERENCES SalleDeVente(IdSalle),
    CHECK (PrixRevient >= 0),
    CHECK (Stock >= 0)
);


CREATE TABLE Utilisateur (
    Email VARCHAR(100) PRIMARY KEY,
    CHECK (Email LIKE '%@%.%'),
    Nom VARCHAR(50) NOT NULL,
    Prenom VARCHAR(50) NOT NULL,
    AdressePostale VARCHAR(255) NOT NULL
);

CREATE TABLE Vente (
    IdVente INT PRIMARY KEY,
    PrixDepart INT NOT NULL,
    PrixActuel INT NOT NULL,
    DateVente VARCHAR(10) NOT NULL,
    Quantite INT NOT NULL,
    CHECK (DateVente LIKE '____-__-__'),
    HeureVente VARCHAR(8) NOT NULL,
    CHECK (HeureVente LIKE '__:__:__'),
    Duree INT, -- Durée en minutes
    CHECK (Duree = -1 or Duree > 0),
    IdSalle INT NOT NULL,
    IdProduit INT NOT NULL,
    FOREIGN KEY (IdSalle) REFERENCES SalleDeVente(IdSalle),
    FOREIGN KEY (IdProduit) REFERENCES Produit(IdProduit),
    CHECK (PrixDepart > 0),
    CHECK (PrixActuel > 0)

);

CREATE TABLE Caracteristiques (
    NomCar VARCHAR(50),
    ValeurCar VARCHAR(255) NOT NULL,
    IdProduit INT NOT NULL,
    FOREIGN KEY (IdProduit) REFERENCES Produit(IdProduit),
    PRIMARY KEY (NomCar, IdProduit)
);

CREATE TABLE Offre (
    PrixOffre INT NOT NULL,
    DateOffre DATE NOT NULL,
    HeureOffre INTERVAL DAY TO SECOND,
    Quantite INT NOT NULL,
    Email VARCHAR(100) NOT NULL,
    IdVente INT NOT NULL,
    PRIMARY KEY(IdVente, Email, DateOffre, HeureOffre),
    FOREIGN KEY (Email) REFERENCES Utilisateur(Email),
    FOREIGN KEY (IdVente) REFERENCES Vente(IdVente),
    CHECK (PrixOffre > 0),
    CHECK (Quantite >= 0)
);