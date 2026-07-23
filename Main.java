import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Demonstrates the Quantum Radar system end to end:
 *  1. Configure the radar with the currently known rules.
 *  2. Feed it a handful of observations (as if sent by the physical radar).
 *  3. Print every fine that was issued, exactly as required.
 *  4. Print the two summary reports: fines per plate, and violations per rule.
 */
public final class Main {

    public static void main(String[] args) {

        // 1. Configure the rules. Adding a new rule (e.g. a bus speed limit)
        //    is just one more line here - Radar itself never changes.
        List<Rule> rules = Arrays.asList(
                new MaxSpeedRule(CarType.TRUCK, 60, 300),
                new MaxSpeedRule(CarType.PRIVATE, 80, 300),
                new SeatbeltRule(100)
        );
        Radar radar = new Radar(rules);

        // 2. Observations coming in from the physical radar.
        List<Observation> observations = Arrays.asList(
                new Observation("ABC1234", LocalDate.of(2026, 7, 23), CarType.PRIVATE, 94, false),
                new Observation("XYZ9081", LocalDate.of(2026, 7, 23), CarType.TRUCK, 55, true),
                new Observation("TRK5567", LocalDate.of(2026, 7, 23), CarType.TRUCK, 72, true),
                new Observation("BUS3321", LocalDate.of(2026, 7, 23), CarType.BUS, 40, false)
        );

        // 3. Run each observation through the radar and print any resulting fine.
        for (Observation observation : observations) {
            Optional<Fine> fine = radar.processObservation(observation);
            if (fine.isPresent()) {
                System.out.println(fine.get().print());
                System.out.println();
            } else {
                System.out.println("No violations for car " + observation.getPlateNumber());
                System.out.println();
            }
        }

        // 4. Summary reports.
        System.out.println("=== Fines per plate ===");
        Map<String, Double> totalsByPlate = radar.getFineTotalsByPlate();
        for (Map.Entry<String, Double> entry : totalsByPlate.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue() + " EGP");
        }

        System.out.println();
        System.out.println("=== Violations per rule ===");
        Map<String, Integer> countsByRule = radar.getViolationCountsByRule();
        for (Map.Entry<String, Integer> entry : countsByRule.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}
