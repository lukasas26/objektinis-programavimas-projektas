package objectinis.projektas.model;

public class StudentPassenger extends Passenger {
    public StudentPassenger(String name) { super(name); }
    @Override public double GetDiscountRate() { return 0.5; }
    @Override public String GetType() { return "Student"; }
}
