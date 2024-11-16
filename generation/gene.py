#! /usr/bin/env python3
import random
from recup_cat import recup_categorie

def ecriture_salle_vente():
    print("Début génération ...")
    FICHIER = "../data/liste_cat.txt"
    Style = """INSERT INTO "SALLE_DE_VENTE" ("IDSALLE", "CATEGORIE", "TYPEVENTE", "DESCRIPTION") VALUES ('0', 'Fruit', '1', 'Test première salle de vente')"""

    fichier_ecriture = open("../data/salle_vente.sql", "w")
    compteur = 1
    liste_cat = recup_categorie()
    for cat in liste_cat:
        val_alea = random.randint(0, 1)
        fichier_ecriture.write(f"""INSERT INTO SALLE_DE_VENTE (IDSALLE, CATEGORIE, TYPEVENTE, DESCRIPTION) VALUES ('{compteur}', '{cat}', '{val_alea}', '{compteur} : salle de vente de {cat}')\n""")
        compteur += 1
    fichier_ecriture.close()
    print("Génération : Done")

ecriture_salle_vente()