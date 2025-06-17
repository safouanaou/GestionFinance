package GestionFinance;
import java.time.LocalDate;
import java.io.Serializable;

/**
 * Classe de base pour la gestion des opérations financières.
 * Implémente Serializable pour permettre la sauvegarde des données.
 * Cette classe sert de base pour les classes Revenue et Depense.
 */
public abstract class GestionDepense implements Serializable {
    /** Identifiant unique de l'opération */
    private int id;
    /** Compteur pour générer des IDs uniques */
    private static int compteur = 0;
    /** Description de l'opération */
    private String description;
    /** Date de l'opération */
    private LocalDate dateOperation;
    /** Montant de l'opération */
    private double montant;

    /**
     * Constructeur pour créer une nouvelle opération financière
     * @param description Description de l'opération
     * @param dateOperation Date de l'opération
     * @param montant Montant de l'opération
     * @throws IllegalArgumentException si la description est vide, la date est null, ou le montant est négatif
     */
    public GestionDepense(String description, LocalDate dateOperation, double montant) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("La description ne doit pas être vide");
        }
        if (dateOperation == null) {
            throw new IllegalArgumentException("La date de l'opération ne doit pas être nulle");
        }
        if (montant < 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }
        this.id = ++compteur;
        this.description = description;
        this.dateOperation = dateOperation;
        this.montant = montant;
    }

    /**
     * Récupère l'identifiant unique de l'opération
     * @return L'identifiant unique
     */
    public int getId() {
        return id;
    }

    /**
     * Récupère la description de l'opération
     * @return La description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Récupère la date de l'opération
     * @return La date
     */
    public LocalDate getDateOperation() {
        return dateOperation;
    }

    /**
     * Récupère le montant de l'opération
     * @return Le montant
     */
    public double getMontant() {
        return montant;
    }

    /**
     * Modifie la description de l'opération
     * @param description Nouvelle description
     * @throws IllegalArgumentException si la description est vide
     */
    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("La description ne doit pas être vide");
        }
        this.description = description;
    }

    /**
     * Modifie la date de l'opération
     * @param dateOperation Nouvelle date
     * @throws IllegalArgumentException si la date est null
     */
    public void setDateOperation(LocalDate dateOperation) {
        if (dateOperation == null) {
            throw new IllegalArgumentException("La date de l'opération ne doit pas être nulle");
        }
        this.dateOperation = dateOperation;
    }

    /**
     * Modifie le montant de l'opération
     * @param montant Nouveau montant
     * @throws IllegalArgumentException si le montant est négatif
     */
    public void setMontant(double montant) {
        if (montant < 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }
        this.montant = montant;
    }
    
    /**
     * Modifie le compteur d'IDs
     * @param nouveauCompteur Nouvelle valeur du compteur
     */
    public static void setCounter(int nouveauCompteur) {
        compteur = nouveauCompteur;
    }
}