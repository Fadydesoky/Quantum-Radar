import java.util.Optional;

/**
 * Enforces "seat belt should be fastened" for every car type.
 */
public final class SeatbeltRule implements Rule {

    private final double fee;

    public SeatbeltRule(double fee) {
        this.fee = fee;
    }

    @Override
    public Optional<Violation> evaluate(Observation observation) {
        if (!observation.isSeatbeltFastened()) {
            return Optional.of(new Violation("SEATBELT", "Seatbelt not fastened", fee));
        }
        return Optional.empty();
    }
}
