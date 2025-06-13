package GestionFinance;
import java.time.LocalDate;
import java.util.List;

public class Revenue extends GestionDepense {

    private List<SourceRevenue> source;

    public Revenue(String description, LocalDate dateOperation, double montant, List<SourceRevenue> source) {
        super(description, dateOperation, montant);
        if (source == null || source.isEmpty()) {
            throw new IllegalArgumentException("Source list must not be null or empty");
        }
        this.source = source;
    }

    public List<SourceRevenue> getSource() {
        return source;
    }

    public void setSource(List<SourceRevenue> source) {
        if (source == null || source.isEmpty()) {
            throw new IllegalArgumentException("Source list must not be null or empty");
        }
        this.source = source;
    }
}