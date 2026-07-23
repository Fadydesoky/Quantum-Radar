import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The Quantum Radar.
 *
 * It knows nothing about the physical hardware - it only receives already
 * parsed {@link Observation} objects. It also knows nothing about *which*
 * rules exist: it is simply handed a list of {@link Rule} objects to check
 * every observation against. That is what makes it extensible - new rules
 * are added from the outside (see Main) without ever touching this class.
 */
public final class Radar {

    private final List<Rule> rules;
    private final List<Fine> issuedFines = new ArrayList<>();

    public Radar(List<Rule> rules) {
        this.rules = new ArrayList<>(rules);
    }

    /** Lets callers register an extra rule at runtime, e.g. loaded from config. */
    public void addRule(Rule rule) {
        rules.add(rule);
    }

    /**
     * Feeds one radar reading through every configured rule.
     * Returns a Fine if one or more rules were broken, otherwise empty.
     */
    public Optional<Fine> processObservation(Observation observation) {
        List<Violation> violations = new ArrayList<>();
        for (Rule rule : rules) {
            rule.evaluate(observation).ifPresent(violations::add);
        }

        if (violations.isEmpty()) {
            return Optional.empty();
        }

        Fine fine = new Fine(observation.getPlateNumber(), violations);
        issuedFines.add(fine);
        return Optional.of(fine);
    }

    public List<Fine> getIssuedFines() {
        return new ArrayList<>(issuedFines);
    }

    /** Plate number -> total amount fined across every fine ever issued to that plate. */
    public Map<String, Double> getFineTotalsByPlate() {
        Map<String, Double> totals = new HashMap<>();
        for (Fine fine : issuedFines) {
            totals.merge(fine.getPlateNumber(), fine.getTotalAmount(), Double::sum);
        }
        return totals;
    }

    /** Rule id -> how many times that rule has been violated across every fine ever issued. */
    public Map<String, Integer> getViolationCountsByRule() {
        Map<String, Integer> counts = new HashMap<>();
        for (Fine fine : issuedFines) {
            for (Violation v : fine.getViolations()) {
                counts.merge(v.getRuleId(), 1, Integer::sum);
            }
        }
        return counts;
    }
}
