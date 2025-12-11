package objectinis.projektas.model;

public class BusTicket extends TransportTicket {

    public BusTicket(String route, double basePrice, Passenger passenger, String ownerUsername) {
        super(route, basePrice, passenger, ownerUsername);
    }

    @Override
    public double CalculatePrice() {
        // Bilieto kaina priklauso nuo keleivio nuolaidos
        return basePrice * passenger.GetDiscountRate();
    }

    @Override
    public void PrintTicket() {
        System.out.printf(
                "[BUS TICKET] Route: %s, Price: %.2f EUR, Type: %s, Name: %s%n",
                route,
                CalculatePrice(),
                passenger.getClass().getSimpleName(),
                passenger.GetName()
        );
    }
}
