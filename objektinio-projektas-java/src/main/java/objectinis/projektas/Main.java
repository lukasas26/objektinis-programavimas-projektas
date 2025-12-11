package objectinis.projektas;

import objectinis.projektas.model.TransportTicket;
import objectinis.projektas.model.User;
import objectinis.projektas.service.ConsoleTicketPrinter;
import objectinis.projektas.service.ITicketPrinter;
import objectinis.projektas.service.TicketService;
import objectinis.projektas.service.UserService;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        UserService userService = new UserService();
        TicketService ticketService = new TicketService();

        Scanner sc = new Scanner(System.in);

        User user = userService.loginOrRegisterInteractive(sc);

        if (user == null) {
            System.out.println("Programa baigta.");
            return;
        }

        System.out.println("Sveiki, " + user.getUsername());

        if (user.isAdmin()) {
            adminMenu(user, userService, ticketService, sc);
        } else {
            userMenu(user, ticketService, sc);
        }
    }

    // ===============================
    //           ADMIN MENU
    // ===============================
    private static void adminMenu(User admin, UserService userService, TicketService ticketService, Scanner sc) {

        while (true) {
            System.out.println("\n=== ADMIN MENIU ===");
            System.out.println("1. Perziureti visus vartotojus");
            System.out.println("2. Perziureti visus bilietus");
            System.out.println("3. Prideti nauja bilieta");
            System.out.println("4. Istrinti bilieta");
            System.out.println("0. Iseiti");

            String input = sc.nextLine();

            if (!input.matches("\\d+")) {   // allow only digits
                System.out.println("Prasome ivesti skaiciu!");
                continue;
            }

            int c = Integer.parseInt(input);

            switch (c) {
                case 1:
                    System.out.println("\n=== VARTOTOJAI ===");
                    userService.getAllUsers().forEach(u ->
                            System.out.println(u.getUsername() + " | " + u.getRole())
                    );
                    break;

                case 2:
                    System.out.println("\n=== Visi bilietai ===");

                    boolean printedAny = false;

                    List<TransportTicket> tickets = ticketService.getAllTickets();

                    ITicketPrinter printer = new ConsoleTicketPrinter();

                    if (!tickets.isEmpty()) {
                        printer.PrintAll(tickets);
                        printedAny = true;
                    }


                    if (!printedAny) {
                        System.out.println("Bilietu nera.");
                    }
                    break;

                case 3:
                    addTicketAdmin(ticketService, sc);
                    break;

                case 4:
                    deleteTicketAdmin(sc, ticketService);
                    break;

                case 0:
                    return;
            }
        }
    }

    private static void addTicketAdmin(TicketService ticketService, Scanner sc) {
        System.out.println("\n=== Naujas bilietas ===");

        System.out.print("Tipas (bus/train/trolley): ");
        String type = sc.nextLine();

        System.out.print("Marsrutas: ");
        String route = sc.nextLine();

        System.out.print("Bazine kaina: ");
        String priceInput = sc.nextLine();

        if (!priceInput.matches("\\d+(\\.\\d+)?")) {
            System.out.println("Prasome ivesti tik skaiciu!");
            return;
        }

        double price = Double.parseDouble(priceInput);

        System.out.print("Keleivio tipas (adult/student/senior): ");
        String pType = sc.nextLine();

        ticketService.addTicket(type, route, price, pType);

        System.out.println("Bilietas pridetas!");
    }

    // ===============================
    //             USER MENU
    // ===============================
    private static void userMenu(User user, TicketService ticketService, Scanner sc) {

        while (true) {
            System.out.println("\n=== USER MENIU ===");
            System.out.println("1. Mano bilietai");
            System.out.println("2. Pirkti bilieta");
            System.out.println("3. Pirkti bilieta kitam");
            System.out.println("4. Grazinti bilieta");
            System.out.println("5. Ieskoti bilietu");
            System.out.println("0. Iseiti");



            String input = sc.nextLine();

            if (!input.matches("\\d+")) {   // allow only digits
                System.out.println("Prasome ivesti skaiciu!");
                continue;
            }

            int c = Integer.parseInt(input);

            switch (c) {

                case 1: {
                    List<TransportTicket> myTickets = ticketService.getUserTickets(user.getUsername());
                    if (myTickets.isEmpty()) {
                        System.out.println("Neturite bilietu.");
                    } else {
                        myTickets.forEach(TransportTicket::PrintTicket);
                    }
                    break;
                }
                case 2: {
                    System.out.print("Iveskite marsruta: ");
                    String r = sc.nextLine();
                    buyTicket(user, ticketService, r, user.getUsername(), sc);
                    break;
                }
                case 3: {
                    System.out.print("Iveskite marsruta: ");
                    String r2 = sc.nextLine();
                    System.out.print("Gavejas (username): ");
                    String holder = sc.nextLine();

                    if (holder.isEmpty() || holder.equals("0")) {
                        System.out.println("Pirkimas atsauktas.");
                        continue;
                    }

                    buyTicket(user, ticketService, r2, holder, sc);
                    break;
                }
                case 4:
                {
                    returnTicket(user, ticketService, sc);
                    break;
                }
                case 5: {
                    System.out.print("Paieskos fraze: ");
                    String key = sc.nextLine();
                    ticketService.search(key).forEach(TransportTicket::PrintTicket);
                    break;
                }
                case 0: {
                    return;
                }
                default: {
                    System.out.println("Prasome ivesti skaiciu!");
                    return;
                }
            }
        }
    }

    private static void buyTicket(User owner, TicketService ticketService, String route, String holder, Scanner sc) {
        List<TransportTicket> available = ticketService.search(route);

        if (available.isEmpty()) {
            System.out.println("Tokio marsruto bilietu nera.");
            return;
        }

        System.out.println("Galimi bilietai:");
        for (int i = 0; i < available.size(); i++) {
            System.out.print((i + 1) + ". ");
            available.get(i).PrintTicket();
        }

        System.out.print("Pasirinkite: ");
        int idx = Integer.parseInt(sc.nextLine()) - 1;

        System.out.print("Keleivio tipas (adult/student/senior): ");
        String passengerType = sc.nextLine();

        if (passengerType.equals("0") || passengerType.isEmpty())
        {
            System.out.println("Pirkimas atsauktas.");
            return;
        }

        TransportTicket selected = available.get(idx);

        ticketService.buyTicket(
                selected,
                owner.getUsername(),
                holder,
                passengerType
        );

        System.out.println("Bilietas nupirktas!");
    }

    private static void returnTicket(User user, TicketService ticketService, Scanner sc) {
        List<TransportTicket> myTickets = ticketService.getUserTickets(user.getUsername());

        if (myTickets.isEmpty()) {
            System.out.println("Neturi bilietu.");
            return;
        }

        System.out.println("Pasirinkite grazinama bilieta:");
        for (int i = 0; i < myTickets.size(); i++) {
            System.out.print((i + 1) + ". ");
            myTickets.get(i).PrintTicket();
        }

        String input = sc.nextLine();

        // tik skaiciai
        if (!input.matches("\\d+")) {
            System.out.println("Prasome ivesti skaiciu!");
            return;
        }

        // 0 = atsaukimas
        if (input.equals("0")) {
            System.out.println("Grazinimas atsauktas.");
            return;
        }

        int nr = Integer.parseInt(input) - 1;

        // tikriname ar indeksas galiojantis
        if (nr < 0 || nr >= myTickets.size()) {
            System.out.println("Tokio bilieto nera!");
            return;
        }

        TransportTicket t = myTickets.get(nr);

        boolean ok = ticketService.returnTicket(t, user.getUsername());

        if (ok) {
            System.out.println("Bilietas grazintas.");
        } else {
            System.out.println("Negalite grazinti ne savo bilieto!");
        }
        
    }

    private static void deleteTicketAdmin(Scanner sc, TicketService ticketService) {

        List<TransportTicket> all = ticketService.getAllTickets();

        if (all.isEmpty()) {
            System.out.println("Nera bilietu, kuriuos butu galima istrinti.");
            return;
        }

        System.out.println("\n=== Visi bilietai ===");
        for (int i = 0; i < all.size(); i++) {
            System.out.print((i + 1) + ". ");
            all.get(i).PrintTicket();
        }

        System.out.print("Pasirinkite bilieto numeri, kuri norite istrinti: ");

        String input = sc.nextLine();

        // leidzia tik skaicius
        if (!input.matches("\\d+")) {
            System.out.println("Prasome ivesti skaiciu!");
            return;
        }

        int idx = Integer.parseInt(input) - 1;

        if (idx < 0 || idx >= all.size()) {
            System.out.println("Neteisingas pasirinkimas.");
            return;
        }

        TransportTicket t = all.get(idx);

        ticketService.deleteTicket(t);

        System.out.println("Bilietas istrintas sekmingai!");
    }

}
