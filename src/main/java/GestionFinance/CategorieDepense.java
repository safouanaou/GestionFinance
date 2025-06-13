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
    /** Description détaillée de la catégorie */
    private String description;

    /**
     * Constructeur pour créer une nouvelle catégorie de dépense
     * @param nom Nom de la catégorie
     * @param description Description de la catégorie
     * @throws IllegalArgumentException si le nom ou la description est vide
     */
    public CategorieDepense(String nom, String description) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne doit pas être vide");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("La description ne doit pas être vide");
        }
        this.nom = nom;
        this.description = description;
    }

    /**
     * Récupère le nom de la catégorie
     * @return Le nom de la catégorie
     */
    public String getNom() {
        return nom;
    }

    /**
     * Récupère la description de la catégorie
     * @return La description de la catégorie
     */
    public String getDescription() {
        return description;
    }
}