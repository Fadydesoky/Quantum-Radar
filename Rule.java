import java.util.Optional;

/**
 * A single traffic rule.
 *
 * This is the extensibility seam the challenge asks for: to add a brand new
 * rule (e.g. "Bus speed shouldn't exceed 70" or "No phone use while driving")
 * you simply write a new class implementing this interface and add an
 * instance of it to the list passed into {@link Radar}. Nothing in Radar,
 * Fine, or Observation ever needs to change.
 */
public interface Rule {

    /**
     * Checks a single observation against this rule.
     *
     * @param observation the radar reading to check
     * @return a Violation if the rule was broken, otherwise Optional.empty()
     */
    Optional<Violation> evaluate(Observation observation);
}
