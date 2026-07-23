import java.time.LocalDate;

/**
 * A single, immutable snapshot of data sent by the physical radar hardware.
 * The Radar class (and every Rule) only ever depends on this shape, never on
 * how the hardware actually captured it.
 */
public final class Observation {

    private final String plateNumber;
    private final LocalDate date;
    private final CarType carType;
    private final double speed;
    private final boolean seatbeltFastened;

    public Observation(String plateNumber,
                        LocalDate date,
                        CarType carType,
                        double speed,
                        boolean seatbeltFastened) {
        this.plateNumber = plateNumber;
        this.date = date;
        this.carType = carType;
        this.speed = speed;
        this.seatbeltFastened = seatbeltFastened;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public CarType getCarType() {
        return carType;
    }

    public double getSpeed() {
        return speed;
    }

    public boolean isSeatbeltFastened() {
        return seatbeltFastened;
    }
}
