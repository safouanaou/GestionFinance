package GestionFinance;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Rapport {

private List<Revenue> allRevenues;
    private List<Depense> allExpenses;

    public Rapport(List<Revenue> revenus, List<Depense> depenses) {
        this.allRevenues = revenus;
        this.allExpenses = depenses;
    }

    /**
     * Filters and returns a list of revenues for a specific month and year.
     * @param month The month (1-12).
     * @param year The year.
     * @return A list of matching Revenue objects.
     */
    public List<Revenue> getMonthlyRevenues(int month, int year) {
        return allRevenues.stream()
                .filter(r -> r.getDateOperation().getMonthValue() == month && r.getDateOperation().getYear() == year)
                .collect(Collectors.toList());
    }

    /**
     * Filters and returns a list of expenses for a specific month and year.
     * @param month The month (1-12).
     * @param year The year.
     * @return A list of matching Depense objects.
     */
    public List<Depense> getMonthlyExpenses(int month, int year) {
        return allExpenses.stream()
                .filter(d -> d.getDateOperation().getMonthValue() == month && d.getDateOperation().getYear() == year)
                .collect(Collectors.toList());
    }

    /**
     * Calculates the sum of amounts for a given list of revenues.
     * @param revenues The list of revenues.
     * @return The total sum.
     */
    public double calculateTotalRevenue(List<Revenue> revenues) {
        return revenues.stream().mapToDouble(GestionDepense::getMontant).sum();
    }

    /**
     * Calculates the sum of amounts for a given list of expenses.
     * @param expenses The list of expenses.
     * @return The total sum.
     */
    public double calculateTotalExpense(List<Depense> expenses) {
        return expenses.stream().mapToDouble(GestionDepense::getMontant).sum();
    }

    // This is the original method from your file, kept for completeness.
    public String generateMonthlyReport(int month, int year) {
        String report = "--- Monthly Financial Report - " + month + "/" + year + " ---\n\n";

        List<Revenue> monthlyRevenues = getMonthlyRevenues(month, year);
        List<Depense> monthlyExpenses = getMonthlyExpenses(month, year);
        
        // ... rest of the original method logic ...

        return report;
    }
}