-- Création des tables

DROP TABLE Offre CASCADE CONSTRAINTS;
DROP TABLE Vente CASCADE CONSTRAINTS;
DROP TABLE SalleDeVente CASCADE CONSTRAINTS;
DROP TABLE Produit CASCADE CONSTRAINTS;
DROP TABLE Caracteristiques CASCADE CONSTRAINTS;
DROP TABLE Categorie CASCADE CONSTRAINTS;
DROP TABLE Utilisateur CASCADE CONSTRAINTS;



CREATE TABLE Categorie (
    NomCat VARCHAR(50) PRIMARY KEY,
    DescrCat TEXT NOT NULL
);

CREATE TABLE Produit (
    IdProduit INTEGER PRIMARY KEY,
    NomProduit VARCHAR(100) NOT NULL,
    PrixRevient INTEGER NOT NULL,
    Stock INTEGER NOT NULL,
    DispoProduit BOOLEAN NOT NULL,
    NomCat VARCHAR(50) NOT NULL,
    FOREIGN KEY (NomCat) REFERENCES Categorie(NomCat),
    CONSTRAINT check_prix_revient CHECK (PrixRevient > 0),
    CONSTRAINT check_stock CHECK (Stock >= 0)
);

CREATE TABLE SalleDeVente (
    IdSalle INTEGER PRIMARY KEY,
    Est_montante BOOLEAN NOT NULL,
    Est_Occupee BOOLEAN NOT NULL,
    Revocable BOOLEAN NOT NULL,
    LimiteOffres INTEGER,
    TypeDuree VARCHAR(20) NOT NULL,
    Categorie VARCHAR(50) NOT NULL,
    FOREIGN KEY (Categorie) REFERENCES Categorie(NomCat),
    CONSTRAINT check_limite_offres CHECK (LimiteOffres IS NULL OR LimiteOffres > 0),
    CONSTRAINT check_type_duree CHECK (TypeDuree IN ('limitee', 'libre'))
);

CREATE TABLE Utilisateur (
    Email VARCHAR(100) PRIMARY KEY,
    Nom VARCHAR(50) NOT NULL,
    Prenom VARCHAR(50) NOT NULL,
    AdressePostale TEXT NOT NULL
);

CREATE TABLE Vente (
    IdVente INTEGER PRIMARY KEY,
    PrixDepart INTEGER NOT NULL,
    PrixActuel INTEGER NOT NULL,
    Duree TIMESTAMP,
    IdSalle INTEGER NOT NULL,
    FOREIGN KEY (IdSalle) REFERENCES SalleDeVente(IdSalle),
    CONSTRAINT check_prix_depart CHECK (PrixDepart > 0),
    CONSTRAINT check_prix_actuel CHECK (PrixActuel > 0)
);

CREATE TABLE Caracteristiques (
    NomCar VARCHAR(50),
    ValeurCar TEXT NOT NULL,
    IdProduit INTEGER NOT NULL,
    PRIMARY KEY (NomCar, IdProduit),
    FOREIGN KEY (IdProduit) REFERENCES Produit(IdProduit)
);

CREATE TABLE Offre (
    PrixOffre INTEGER NOT NULL,
    DateOffre DATE NOT NULL,
    HeureOffre TIME NOT NULL,
    Quantite INTEGER NOT NULL,
    Email VARCHAR(100) NOT NULL,
    IdVente INTEGER NOT NULL,
    IdProduit INTEGER NOT NULL,
    PRIMARY KEY(IdVente, IdProduit, Email),
    FOREIGN KEY (Email) REFERENCES Utilisateur(Email),
    FOREIGN KEY (IdVente) REFERENCES Vente(IdVente),
    FOREIGN KEY (IdProduit) REFERENCES Produit(IdProduit),
    CONSTRAINT check_prix_offre CHECK (PrixOffre > 0),
    CONSTRAINT check_quantite CHECK (Quantite >= 0)
);