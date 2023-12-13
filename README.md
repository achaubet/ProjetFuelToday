demander GPS utilisateur  

Crée la table de préférence suivante pour persister les préférence utilisateur  

CREATE TABLE PREFERENCE (  
   ID_PREF PRIMARY KEY ,  
   RAYON INT,  
   TYPE_ESSENCE TEXT,  
   SERVICE_PREF TEXT,
  ORDRE_AFFICHE TEXT   
);  


Format similaire à ça pour les paramètres et pour l'affichage des stations (nouvelle activité via liste view sans les 3 boutons en haut pour station) (nouvelle activité coder en "dur" pour les paramètres)    
<img src="https://github.com/achaubet/ProjetFuelToday/assets/90894382/4d5c68eb-ea74-4eb2-9ea2-11f2a6fe1dc9" height="500">
<img src="https://github.com/achaubet/ProjetFuelToday/assets/90894382/6e7959ea-155e-479a-96c6-b61ea14d8ef6" height="500">  

Quand on clique sur une station il faut afficher les différent service disponible  
  
