package objectinis.projektas.model;

public class User {

    private String vardas;
    private String pavarde;
    private String username;
    private String password;
    private String asmensKodas;
    private Role role;

    public User(String vardas, String pavarde, String username,
                String password, String asmensKodas) {

        this.vardas = vardas;
        this.pavarde = pavarde;
        this.username = username;
        this.password = password;
        this.asmensKodas = asmensKodas;
        this.role = Role.USER; // default
    }

    // ADMIN constructor
    public User(String vardas, String pavarde, String username,
                String password, String asmensKodas, Role role) {

        this.vardas = vardas;
        this.pavarde = pavarde;
        this.username = username;
        this.password = password;
        this.asmensKodas = asmensKodas;
        this.role = role;
    }

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getVardas() { return vardas; }
    public String getPavarde() { return pavarde; }
    public String getAsmensKodas() { return asmensKodas; }
    public Role getRole() { return role; }
}
