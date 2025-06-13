package GestionFinance;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Rapport {

    private List<Revenue> revenus;
    private List<Depense> depenses;

    public Rapport(List<Revenue> revenus, List<Depense> depenses) {
        this.revenus = revenus;
        this.depenses = depenses;
    }

    public String generateMonthlyReport(int month, int year) {
        String report = "--- Monthly Financial Report - " + month + "/" + year + " ---\n\n";

        List<Revenue> monthlyRevenues = revenus.stream()
                .filter(r -> r.getDateOperation().getMonthValue() == month && r.getDateOperation().getYear() == year)
                .collect(Collectors.toList());

        List<Depense> monthlyExpenses = depenses.stream()
                .filter(d -> d.getDateOperation().getMonthValue() == month && d.getDateOperation().getYear() == year)
                .collect(Collectors.toList());

        report += "--- Revenues ---\n";
        if (monthlyRevenues.isEmpty()) {
            report += "No revenues for this month.\n";
        } else {
            for (Revenue r : monthlyRevenues) {
                report += "  - " + r.getDescription() + ": " + r.getMontant() + "\n";
            }
        }
        report += "\n";

        report += "--- Expenses ---\n";
        if (monthlyExpenses.isEmpty()) {
            report += "No expenses for this month.\n";
        } else {
            for (Depense d : monthlyExpenses) {
                report += "  - " + d.getDescription() + " (" + (d.getCategorie() != null ? d.getCategorie().getName() : "No Category") + "): " + d.getMontant() + "\n";
            }
        }
        report += "\n";

        double totalRevenue = monthlyRevenues.stream().mapToDouble(GestionDepense::getMontant).sum();
        double totalExpense = monthlyExpenses.stream().mapToDouble(GestionDepense::getMontant).sum();

        report += "--- Summary ---\n" + String.format("Total Revenue: %.2f\n", totalRevenue) +
                  String.format("Total Expense: %.2f\n", totalExpense) +
                  String.format("Net Balance: %.2f\n", totalRevenue - totalExpense) +
                  "----------------------------------------------\n";

        return report;
    }
}