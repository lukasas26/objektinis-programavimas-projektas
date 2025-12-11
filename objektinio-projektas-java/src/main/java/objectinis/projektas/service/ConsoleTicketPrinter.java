package objectinis.projektas.service;
import java.util.List;
import objectinis.projektas.model.TransportTicket;

public class ConsoleTicketPrinter implements ITicketPrinter {
    @Override public void PrintAll(List<TransportTicket> tickets) {
        for (TransportTicket t : tickets) t.PrintTicket();
    }
}
