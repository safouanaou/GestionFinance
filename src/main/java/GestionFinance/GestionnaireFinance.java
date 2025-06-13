package GestionFinance;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.*;
import java.util.stream.Stream;

/**
 * Classe principale de gestion des finances.
 * Gère toutes les opérations liées aux revenus et dépenses.
 */
public class GestionnaireFinance {

    /** Liste des revenus enregistrés */
    private List<Revenue> revenus;
    /** Liste des dépenses enregistrées */
    private List<Depense> depenses;
    /** Fichier de stockage des données */
    private static final String FICHIER_DONNEES = "finance.in";

    /**
     * Constructeur initialisant les listes de revenus et dépenses
     */
    public GestionnaireFinance() {
        this.revenus = new ArrayList<>();
        this.depenses = new ArrayList<>();
    }

    /**
     * Ajoute un nouveau revenu
     * @param description Description du revenu
     * @param dateOperation Date du revenu
     * @param montant Montant du revenu
     * @param source Sources du revenu
     */
    public void ajouterRevenu(String description, LocalDate dateOperation, double montant, List<SourceRevenue> source) {
        Revenue nouveauRevenu = new Revenue(description, dateOperation, montant, source);
        this.revenus.add(nouveauRevenu);
    }

    /**
     * Ajoute une nouvelle dépense
     * @param description Description de la dépense
     * @param dateOperation Date de la dépense
     * @param montant Montant de la dépense
     * @param categorie Catégorie de la dépense
     */
    public void ajouterDepense(String description, LocalDate dateOperation, double montant, CategorieDepense categorie) {
        Depense nouvelleDepense = new Depense(description, dateOperation, montant, categorie);
        this.depenses.add(nouvelleDepense);
    }

    /**
     * Récupère tous les revenus
     * @return Copie de la liste des revenus
     */
    public List<Revenue> obtenirTousRevenus() {
        return new ArrayList<>(this.revenus);
    }

    /**
     * Récupère toutes les dépenses
     * @return Copie de la liste des dépenses
     */
    public List<Depense> obtenirToutesDepenses() {
        return new ArrayList<>(this.depenses);
    }

    /**
     * Récupère les revenus d'un mois spécifique
     * @param mois Mois cible
     * @param annee Année cible
     * @return Liste des revenus du mois spécifié
     */
    public List<Revenue> obtenirRevenusParMois(int mois, int annee) {
        return revenus.stream()
                .filter(r -> r.getDateOperation().getMonthValue() == mois && r.getDateOperation().getYear() == annee)
                .collect(Collectors.toList());
    }

    /**
     * Récupère les dépenses d'un mois spécifique
     * @param mois Mois cible
     * @param annee Année cible
     * @return Liste des dépenses du mois spécifié
     */
    public List<Depense> obtenirDepensesParMois(int mois, int annee) {
        return depenses.stream()
                .filter(d -> d.getDateOperation().getMonthValue() == mois && d.getDateOperation().getYear() == annee)
                .collect(Collectors.toList());
    }

    /**
     * Supprime un revenu par son ID
     * @param id ID du revenu à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean supprimerRevenuParId(int id) {
        return this.revenus.removeIf(r -> r.getId() == id);
    }

    /**
     * Supprime une dépense par son ID
     * @param id ID de la dépense à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean supprimerDepenseParId(int id) {
        return this.depenses.removeIf(d -> d.getId() == id);
    }

    /**
     * Modifie une dépense existante
     * @param id ID de la dépense à modifier
     * @param nouvelleDescription Nouvelle description
     * @param nouvelleDate Nouvelle date
     * @param nouveauMontant Nouveau montant
     * @param nouvelleCategorie Nouvelle catégorie
     * @return true si la modification a réussi, false sinon
     */
    public boolean modifierDepense(int id, String nouvelleDescription, java.time.LocalDate nouvelleDate, double nouveauMontant, CategorieDepense nouvelleCategorie) {
        for (Depense d : depenses) {
            if (d.getId() == id) {
                d.setDescription(nouvelleDescription);
                d.setDateOperation(nouvelleDate);
                d.setMontant(nouveauMontant);
                d.setCategorie(nouvelleCategorie);
                return true;
            }
        }
        return false;
    }

    /**
     * Modifie un revenu existant
     * @param id ID du revenu à modifier
     * @param nouvelleDescription Nouvelle description
     * @param nouvelleDate Nouvelle date
     * @param nouveauMontant Nouveau montant
     * @param nouvellesSources Nouvelles sources
     * @return true si la modification a réussi, false sinon
     */
    public boolean modifierRevenue(int id, String nouvelleDescription, java.time.LocalDate nouvelleDate, double nouveauMontant, java.util.List<SourceRevenue> nouvellesSources) {
        for (Revenue r : revenus) {
            if (r.getId() == id) {
                r.setDescription(nouvelleDescription);
                r.setDateOperation(nouvelleDate);
                r.setMontant(nouveauMontant);
                r.setSources(nouvellesSources);
                return true;
            }
        }
        return false;
    }

    /**
     * Sauvegarde les données dans le fichier
     */
    public void sauvegarderDonnees() {
        try (ObjectOutputStream sortie = new ObjectOutputStream(new FileOutputStream(FICHIER_DONNEES))) {
            sortie.writeInt(revenus.size());
            for (Revenue revenu : revenus) {
                sortie.writeObject(revenu);
            }

            sortie.writeInt(depenses.size());
            for (Depense depense : depenses) {
                sortie.writeObject(depense);
            }
            System.out.println("Données sauvegardées avec succès dans " + FICHIER_DONNEES);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des données : " + e.getMessage());
        }
    }

    /**
     * Charge les données depuis le fichier
     */
    public void chargerDonnees() {
        try (ObjectInputStream entree = new ObjectInputStream(new FileInputStream(FICHIER_DONNEES))) {
            int nombreRevenus = entree.readInt();
            this.revenus.clear();
            for (int i = 0; i < nombreRevenus; i++) {
                revenus.add((Revenue) entree.readObject());
            }

            int nombreDepenses = entree.readInt();
            this.depenses.clear();
            for (int i = 0; i < nombreDepenses; i++) {
                depenses.add((Depense) entree.readObject());
            }
            System.out.println("Données chargées avec succès depuis " + FICHIER_DONNEES);
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Impossible de charger les données, démarrage à vide : " + e.getMessage());
            this.revenus = new ArrayList<>();
            this.depenses = new ArrayList<>();
        }

        int idMax = Stream.concat(revenus.stream(), depenses.stream())
                          .mapToInt(GestionDepense::getId)
                          .max()
                          .orElse(0);
        GestionDepense.setCounter(idMax);
    }
}

