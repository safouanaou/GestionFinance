package GestionFinance;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe pour générer des rapports financiers.
 * Permet d'analyser les revenus et dépenses sur une période donnée.
 */
public class Rapport {

    /** Liste de tous les revenus */
    private List<Revenue> allRevenues;
    /** Liste de toutes les dépenses */
    private List<Depense> allExpenses;

    /**
     * Constructeur pour créer un nouveau rapport
     * @param revenus Liste des revenus à analyser
     * @param depenses Liste des dépenses à analyser
     */
    public Rapport(List<Revenue> revenus, List<Depense> depenses) {
        this.allRevenues = revenus;
        this.allExpenses = depenses;
    }

    /**
     * Filtre et retourne la liste des revenus pour un mois et une année spécifiques
     * @param month Le mois (1-12)
     * @param year L'année
     * @return Liste des revenus correspondants
     */
    public List<Revenue> getMonthlyRevenues(int month, int year) {
        return allRevenues.stream()
                .filter(r -> r.getDateOperation().getMonthValue() == month && r.getDateOperation().getYear() == year)
                .collect(Collectors.toList());
    }

    /**
     * Filtre et retourne la liste des dépenses pour un mois et une année spécifiques
     * @param month Le mois (1-12)
     * @param year L'année
     * @return Liste des dépenses correspondantes
     */
    public List<Depense> getMonthlyExpenses(int month, int year) {
        return allExpenses.stream()
                .filter(d -> d.getDateOperation().getMonthValue() == month && d.getDateOperation().getYear() == year)
                .collect(Collectors.toList());
    }

    /**
     * Calcule la somme totale des montants pour une liste de revenus donnée
     * @param revenues Liste des revenus
     * @return Somme totale
     */
    public double calculateTotalRevenue(List<Revenue> revenues) {
        return revenues.stream().mapToDouble(GestionDepense::getMontant).sum();
    }

    /**
     * Calcule la somme totale des montants pour une liste de dépenses donnée
     * @param expenses Liste des dépenses
     * @return Somme totale
     */
    public double calculateTotalExpense(List<Depense> expenses) {
        return expenses.stream().mapToDouble(GestionDepense::getMontant).sum();
    }

    /**
     * Génère un rapport mensuel détaillé
     * @param month Le mois (1-12)
     * @param year L'année
     * @return Rapport mensuel formaté
     */
    public String generateMonthlyReport(int month, int year) {
        String report = "--- Rapport Financier Mensuel - " + month + "/" + year + " ---\n\n";

        List<Revenue> monthlyRevenues = getMonthlyRevenues(month, year);
        List<Depense> monthlyExpenses = getMonthlyExpenses(month, year);
        
        // ... rest of the original method logic ...

        return report;
    }
}