/**
 * A single broken rule produced by evaluating one {@link Rule} against one
 * {@link Observation}.
 *
 * ruleId   - a stable identifier for the rule that fired (used for grouping /
 *            counting statistics, e.g. "SEATBELT", "TRUCK_SPEED_LIMIT").
 * description - the human readable line printed on the fine, e.g.
 *            "speed of 94 exceeded max allowed 80".
 * fee      - the amount charged for this specific violation.
 */
public final class Violation {

    private final String ruleId;
    private final String description;
    private final double fee;

    public Violation(String ruleId, String description, double fee) {
        this.ruleId = ruleId;
        this.description = description;
        this.fee = fee;
    }

    public String getRuleId() {
        return ruleId;
    }

    public String getDescription() {
        return description;
    }

    public double getFee() {
        return fee;
    }
}
