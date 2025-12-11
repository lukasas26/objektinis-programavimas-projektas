package objectinis.projektas.model;
public class TrolleybusTicket extends TransportTicket {

    public TrolleybusTicket(String route, double basePrice, Passenger passenger, String ownerUsername) {
        super(route, basePrice, passenger, ownerUsername);
    }
    @Override public double CalculatePrice() { return (basePrice * passenger.GetDiscountRate()); }
    @Override public void PrintTicket() {
        System.out.println("[TROLLEYBUS TICKET] Route: " + route + ", Price: " + CalculatePrice() + " EUR"
                + ", Type: " + passenger.GetType() + ", Name: " + passenger.GetName());
    }
}
