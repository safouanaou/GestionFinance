package GestionFinance;

import java.time.LocalDate;

/**
 * Classe représentant une dépense dans le système de gestion financière.
 * Hérite de GestionDepense pour les fonctionnalités de base.
 * Ajoute la gestion des catégories de dépenses.
 */
public class Depense extends GestionDepense {

    /** Catégorie de la dépense (ex: alimentation, transport, etc.) */
    private CategorieDepense categorie;

    /**
     * Constructeur pour créer une nouvelle dépense
     * @param description Description de la dépense
     * @param dateOperation Date de la dépense
     * @param montant Montant de la dépense
     * @param categorie Catégorie de la dépense
     * @throws IllegalArgumentException si la catégorie est null
     */
    public Depense(String description, LocalDate dateOperation, double montant, CategorieDepense categorie) {
        super(description, dateOperation, montant);
        if (categorie == null) {
            throw new IllegalArgumentException("La catégorie ne doit pas être nulle");
        }
        this.categorie = categorie;
    }

    /**
     * Récupère la catégorie de la dépense
     * @return La catégorie de la dépense
     */
    public CategorieDepense getCategorie() {
        return categorie;
    }

    /**
     * Modifie la catégorie de la dépense
     * @param categorie Nouvelle catégorie
     * @throws IllegalArgumentException si la catégorie est null
     */
    public void setCategorie(CategorieDepense categorie) {
        if (categorie == null) {
            throw new IllegalArgumentException("La catégorie ne doit pas être nulle");
        }
        this.categorie = categorie;
    }
}