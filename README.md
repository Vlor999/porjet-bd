# projet-BD


## Les Dossiers

- On range les table initiale dans table-idee. Il y aura toutes les tables et une fois qu'on se sera mit d'accord sur une table elle sera à la racine du projet.

## Lancement Adminer

- Pour lancer Admnier il suffit d'aller sur le site : 
    ```http://adminer.ensimag.fr```
    Puis se connecter en utilisant les identifiants suivants :
    - Utilisateur : ```<login>```
    - Mot de passe : ```<login>```
    - Base de donnée : ```<login>```
    - ici ```<login>``` est ```adnetw```

## To Do list

- [ ] Modélisation du porblème
    - [ ] Analyse du projet : propriété, dépendances et contraintes (valeurs, multiplicité et contextuelles)
    - [ ] Schéma Entités/Associations re présenatnt les données nécessaires à l'applicaiotn et leurs liens sémantiques
    - [ ] Vérifiactions liens entre Entités/Associations et analyse. Justification dans la docu.

- [ ] Implémentation de la BD
    - [ ] Traduire le schéma en un schéma relationnel. Décrivant les noms des relations, les noms et types des attributs et contraintes à vérifier. Justifier les choix de traduction et explicaiton des points difficiles. 
    - [ ] Forme normale de chacune des relations obtenus avec justification.
    - [ ] implémantation sur le SGBD Oracle : serveur ```oracle1```. Quantité élevé de donnée nécessaire pour la suite.
        - Connexion à la base de donnée : 
        ```bash
        sqlplus <login>@oracle1
        ```
        puis le mot de passe est :
        ```bash
        <login>
        ```

- [ ] Analyse fonctionnalités
    Définir les requêtes SQL2 nécessaire pour réaliser les fonctionnalités suivantes en les regourpant en transactions, ce qui permettra d'assurer la cohérence globale de la base de données, même en cas d'accès concurrents.
    - [ ] Mise en palce d'une Salle de Vente et la sélection de porduits déjà disponibles à la vente et permettant le choix d'enchères et du prix de départ.
    - [ ] L’enchère faite par un utilisateur sur un produit mis en vente dans une Salle de Vente.
    - [ ] Le processus de fin d’enchère déterminant le (ou les) utilisateur(s) ayant remporté une vente, en tenant compte du type d'enchères bien sûr
    - [ ] Test sur Oracle (SQL*Plus) des commandes y compris sur les appels concurrents. **Pas adminer** 

- [ ] Implantation des fonctionnalités (JAVA)
    - [ ] Les fonctionnalités précédemment étudiées devront être implantées dans un démonstrateur programmé en Java/JDBC. Simple : pas d'IG

- [ ] Livrable
    - [ ] Documentation du porjet (PDF) :
        - Analyse du porblème : DF, valeur, multiplicité, contextuelles
        - Schéma Entités/Associations en UML (outil Dia)
        - Traduction en schéma relationnel (SQL2) avec commentaires
        - Mode d'emploi du démonstrateur
    - [ ] Sources JAVA et SQL2 (ZIP) : 
        Script SQL2 permettant de créer le schéma relationnel et de peupler la base de données, fonctionnalités (squellettes transactionnels) et démonstrateur JAVA
    - [ ] SUpports de soutenance : 
        - [ ] Slides de présentation (15 min)