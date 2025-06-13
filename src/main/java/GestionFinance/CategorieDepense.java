package GestionFinance;
import java.io.Serializable;

/**
 * Classe représentant une catégorie de dépense.
 * Implémente Serializable pour permettre la sauvegarde des données.
 */
public class CategorieDepense implements Serializable {

    /** Nom de la catégorie */
    private String name;
    /** Description de la catégorie */
    private String description;

    /**
     * Constructeur pour créer une nouvelle catégorie de dépense
     * @param name Nom de la catégorie
     * @param description Description de la catégorie
     * @throws IllegalArgumentException si le nom ou la description est vide
     */
    public CategorieDepense(String name, String description) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name must not be null or empty");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description must not be null or empty");
        }
        this.name = name;
        this.description = description;
    }

    /**
     * Récupère le nom de la catégorie
     * @return Le nom de la catégorie
     */
    public String getName() {
        return name;
    }

    /**
     * Récupère la description de la catégorie
     * @return La description de la catégorie
     */
    public String getDescription() {
        return description;
    }
}