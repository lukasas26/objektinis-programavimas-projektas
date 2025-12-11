package objectinis.projektas.model;

public class SeniorPassenger extends Passenger {
    public SeniorPassenger(String name) { super(name); }
    @Override public double GetDiscountRate() { return 0.2; }
    @Override public String GetType() { return "Senior"; }
}
