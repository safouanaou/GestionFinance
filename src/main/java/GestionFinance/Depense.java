package GestionFinance;

import java.time.LocalDate;

public class Depense extends GestionDepense {

    private CategorieDepense categorie;

    public Depense(String description, LocalDate dateOperation, double montant, CategorieDepense categorie) {
        super(description, dateOperation, montant);
        if (categorie == null) {
            throw new IllegalArgumentException("CategorieDepense must not be null");
        }
        this.categorie = categorie;
    }

    public CategorieDepense getCategorie() {
        return categorie;
    }

    public void setCategorie(CategorieDepense categorie) {
        if (categorie == null) {
            throw new IllegalArgumentException("CategorieDepense must not be null");
        }
        this.categorie = categorie;
    }
}