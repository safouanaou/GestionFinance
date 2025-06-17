package GestionFinance;
import java.io.Serializable;

/**
 * Classe représentant une catégorie de dépense.
 * Implémente Serializable pour permettre la sauvegarde des données.
 * Utilisée pour classer et organiser les dépenses.
 */
public class CategorieDepense implements Serializable {

    /** Nom de la catégorie (ex: alimentation, transport, etc.) */
    private String nom;

    /**
     * Constructeur pour créer une nouvelle catégorie de dépense
     * @param nom Nom de la catégorie
     * @throws IllegalArgumentException si le nom est vide
     */
    public CategorieDepense(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne doit pas être vide");
        }
        this.nom = nom;
    }

    /**
     * Récupère le nom de la catégorie
     * @return Le nom de la catégorie
     */
    public String getNom() {
        return nom;
    }

    @Override
    public String toString() {
        return this.nom;
    }
}