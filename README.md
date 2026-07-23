![Java](https://img.shields.io/badge/Java-17-orange)
![OOP](https://img.shields.io/badge/OOP-Design-blue)
![SOLID](https://img.shields.io/badge/SOLID-Principles-green)

# Quantum-Radar 🚦

An extensible traffic radar simulator built in Java following Object-Oriented Design and SOLID principles.

| Language | Java |
|----------|------|
| Paradigm | Object-Oriented Programming |
| Principles | SOLID |
| Pattern | Strategy |
| Challenge | Fawry Quantum Internship |


## Features

- Object-Oriented Design (OOP)
- SOLID Principles
- Extensible Rule Engine
- Traffic Violation Processing
- Fine Aggregation
- Summary Reports

## How to run

```bash
cd src
javac *.java
java Main
```

## Design

The whole point of the challenge is extensibility — new rules must be
addable "without modifying the Radar". That drives every decision below.

| Class | Responsibility |
|---|---|
| `Observation` | An immutable snapshot of what the physical radar sent: plate number, date, car type, speed, seatbelt status. `Radar` never talks to the hardware — this is the only thing it depends on, per the note "no need to think about the physical radar". |
| `Rule` (interface) | One method: `evaluate(Observation) -> Optional<Violation>`. This is the extension point. |
| `MaxSpeedRule` | A configurable `Rule` — car type + max speed + fee. One instance per limit (truck @ 60, private @ 80, ...). |
| `SeatbeltRule` | A configurable `Rule` for the seatbelt check. |
| `Violation` | One broken rule: a stable `ruleId` (for counting/statistics), a human-readable `description` (for printing), and a `fee`. |
| `Fine` | Groups every `Violation` found for one observation, sums the fees, and renders the exact receipt format from the spec. |
| `Radar` | Holds a `List<Rule>`. For each observation, runs every rule and bundles any violations into a `Fine`. Also keeps a running history so it can answer "total fines per plate" and "violation counts per rule". |
| `Main` | Wires it all together and demonstrates the system on a handful of sample observations. |



## Architecture

```text
                         +------------------+
                         |   Observation    |
                         |------------------|
                         | plateNumber      |
                         | date             |
                         | carType          |
                         | speed            |
                         | seatbelt         |
                         +--------+---------+
                                  |
                                  | processObservation()
                                  v
                        +----------------------+
                        |        Radar         |
                        |----------------------|
                        | List<Rule> rules     |
                        | History / Reports    |
                        +----+-----------+-----+
                             |           |
               iterates over |           | creates
                             |           v
                             |    +--------------+
                             |    |     Fine     |
                             |    |--------------|
                             |    | Violations[] |
                             |    | Total Amount |
                             |    +------+-------+
                             |           ^
                             |           |
                             v           |
                  +-------------------------+
                  |          Rule           |
                  |-------------------------|
                  | evaluate(observation)   |
                  +-----------+-------------+
                              ^
              implements      |      implements
                     +--------+---------+
                     |                  |
        +---------------------+   +----------------------+
        |   MaxSpeedRule      |   |    SeatbeltRule      |
        +---------------------+   +----------------------+
                     \                  /
                      \                /
                       \              /
                        v            v
                     +----------------------+
                     |      Violation       |
                     |----------------------|
                     | ruleId               |
                     | description          |
                     | amount               |
                     +----------------------+
```


```
Observation
     │
     ▼
Radar.processObservation()
     │
     ▼
For each Rule
     │
     ▼
Rule.evaluate()
     │
     ├── No violation
     │
     └── Violation
            │
            ▼
Collect Violations
            │
            ▼
Create Fine
            │
            ▼
Update Statistics
```

## Design Pattern

The project follows the Strategy Pattern.

Each traffic rule implements the `Rule` interface and can be plugged into the Radar without modifying its implementation.

This keeps the Radar closed for modification but open for extension, following the Open/Closed Principle.


### Why this is extensible

Adding a new rule — say "Bus speed shouldn't exceed 70" or a brand new
"no phone use" rule — never touches `Radar`, `Fine`, or `Observation`:

```java
// Just add another instance to the list handed to Radar:
new MaxSpeedRule(CarType.BUS, 70, 300)

// Or write a whole new rule from scratch:
public final class NoPhoneUseRule implements Rule {
    public Optional<Violation> evaluate(Observation o) {
        // ... your logic ...
    }
}
```

`Radar` only ever iterates over `List<Rule>` — it has zero knowledge of how
many rule types exist or what they check.

### Fees

In the sample, every violation has a flat fee (100 for seatbelt, 300 for
speeding regardless of how much the limit was exceeded by), so each `Rule`
is configured with a fixed fee. If you later want the fee to scale with how
badly the rule was broken (e.g. 5 EGP per km/h over the limit), that only
requires changing what happens *inside* `MaxSpeedRule.evaluate` — nothing
else in the system needs to know.

### Reporting

- `radar.getFineTotalsByPlate()` → `Map<plate, totalAmount>` — satisfies "get
  all fines: plate number with total amount".
- `radar.getViolationCountsByRule()` → `Map<ruleId, count>` — satisfies "get
  all violated rules with count for each".

Both are cumulative across every observation the radar has ever processed,
not just one fine, since a plate can rack up violations over multiple
readings.


## Future Enhancements

- BusSpeedRule
- RedLightRule
- MobilePhoneRule
- External configuration for speed limits
- Unit Tests

---

Built as part of the Fawry Quantum Internship Challenge.
