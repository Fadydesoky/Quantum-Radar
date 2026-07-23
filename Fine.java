import java.util.Collections;
import java.util.List;

/**
 * A fine issued for one observation. Bundles every {@link Violation} found
 * for that observation together with the total amount due, and knows how to
 * render itself in the exact format required by the challenge:
 *
 * Traffic fine for car ABC1234
 * Total amount: 400 EGP
 * Violations:
 * - Seatbelt not fastened : 100 EGP
 * - speed of 94 exceeded max allowed 80 : 300 EGP
 */
public final class Fine {

    private final String plateNumber;
    private final List<Violation> violations;
    private final double totalAmount;

    public Fine(String plateNumber, List<Violation> violations) {
        this.plateNumber = plateNumber;
        this.violations = Collections.unmodifiableList(violations);
        double sum = 0;
        for (Violation v : violations) {
            sum += v.getFee();
        }
        this.totalAmount = sum;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public List<Violation> getViolations() {
        return violations;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    /** Renders the fine exactly as specified in the challenge. */
    public String print() {
        StringBuilder sb = new StringBuilder();
        sb.append("Traffic fine for car ").append(plateNumber).append(System.lineSeparator());
        sb.append("Total amount: ").append(formatAmount(totalAmount)).append(" EGP").append(System.lineSeparator());
        sb.append("Violations:");
        for (Violation v : violations) {
            sb.append(System.lineSeparator())
              .append("- ").append(v.getDescription())
              .append(" : ").append(formatAmount(v.getFee())).append(" EGP");
        }
        return sb.toString();
    }

    private static String formatAmount(double value) {
        if (value == Math.floor(value) && !Double.isInfinite(value)) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }

    @Override
    public String toString() {
        return print();
    }
}
