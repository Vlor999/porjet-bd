CREATE TABLE Categorie (
    NomCat VARCHAR(50) PRIMARY KEY,
    DescrCat TEXT NOT NULL
);

CREATE TABLE Produit (
    IdProduit INTEGER PRIMARY KEY,
    NomProduit VARCHAR(100) NOT NULL,
    PrixRevient DECIMAL(10,2) NOT NULL,
    Stock INTEGER NOT NULL,
    NomCat VARCHAR(50) NOT NULL,
    FOREIGN KEY (NomCat) REFERENCES Categorie(NomCat),
    CONSTRAINT check_prix_revient CHECK (PrixRevient > 0),
    CONSTRAINT check_stock CHECK (Stock >= 0)
);

CREATE TABLE SalleDeVente (
    IdSalle INTEGER PRIMARY KEY,
    Categorie VARCHAR(50) NOT NULL,
    FOREIGN KEY (Categorie) REFERENCES Categorie(NomCat)
);

CREATE TABLE Utilisateur (
    Email VARCHAR(100) PRIMARY KEY,
    Nom VARCHAR(50) NOT NULL,
    Prenom VARCHAR(50) NOT NULL,
    AdressePostale TEXT NOT NULL
);

CREATE TABLE Vente (
    IdVente INTEGER PRIMARY KEY,
    TypeVente VARCHAR(20) NOT NULL,
    PrixDepart DECIMAL(10,2) NOT NULL,
    Revocable BOOLEAN NOT NULL,
    LimiteOffres INTEGER,
    Duree VARCHAR(20) NOT NULL,
    DateFinVente TIMESTAMP,
    IdProduit INTEGER NOT NULL,
    IdSalle INTEGER NOT NULL,
    FOREIGN KEY (IdProduit) REFERENCES Produit(IdProduit),
    FOREIGN KEY (IdSalle) REFERENCES SalleDeVente(IdSalle),
    CONSTRAINT check_type_vente CHECK (TypeVente IN ('montante', 'descendante')),
    CONSTRAINT check_prix_depart CHECK (PrixDepart > 0),
    CONSTRAINT check_limite_offres CHECK (LimiteOffres IS NULL OR LimiteOffres >= 0),
    CONSTRAINT check_duree CHECK (Duree IN ('limitee', 'libre')),
    CONSTRAINT check_date_fin CHECK (
        (Duree = 'limitee' AND DateFinVente IS NOT NULL) OR
        (Duree = 'libre' AND DateFinVente IS NULL)
    )
);

CREATE TABLE Caracteristiques (
    NomCar VARCHAR(50),
    ValeurCar TEXT NOT NULL,
    IdProduit INTEGER NOT NULL,
    PRIMARY KEY (NomCar, IdProduit),
    FOREIGN KEY (IdProduit) REFERENCES Produit(IdProduit)
);

CREATE TABLE Offre (
    IdOffre INTEGER PRIMARY KEY,
    PrixOffre DECIMAL(10,2) NOT NULL,
    DateOffre DATE NOT NULL,
    HeureOffre TIME NOT NULL,
    Quantite INTEGER NOT NULL,
    Email VARCHAR(100) NOT NULL,
    IdVente INTEGER NOT NULL,
    FOREIGN KEY (Email) REFERENCES Utilisateur(Email),
    FOREIGN KEY (IdVente) REFERENCES Vente(IdVente),
    CONSTRAINT check_prix_offre CHECK (PrixOffre > 0),
    CONSTRAINT check_quantite CHECK (Quantite >= 0)
);