package objectinis.projektas.service;
import java.util.List;
import objectinis.projektas.model.TransportTicket;

public interface ITicketStorage {
    void Save(List<TransportTicket> tickets, String filename);
    List<TransportTicket> Load(String filename);
}
