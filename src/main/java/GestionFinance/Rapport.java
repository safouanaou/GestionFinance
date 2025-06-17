package GestionFinance;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe pour générer des rapports financiers.
 * Permet d'analyser les revenus et dépenses sur une période donnée.
 * Fournit des méthodes pour filtrer et calculer les totaux des opérations financières.
 */
public class Rapport {

    /** Liste de tous les revenus à analyser */
    private List<Revenue> tousRevenus;
    /** Liste de toutes les dépenses à analyser */
    private List<Depense> toutesDepenses;

    /**
     * Constructeur pour créer un nouveau rapport
     * @param revenus Liste des revenus à analyser
     * @param depenses Liste des dépenses à analyser
     */
    public Rapport(List<Revenue> revenus, List<Depense> depenses) {
        this.tousRevenus = revenus;
        this.toutesDepenses = depenses;
    }

    /**
     * Filtre et retourne la liste des revenus pour un mois et une année spécifiques
     * @param mois Le mois (1-12)
     * @param annee L'année
     * @return Liste des revenus correspondants
     */
    public List<Revenue> getRevenuesMensuelle(int mois, int annee) {
        return tousRevenus.stream()
                .filter(r -> r.getDateOperation().getMonthValue() == mois && r.getDateOperation().getYear() == annee)
                .collect(Collectors.toList());
    }

    /**
     * Filtre et retourne la liste des dépenses pour un mois et une année spécifiques
     * @param mois Le mois (1-12)
     * @param annee L'année
     * @return Liste des dépenses correspondantes
     */
    public List<Depense> getDepensesMensuelle(int mois, int annee) {
        return toutesDepenses.stream()
                .filter(d -> d.getDateOperation().getMonthValue() == mois && d.getDateOperation().getYear() == annee)
                .collect(Collectors.toList());
    }

    /**
     * Calcule la somme totale des montants pour une liste de revenus donnée
     * @param revenus Liste des revenus
     * @return Somme totale des revenus
     */
    public double calculerTotalRevenue(List<Revenue> revenus) {
        return revenus.stream()
                .mapToDouble(Revenue::getMontant)
                .sum();
    }

    /**
     * Calcule la somme totale des montants pour une liste de dépenses donnée
     * @param depenses Liste des dépenses
     * @return Somme totale des dépenses
     */
    public double calculerTotalDepense(List<Depense> depenses) {
        return depenses.stream()
                .mapToDouble(Depense::getMontant)
                .sum();
    }

    /**
     * Génère un rapport mensuel détaillé
     * @param mois Le mois (1-12)
     * @param annee L'année
     * @return Rapport mensuel formaté
     */
    public String genererRapportMensuelle(int mois, int annee) {
        String rapport = "--- Rapport Financier Mensuel - " + mois + "/" + annee + " ---\n\n";

        List<Revenue> revenusMensuels = getRevenuesMensuelle(mois, annee);
        List<Depense> depensesMensuelles = getDepensesMensuelle(mois, annee);
        
        // ... rest of the original method logic ...

        return rapport;
    }
}