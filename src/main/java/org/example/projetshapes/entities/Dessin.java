package org.example.projetshapes.entities;

import java.time.LocalDateTime;

public class Dessin {
        private int id;
        private String nom;
        private LocalDateTime dateCreation;
        private int nbShapes;

    public Dessin(int id, String nom, LocalDateTime dateCreation, int nbShapes) {
        this.id = id;
        this.nom = nom;
        this.dateCreation = dateCreation;
        this.nbShapes = nbShapes;
    }

    public Dessin() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public int getNbShapes() {
        return nbShapes;
    }

    public void setNbShapes(int nbShapes) {
        this.nbShapes = nbShapes;
    }

    public int getIdDessin() {
        return this.id;
    }
}
