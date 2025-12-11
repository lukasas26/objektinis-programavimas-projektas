package objectinis.projektas.service;
import java.util.*;
import objectinis.projektas.model.TransportTicket;

public class TicketSearchService {
    private static String trim(String s) { return s == null ? "" : s.replace("\r","").trim(); }
    private static String toLower(String s) { return s == null ? "" : s.toLowerCase(); }
    public List<TransportTicket> SearchByRoutePart(List<TransportTicket> tickets, String routePart) {
        List<TransportTicket> results = new ArrayList<>();
        String searchTerm = toLower(trim(routePart));
        for (TransportTicket t : tickets) {
            String route = toLower(trim(t.GetRoute()));
            if (route.contains(searchTerm)) results.add(t);
        }
        return results;
    }

}
