import java.util.Optional;

/**
 * Enforces a maximum speed for a given car type, e.g.
 * "Truck speed shouldn't exceed 60" or "Private car speed shouldn't exceed 80".
 *
 * A new speed limit for a new (or existing) car type is added by creating
 * another instance of this rule with different arguments - no code changes
 * needed elsewhere.
 */
public final class MaxSpeedRule implements Rule {

    private final CarType carType;
    private final double maxSpeed;
    private final double fee;

    public MaxSpeedRule(CarType carType, double maxSpeed, double fee) {
        this.carType = carType;
        this.maxSpeed = maxSpeed;
        this.fee = fee;
    }

    @Override
    public Optional<Violation> evaluate(Observation observation) {
        if (observation.getCarType() == carType && observation.getSpeed() > maxSpeed) {
            String description = String.format(
                    "speed of %s exceeded max allowed %s",
                    trimTrailingZero(observation.getSpeed()),
                    trimTrailingZero(maxSpeed));
            return Optional.of(new Violation(carType + "_SPEED_LIMIT", description, fee));
        }
        return Optional.empty();
    }

    // Keeps whole numbers looking like "94" instead of "94.0" in printed fines.
    private static String trimTrailingZero(double value) {
        if (value == Math.floor(value) && !Double.isInfinite(value)) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }
}
