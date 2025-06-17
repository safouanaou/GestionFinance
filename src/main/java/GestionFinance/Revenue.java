package GestionFinance;
import java.time.LocalDate;

/**
 * Classe représentant un revenu dans le système de gestion financière.
 * Hérite de GestionDepense pour les fonctionnalités de base.
 * Ajoute la gestion des sources de revenus.
 */
public class Revenue extends GestionDepense {

    /** Liste des sources de revenu (ex: salaire, investissement, etc.) */
    private SourceRevenue source;

    /**
     * Constructeur pour créer un nouveau revenu
     * @param description Description du revenu
     * @param dateOperation Date du revenu
     * @param montant Montant du revenu
     * @param source Source du revenu
     * @throws IllegalArgumentException si la source est null
     */
    public Revenue(String description, LocalDate dateOperation, double montant, SourceRevenue source) {
        super(description, dateOperation, montant);
        if (source == null) {
            throw new IllegalArgumentException("La source ne doit pas être nulle");
        }
        this.source = source;
    }

    /**
     * Récupère la source de revenu
     * @return La source de revenu
     */
    public SourceRevenue getSource() {
        return source;
    }

    /**
     * Modifie la source de revenu
     * @param source Nouvelle source
     * @throws IllegalArgumentException si la source est null
     */
    public void setSource(SourceRevenue source) {
        if (source == null) {
            throw new IllegalArgumentException("La source ne doit pas être nulle");
        }
        this.source = source;
    }
}