package GestionFinance;
import java.io.Serializable;

/**
 * Classe représentant une source de revenu.
 * Implémente Serializable pour permettre la sauvegarde des données.
 */
public class SourceRevenue implements Serializable {
    /** Nom de la source de revenu */
    private String name;
    /** Description de la source de revenu */
    private String description;

    /**
     * Constructeur pour créer une nouvelle source de revenu
     * @param name Nom de la source
     * @param description Description de la source
     * @throws IllegalArgumentException si le nom ou la description est vide
     */
    public SourceRevenue(String name, String description) {
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
     * Récupère le nom de la source de revenu
     * @return Le nom de la source
     */
    public String getName() {
        return name;
    }

    /**
     * Récupère la description de la source de revenu
     * @return La description de la source
     */
    public String getDescription() {
        return description;
    }
}
