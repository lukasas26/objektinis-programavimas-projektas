package objectinis.projektas.service;

import objectinis.projektas.model.Role;
import objectinis.projektas.model.User;
import objectinis.projektas.util.AsmensKodas;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static objectinis.projektas.util.AsmensKodas.atsitiktineData;

public class UserService {

    private final Map<String, User> users = new HashMap<>();

    public UserService() {
        loadUsers();
    }

    private void loadUsers() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream("logins.txt"), "UTF-8"))) {

            String line;
            boolean firstUser = true;

            while ((line = br.readLine()) != null) {

                if (!line.contains(";")) continue;

                String[] parts = line.split(";");
                if (parts.length < 5) continue;

                String vardas = parts[0].trim();
                String pavarde = parts[1].trim();
                String username = parts[2].trim();
                String password = parts[3].trim();
                String asmensKodas = parts[4].trim();

                Role role = firstUser ? Role.ADMIN : Role.USER;
                firstUser = false;


                User user = new User(vardas, pavarde, username, password, asmensKodas, role);
                users.put(username, user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public User loginOrRegisterInteractive(Scanner sc) {

        System.out.println("=== Prisijungimas ===");
        System.out.print("Vartotojas: ");
        String username = sc.nextLine();

        System.out.print("Slaptazodis: ");
        String password = sc.nextLine();

        User u = users.get(username);


        if (u != null && u.getPassword().equals(password)) {
            System.out.println("Sekmingai prisijungta!");
            return u;
        }

        System.out.println("Neteisingi duomenys. Ar norite registruotis? (taip/ne)");
        if (!sc.nextLine().trim().equalsIgnoreCase("taip")) {
            return null;
        }

        return registerInteractive(sc);
    }


    public User registerInteractive(Scanner sc) {
        System.out.println("----- Registracija -----");
        System.out.print("[REGISTER] Iveskite varda: ");
        String vardas = sc.nextLine().trim();

        System.out.print("[REGISTER] Iveskite pavarde: ");
        String pavarde = sc.nextLine().trim();

        String username;
        while (true) {
            System.out.print("[REGISTER] Prisijungimo vardas: ");
            username = sc.nextLine().trim();

            if (username.isEmpty()) continue;

            if (users.containsKey(username)) {
                System.out.println("[REGISTER] Toks prisijungimo vardas jau egzistuoja.");
            } else break;
        }

        System.out.println("[DEBUG] Galimi sugeneruoti asmens kodai:");
        for (int i = 0; i < 5; i++) {
            String kodas = AsmensKodas.asmensKodoGenerator(
                    Math.random() < 0.5 ? "v" : "m",
                    atsitiktineData(),
                    "0"
            );
            System.out.println("  " + kodas);
        }

        System.out.print("[REGISTER] Iveskite asmens koda: ");
        String ak = sc.nextLine().trim();

        while (!AsmensKodas.asmensKodasValid(ak)) {
            System.out.println("Neteisingas asmens kodas! Bandykite dar karta:");
            ak = sc.nextLine().trim();
        }

        String pass1, pass2;
        while (true) {
            System.out.print("[REGISTER] Slaptazodis: ");
            pass1 = sc.nextLine();
            System.out.print("[REGISTER] Pakartoti slaptazodi: ");
            pass2 = sc.nextLine();

            if (!pass1.equals(pass2))
                System.out.println("[REGISTER] Slaptazodziai nesutampa. Bandykite dar karta.");
            else break;
        }

        User u = new User(vardas, pavarde, username, pass1, ak);
        addUser(u);

        return u;
    }

    private void addUser(User u) {
        users.put(u.getUsername(), u);

        try (PrintWriter pw = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream("logins.txt", true), "UTF-8")
        )) {
            pw.println(
                    u.getVardas() + ";" +
                            u.getPavarde() + ";" +
                            u.getUsername() + ";" +
                            u.getPassword() + ";" +
                            u.getAsmensKodas()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public User getUser(String username) {
        return users.get(username);
    }

    public Collection<User> getAllUsers() {
        return users.values();
    }

}
