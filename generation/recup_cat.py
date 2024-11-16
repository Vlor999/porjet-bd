#! /usr/bin/env python3

def recup_categorie():
    FICHIER = open("../data/categories.sql")
    list_categorie = []
    print("Récupération : ...", end="")
    for ligne in FICHIER:
        ligne = ligne.strip()
        list_split_guillemet = ligne.split("'")
        cat = list_split_guillemet[1]
        list_categorie.append(cat)
    print("\rRécupération : Done")
    return list_categorie