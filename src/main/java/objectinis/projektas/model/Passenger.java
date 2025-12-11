package objectinis.projektas.model;

public abstract class Passenger {
    protected String name;
    public Passenger(String name) { this.name = name; }
    public abstract double GetDiscountRate();
    public abstract String GetType();
    public String GetName() { return name; }
}
