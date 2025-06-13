package GestionFinance;
import java.io.Serializable;

/**
 * Classe représentant une source de revenu.
 * Implémente Serializable pour permettre la sauvegarde des données.
 * Utilisée pour identifier l'origine des revenus.
 */
public class SourceRevenue implements Serializable {
    /** Nom de la source de revenu (ex: salaire, investissement, etc.) */
    private String nom;
    /** Description détaillée de la source de revenu */
    private String description;

    /**
     * Constructeur pour créer une nouvelle source de revenu
     * @param nom Nom de la source
     * @param description Description de la source
     * @throws IllegalArgumentException si le nom ou la description est vide
     */
    public SourceRevenue(String nom, String description) {
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
     * Récupère le nom de la source de revenu
     * @return Le nom de la source
     */
    public String getNom() {
        return nom;
    }

    /**
     * Récupère la description de la source de revenu
     * @return La description de la source
     */
    public String getDescription() {
        return description;
    }
}
