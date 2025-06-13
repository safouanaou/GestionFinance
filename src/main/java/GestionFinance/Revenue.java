package GestionFinance;
import java.time.LocalDate;
import java.util.List;

/**
 * Classe représentant un revenu dans le système de gestion financière.
 * Hérite de GestionDepense pour les fonctionnalités de base.
 */
public class Revenue extends GestionDepense {

    /** Liste des sources de revenu (ex: salaire, investissement, etc.) */
    private List<SourceRevenue> source;

    /**
     * Constructeur pour créer un nouveau revenu
     * @param description Description du revenu
     * @param dateOperation Date du revenu
     * @param montant Montant du revenu
     * @param source Liste des sources du revenu
     * @throws IllegalArgumentException si la liste des sources est null ou vide
     */
    public Revenue(String description, LocalDate dateOperation, double montant, List<SourceRevenue> source) {
        super(description, dateOperation, montant);
        if (source == null || source.isEmpty()) {
            throw new IllegalArgumentException("Source list must not be null or empty");
        }
        this.source = source;
    }

    /**
     * Récupère la liste des sources de revenu
     * @return La liste des sources de revenu
     */
    public List<SourceRevenue> getSource() {
        return source;
    }

    /**
     * Modifie la liste des sources de revenu
     * @param source Nouvelle liste des sources
     * @throws IllegalArgumentException si la liste des sources est null ou vide
     */
    public void setSource(List<SourceRevenue> source) {
        if (source == null || source.isEmpty()) {
            throw new IllegalArgumentException("Source list must not be null or empty");
        }
        this.source = source;
    }
}