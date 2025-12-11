package objectinis.projektas.service;

import objectinis.projektas.model.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class FileTicketStorage implements ITicketStorage {

    @Override
    public void Save(List<TransportTicket> tickets, String filename) {
        File file = new File(filename);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            throw new RuntimeException("[FAILAS] Nepavyko sukurti failo: " + filename, e);
        }

        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            for (TransportTicket t : tickets) {
                String passengerType = t.GetPassenger().getClass().getSimpleName();
                String passengerName = t.GetPassenger().GetName();
                String ownerUsername = t.GetOwnerUsername() != null ? t.GetOwnerUsername() : "";

                pw.println(
                        t.getClass().getSimpleName() + "," +
                                t.GetRoute() + "," +
                                t.GetBasePrice() + "," +
                                passengerType + "," +
                                passengerName + "," +
                                ownerUsername
                );
            }
        } catch (IOException e) {
            System.out.println("[FAILAS] Klaida irasant i faila: " + e.getMessage());
        }
    }

    @Override
    public List<TransportTicket> Load(String filename) {
        List<TransportTicket> tickets = new ArrayList<>();
        File file = new File(filename);

        // užtikrinam, kad failas egzistuoja
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("[FAILAS] Nepavyko sukurti failo: " + filename, e);
            }
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 5) continue;

                String type = parts[0];
                String route = parts[1];
                double price = Double.parseDouble(parts[2]);
                String passengerType = parts[3];
                String passengerName = parts[4];
                String ownerUsername = parts.length >= 6 ? parts[5] : "";

                Passenger passenger = switch (passengerType) {
                    case "AdultPassenger" -> new AdultPassenger(passengerName);
                    case "StudentPassenger" -> new StudentPassenger(passengerName);
                    case "SeniorPassenger" -> new SeniorPassenger(passengerName);
                    default -> new AdultPassenger(passengerName); // apsauga nuo null
                };

                TransportTicket ticket = switch (type) {
                    case "BusTicket" -> new BusTicket(route, price, passenger, ownerUsername);
                    case "TrainTicket" -> new TrainTicket(route, price, passenger, ownerUsername);
                    case "TrolleybusTicket" -> new TrolleybusTicket(route, price, passenger, ownerUsername);
                    default -> null;
                };

                if (ticket != null) {
                    tickets.add(ticket);
                }
            }

        } catch (IOException e) {
            System.out.println("[FAILAS] Klaida skaitant failą: " + e.getMessage());
        }

        return tickets;
    }
}
