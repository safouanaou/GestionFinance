package GestionFinance;
import java.time.LocalDate;
import java.util.List;

/**
 * Classe représentant un revenu dans le système de gestion financière.
 * Hérite de GestionDepense pour les fonctionnalités de base.
 * Ajoute la gestion des sources de revenus.
 */
public class Revenue extends GestionDepense {

    /** Liste des sources de revenu (ex: salaire, investissement, etc.) */
    private List<SourceRevenue> sources;

    /**
     * Constructeur pour créer un nouveau revenu
     * @param description Description du revenu
     * @param dateOperation Date du revenu
     * @param montant Montant du revenu
     * @param sources Liste des sources du revenu
     * @throws IllegalArgumentException si la liste des sources est null ou vide
     */
    public Revenue(String description, LocalDate dateOperation, double montant, List<SourceRevenue> sources) {
        super(description, dateOperation, montant);
        if (sources == null || sources.isEmpty()) {
            throw new IllegalArgumentException("La liste des sources ne doit pas être vide");
        }
        this.sources = sources;
    }

    /**
     * Récupère la liste des sources de revenu
     * @return La liste des sources de revenu
     */
    public List<SourceRevenue> getSources() {
        return sources;
    }

    /**
     * Modifie la liste des sources de revenu
     * @param sources Nouvelle liste des sources
     * @throws IllegalArgumentException si la liste des sources est null ou vide
     */
    public void setSources(List<SourceRevenue> sources) {
        if (sources == null || sources.isEmpty()) {
            throw new IllegalArgumentException("La liste des sources ne doit pas être vide");
        }
        this.sources = sources;
    }
}