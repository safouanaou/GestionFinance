package GestionFinance;
import java.io.Serializable;

/**
 * Classe représentant une source de revenu.
 * Implémente Serializable pour permettre la sauvegarde des données.
 * Utilisée pour classer et organiser les revenus.
 */
public class SourceRevenue implements Serializable {

    /** Nom de la source (ex: salaire, freelance, etc.) */
    private String nom;

    /**
     * Constructeur pour créer une nouvelle source de revenu
     * @param nom Nom de la source
     * @throws IllegalArgumentException si le nom est vide
     */
    public SourceRevenue(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne doit pas être vide");
        }
        this.nom = nom;
    }

    /**
     * Récupère le nom de la source
     * @return Le nom de la source
     */
    public String getNom() {
        return nom;
    }

    @Override
    public String toString() {
        return this.nom;
    }
}
