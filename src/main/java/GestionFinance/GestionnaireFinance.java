package GestionFinance;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.util.stream.Collectors;

public class GestionnaireFinance {

    private List<Revenue> revenues;
    private List<Depense> depenses;

    public GestionnaireFinance() {
        this.revenues = new ArrayList<>();
        this.depenses = new ArrayList<>();
    }

    public void addRevenu(String description, LocalDate dateOperation, double montant, List<SourceRevenue> source) {
        Revenue newRevenu = new Revenue(description, dateOperation, montant, source);
        this.revenues.add(newRevenu);
    }

    public void addDepense(String description, LocalDate dateOperation, double montant, CategorieDepense categorie) {
        Depense newDepense = new Depense(description, dateOperation, montant, categorie);
        this.depenses.add(newDepense);
    }

    public List<Revenue> getAllRevenues() {
        return new ArrayList<>(this.revenues); // Return a copy to prevent external modification
    }

    public List<Depense> getAllDepenses() {
        return new ArrayList<>(this.depenses); // Return a copy to prevent external modification
    }

    public List<Revenue> getRevenuesByMonth(int month, int year) {
        return revenues.stream()
                .filter(r -> r.getDateOperation().getMonthValue() == month && r.getDateOperation().getYear() == year)
                .collect(Collectors.toList());
    }

    public List<Depense> getDepensesByMonth(int month, int year) {
        return depenses.stream()
                .filter(d -> d.getDateOperation().getMonthValue() == month && d.getDateOperation().getYear() == year)
                .collect(Collectors.toList());
    }

    public boolean removeRevenuById(int id) {
        return this.revenues.removeIf(r -> r.getId() == id);
    }

    public boolean removeDepenseById(int id) {
        return this.depenses.removeIf(d -> d.getId() == id);
    }
    
       /**
     * Finds a Depense by its ID and updates its properties.
     * @param id The ID of the expense to update.
     * @param newDescription The new description for the expense.
     * @param newDate The new date for the expense.
     * @param newMontant The new amount for the expense.
     * @param newCategorie The new category for the expense.
     * @return true if the update was successful, false if the ID was not found.
     */
    public boolean modifierDepense(int id, String newDescription, java.time.LocalDate newDate, double newMontant, CategorieDepense newCategorie) {
        for (Depense d : depenses) {
            if (d.getId() == id) {
                d.setDescription(newDescription);
                d.setDateOperation(newDate);
                d.setMontant(newMontant);
                d.setCategorie(newCategorie);
                return true; // Update successful
            }
        }
        return false; // ID not found
    }

    /**
     * Finds a Revenue by its ID and updates its properties.
     * @param id The ID of the revenue to update.
     * @param newDescription The new description for the revenue.
     * @param newDate The new date for the revenue.
     * @param newMontant The new amount for the revenue.
     * @param newSource The new source list for the revenue.
     * @return true if the update was successful, false if the ID was not found.
     */
    public boolean modifierRevenue(int id, String newDescription, java.time.LocalDate newDate, double newMontant, java.util.List<SourceRevenue> newSource) {
        for (Revenue r : revenues) {
            if (r.getId() == id) {
                r.setDescription(newDescription);
                r.setDateOperation(newDate);
                r.setMontant(newMontant);
                r.setSource(newSource);
                return true; // Update successful
            }
        }
        return false; // ID not found
    }
}