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
}