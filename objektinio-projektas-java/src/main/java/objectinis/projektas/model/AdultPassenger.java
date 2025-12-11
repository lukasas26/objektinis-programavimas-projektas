package objectinis.projektas.model;

public class AdultPassenger extends Passenger {
    public AdultPassenger(String name) { super(name); }
    @Override public double GetDiscountRate() { return 1.0; }
    @Override public String GetType() { return "Adult"; }
}
