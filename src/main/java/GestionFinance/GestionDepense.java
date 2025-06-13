package GestionFinance;
import java.time.LocalDate;

public class GestionDepense {
    private int id;
    private static int counter = 0;
    private String description;
    private LocalDate dateOperation;
    private double montant;

    public GestionDepense(String description, LocalDate dateOperation, double montant) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description must not be null or empty");
        }
        if (dateOperation == null) {
            throw new IllegalArgumentException("Date of operation must not be null");
        }
        if (montant < 0) {
            throw new IllegalArgumentException("Amount must be non-negative");
        }
        this.id = ++counter; // Increment and assign unique ID
        this.description = description;
        this.dateOperation = dateOperation;
        this.montant = montant;
    }
    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDateOperation() {
        return dateOperation;
    }

    public double getMontant() {
        return montant;
    }

    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description must not be null or empty");
        }
        this.description = description;
    }

    public void setDateOperation(LocalDate dateOperation) {
        if (dateOperation == null) {
            throw new IllegalArgumentException("Date of operation must not be null");
        }
        this.dateOperation = dateOperation;
    }

    public void setMontant(double montant) {
        if (montant < 0) {
            throw new IllegalArgumentException("Amount must be non-negative");
        }
        this.montant = montant;
    }
}