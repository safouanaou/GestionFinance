package GestionFinance;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.io.*;
import java.util.stream.Stream;

public class GestionnaireFinance {

    private List<Revenue> revenues;
    private List<Depense> depenses;
    private static final String DATA_FILE = "finance.in"; // Use the .in extension

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


    public void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeInt(revenues.size());
            for (Revenue revenue : revenues) {
                out.writeObject(revenue);
            }

            out.writeInt(depenses.size());
            for (Depense depense : depenses) {
                out.writeObject(depense);
            }
            System.out.println("Data saved successfully to " + DATA_FILE);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    public void loadData() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            int revenueCount = in.readInt();
            this.revenues.clear();
            for (int i = 0; i < revenueCount; i++) {
                revenues.add((Revenue) in.readObject());
            }

            int depenseCount = in.readInt();
            this.depenses.clear();
            for (int i = 0; i < depenseCount; i++) {
                depenses.add((Depense) in.readObject());
            }
            System.out.println("Data loaded successfully from " + DATA_FILE);
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Could not load data, starting fresh: " + e.getMessage());
            this.revenues = new ArrayList<>();
            this.depenses = new ArrayList<>();
        }

        int maxId = Stream.concat(revenues.stream(), depenses.stream())
                          .mapToInt(GestionDepense::getId)
                          .max()
                          .orElse(0);
        GestionDepense.setCounter(maxId);
    }

}

