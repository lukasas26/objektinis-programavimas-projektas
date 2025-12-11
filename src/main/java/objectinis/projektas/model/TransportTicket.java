package objectinis.projektas.model;

public abstract class TransportTicket {
    protected String route;
    protected double basePrice;
    protected Passenger passenger;
    protected String ownerUsername;

    public TransportTicket(String route, double basePrice, Passenger passenger, String ownerUsername) {
        this.route = route;
        this.basePrice = basePrice;
        this.passenger = passenger;
        this.ownerUsername = ownerUsername;
    }

    public String GetRoute() { return route; }
    public Passenger GetPassenger() { return passenger; }
    public double GetBasePrice() { return basePrice; }
    public String GetOwnerUsername() { return ownerUsername; }

    public abstract double CalculatePrice();
    public abstract void PrintTicket();
}
