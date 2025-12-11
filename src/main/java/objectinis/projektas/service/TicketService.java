package objectinis.projektas.service;

import objectinis.projektas.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TicketService {

    // ============================
    //        FIELDS
    // ============================
    private final List<TransportTicket> allTickets = new ArrayList<>();
    private final List<TransportTicket> soldTickets = new ArrayList<>();
    private final ITicketStorage storage = new FileTicketStorage();


    public TicketService() {
        loadFromFile();
    }

    public List<TransportTicket> getAllTickets() {
        List<TransportTicket> all = new ArrayList<>(allTickets);
        all.addAll(soldTickets);

        return all;
    }


    public void deleteTicket(TransportTicket t) {
        allTickets.remove(t);
        soldTickets.remove(t);
        saveToFile();
    }

    public List<TransportTicket> search(String route) {
        TicketSearchService searchService = new TicketSearchService();
        return searchService.SearchByRoutePart(allTickets, route);
    }


    public boolean buyTicket(
            TransportTicket ticket,
            String ownerUsername,
            String holderUsername,
            String passengerType
    ) {
        // surandame tikrą objektą iš allTickets
        TransportTicket real = allTickets.stream()
                .filter(t -> t != null
                        && t.GetRoute().equals(ticket.GetRoute())
                        && t.GetBasePrice() == ticket.GetBasePrice()
                        && t.GetPassenger().GetName().equals("-"))
                .findFirst()
                .orElse(null);

        if (real == null) return false;

        allTickets.remove(real);

        Passenger passenger = createPassenger(passengerType, holderUsername);

        TransportTicket newTicket = createTicket(
                real.getClass().getSimpleName(),
                real.GetRoute(),
                real.GetBasePrice(),
                passenger,
                ownerUsername
        );

        soldTickets.add(newTicket);
        saveToFile();
        return true;
    }

    /**
     * Grąžina VISUS konkretaus vartotojo bilietus (parduotus jam).
     */
    public List<TransportTicket> getUserTickets(String username) {
        return soldTickets.stream()
                .filter(t -> t.GetOwnerUsername().equals(username))
                .collect(Collectors.toList());
    }

    /**
     * Grąžina bilietą:
     *  - patikrina, ar bilietas priklauso vartotojui,
     *  - pašalina iš soldTickets,
     *  - sukuria naują laisvą bilietą su keleiviu "-",
     *  - išsaugo faile.
     */
    public boolean returnTicket(TransportTicket t, String username) {

        if (!t.GetOwnerUsername().equals(username)) return false;

        soldTickets.remove(t);

        Passenger empty = createPassenger("adult", "-");

        TransportTicket restored = createTicket(
                t.getClass().getSimpleName(),
                t.GetRoute(),
                t.GetBasePrice(),
                empty,
                "-"
        );

        allTickets.add(restored);
        saveToFile();
        return true;
    }

    /**
     * Admin funkcija – pridėti naują bilieta i laisvu sarasa.
     */
    public void addTicket(
            String type,
            String route,
            double price,
            String passengerType
    ) {
        Passenger passenger = createPassenger(passengerType, "-");

        TransportTicket ticket = createTicket(type, route, price, passenger, "-");

        if (ticket == null) {
            System.out.println("[Bilietas. KLAIDA] Blogas tipas.");
            return;
        }

        allTickets.add(ticket);
        saveToFile();
    }

    // ============================
    //      LOAD / SAVE (I/O)
    // ============================

    private void loadFromFile() {

        // nuskaitome visus bilietus per FileTicketStorage
        List<TransportTicket> allLoaded = storage.Load("baze.txt");

        for (TransportTicket t : allLoaded) {
            if (t == null) continue;

            String owner = t.GetOwnerUsername();

            if (owner == null || owner.isBlank() || owner.equals("-")) {
                allTickets.add(t);
            } else {
                soldTickets.add(t);
            }
        }
    }


    private void saveToFile() {

        List<TransportTicket> all = new ArrayList<>();
        all.addAll(allTickets);
        all.addAll(soldTickets);

        storage.Save(all, "baze.txt");
    }


    private Passenger createPassenger(String type, String name) {

        if (type == null) {
            return new AdultPassenger(name);
        }

        String normalized = type.toLowerCase();

        return switch (normalized) {
            case "adult", "adultpassenger" -> new AdultPassenger(name);
            case "student", "studentpassenger" -> new StudentPassenger(name);
            case "senior", "seniorpassenger" -> new SeniorPassenger(name);
            default -> new AdultPassenger(name);
        };
    }


    private TransportTicket createTicket(String type, String route, double price, Passenger passenger, String owner) {

        if (type == null) return null;

        String normalized = type.toLowerCase();

        return switch (normalized) {
            case "bus", "busticket" -> new BusTicket(route, price, passenger, owner);
            case "train", "trainticket" -> new TrainTicket(route, price, passenger, owner);
            case "trolley", "trolleybus", "trolleybusticket" -> new TrolleybusTicket(route, price, passenger, owner);
            default -> null;
        };
    }
}
