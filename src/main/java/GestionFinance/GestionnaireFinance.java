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

/**
 * Classe principale de gestion des finances.
 * Gère toutes les opérations liées aux revenus et dépenses.
 */
public class GestionnaireFinance {

    /** Liste des revenus enregistrés */
    private List<Revenue> revenues;
    /** Liste des dépenses enregistrées */
    private List<Depense> depenses;
    /** Fichier de stockage des données */
    private static final String DATA_FILE = "finance.in";

    /**
     * Constructeur initialisant les listes de revenus et dépenses
     */
    public GestionnaireFinance() {
        this.revenues = new ArrayList<>();
        this.depenses = new ArrayList<>();
    }

    /**
     * Ajoute un nouveau revenu
     * @param description Description du revenu
     * @param dateOperation Date du revenu
     * @param montant Montant du revenu
     * @param source Sources du revenu
     */
    public void addRevenu(String description, LocalDate dateOperation, double montant, List<SourceRevenue> source) {
        Revenue newRevenu = new Revenue(description, dateOperation, montant, source);
        this.revenues.add(newRevenu);
    }

    /**
     * Ajoute une nouvelle dépense
     * @param description Description de la dépense
     * @param dateOperation Date de la dépense
     * @param montant Montant de la dépense
     * @param categorie Catégorie de la dépense
     */
    public void addDepense(String description, LocalDate dateOperation, double montant, CategorieDepense categorie) {
        Depense newDepense = new Depense(description, dateOperation, montant, categorie);
        this.depenses.add(newDepense);
    }

    /**
     * Récupère tous les revenus
     * @return Copie de la liste des revenus
     */
    public List<Revenue> getAllRevenues() {
        return new ArrayList<>(this.revenues);
    }

    /**
     * Récupère toutes les dépenses
     * @return Copie de la liste des dépenses
     */
    public List<Depense> getAllDepenses() {
        return new ArrayList<>(this.depenses);
    }

    /**
     * Récupère les revenus d'un mois spécifique
     * @param month Mois cible
     * @param year Année cible
     * @return Liste des revenus du mois spécifié
     */
    public List<Revenue> getRevenuesByMonth(int month, int year) {
        return revenues.stream()
                .filter(r -> r.getDateOperation().getMonthValue() == month && r.getDateOperation().getYear() == year)
                .collect(Collectors.toList());
    }

    /**
     * Récupère les dépenses d'un mois spécifique
     * @param month Mois cible
     * @param year Année cible
     * @return Liste des dépenses du mois spécifié
     */
    public List<Depense> getDepensesByMonth(int month, int year) {
        return depenses.stream()
                .filter(d -> d.getDateOperation().getMonthValue() == month && d.getDateOperation().getYear() == year)
                .collect(Collectors.toList());
    }

    /**
     * Supprime un revenu par son ID
     * @param id ID du revenu à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean removeRevenuById(int id) {
        return this.revenues.removeIf(r -> r.getId() == id);
    }

    /**
     * Supprime une dépense par son ID
     * @param id ID de la dépense à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean removeDepenseById(int id) {
        return this.depenses.removeIf(d -> d.getId() == id);
    }

    /**
     * Modifie une dépense existante
     * @param id ID de la dépense à modifier
     * @param newDescription Nouvelle description
     * @param newDate Nouvelle date
     * @param newMontant Nouveau montant
     * @param newCategorie Nouvelle catégorie
     * @return true si la modification a réussi, false sinon
     */
    public boolean modifierDepense(int id, String newDescription, java.time.LocalDate newDate, double newMontant, CategorieDepense newCategorie) {
        for (Depense d : depenses) {
            if (d.getId() == id) {
                d.setDescription(newDescription);
                d.setDateOperation(newDate);
                d.setMontant(newMontant);
                d.setCategorie(newCategorie);
                return true;
            }
        }
        return false;
    }

    /**
     * Modifie un revenu existant
     * @param id ID du revenu à modifier
     * @param newDescription Nouvelle description
     * @param newDate Nouvelle date
     * @param newMontant Nouveau montant
     * @param newSource Nouvelles sources
     * @return true si la modification a réussi, false sinon
     */
    public boolean modifierRevenue(int id, String newDescription, java.time.LocalDate newDate, double newMontant, java.util.List<SourceRevenue> newSource) {
        for (Revenue r : revenues) {
            if (r.getId() == id) {
                r.setDescription(newDescription);
                r.setDateOperation(newDate);
                r.setMontant(newMontant);
                r.setSource(newSource);
                return true;
            }
        }
        return false;
    }

    /**
     * Sauvegarde les données dans le fichier
     */
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

    /**
     * Charge les données depuis le fichier
     */
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

