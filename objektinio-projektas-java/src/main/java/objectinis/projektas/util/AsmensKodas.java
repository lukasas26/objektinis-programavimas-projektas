package objectinis.projektas.util;

import java.util.*;

public class AsmensKodas {

    // =======================
    // Pagalbinės funkcijos
    // =======================

    public static int centuryFromYear(int year) {
        return (year - 1) / 100 + 1;
    }

    public static String eilesNumerioGenerator(boolean aktyvus) {
        if (aktyvus) {
            Random rand = new Random();
            int skaicius = rand.nextInt(999) + 1;
            return String.format("%03d", skaicius);
        } else {
            throw new IllegalArgumentException("Eilės numerio generatorius turi būti įjungtas.");
        }
    }

    public static boolean asmensKodasValid(String asmensKodas) {
        if (asmensKodas == null || asmensKodas.length() != 11) return false;

        char pirmas = asmensKodas.charAt(0);
        if (pirmas < '1' || pirmas > '6') return false;

        try {
            int menesis = Integer.parseInt(asmensKodas.substring(3, 5));
            int diena = Integer.parseInt(asmensKodas.substring(5, 7));

            if (menesis < 1 || menesis > 12) return false;

            if (menesis == 2) {
                if (diena < 1 || diena > 29) return false;
            } else {
                if (diena < 1 || diena > 31) return false;
            }

            // Patikrinam, ar kontrolinis skaičius atitinka
            int[] daugikliai1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 1};
            int[] daugikliai2 = {3, 4, 5, 6, 7, 8, 9, 1, 2, 3};

            int s = 0;
            for (int i = 0; i < 10; ++i)
                s += (asmensKodas.charAt(i) - '0') * daugikliai1[i];

            int kontrolinis = s % 11;
            if (kontrolinis == 10) {
                s = 0;
                for (int i = 0; i < 10; ++i)
                    s += (asmensKodas.charAt(i) - '0') * daugikliai2[i];
                kontrolinis = s % 11;
                if (kontrolinis == 10) kontrolinis = 0;
            }

            int paskutinis = asmensKodas.charAt(10) - '0';
            if (paskutinis != kontrolinis) return false;

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    // =======================
    // Asmens kodo generatorius
    // =======================

    /**
     * Sugeneruoja pilną asmens kodą pagal lytį, gimimo datą ir eilės numerį.
     */
    public static String asmensKodoGenerator(String lytis, String gimimoData, String eilesNr) {
        try {
            int metai = Integer.parseInt(gimimoData.substring(0, 4));
            int amzius = centuryFromYear(metai);

            String gimimoDataX = gimimoData.substring(2).replace("-", "");

            String eiles;
            if (eilesNr.equals("0"))
                eiles = eilesNumerioGenerator(true);
            else
                eiles = eilesNr;

            char pirmas;
            if (lytis.equals("v")) {
                if (amzius == 19) pirmas = '1';
                else if (amzius == 20) pirmas = '3';
                else if (amzius == 21) pirmas = '5';
                else return "Galimas tik 19-21 amžius!";
            } else if (lytis.equals("m")) {
                if (amzius == 19) pirmas = '2';
                else if (amzius == 20) pirmas = '4';
                else if (amzius == 21) pirmas = '6';
                else return "Galimas tik 19-21 amžius!";
            } else {
                return "[!!!] Lytis turi būti 'v' arba 'm'.";
            }

            String reiksme = pirmas + gimimoDataX + eiles;
            if (reiksme.length() != 10)
                return "[KLAIDA] Netinkamas kodas.";

            // Kontrolinio skaičiaus skaičiavimas
            int s = 0;
            int[] daugikliai1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 1};
            int[] daugikliai2 = {3, 4, 5, 6, 7, 8, 9, 1, 2, 3};

            for (int i = 0; i < 10; ++i)
                s += (reiksme.charAt(i) - '0') * daugikliai1[i];

            int kontrolinis = s % 11;
            if (kontrolinis == 10) {
                s = 0;
                for (int i = 0; i < 10; ++i)
                    s += (reiksme.charAt(i) - '0') * daugikliai2[i];
                kontrolinis = s % 11;
                if (kontrolinis == 10) kontrolinis = 0;
            }

            String kodas = reiksme + kontrolinis;

            if (asmensKodasValid(kodas))
                return kodas;
            else
                return "[KLAIDA] Neteisingas sugeneruotas kodas.";

        } catch (Exception e) {
            return "[KLAIDA] " + e.getMessage();
        }
    }

    public static String atsitiktineData() {
        Random r = new Random();
        int metai = 1970 + r.nextInt(40);  // 1970–2009
        int menuo = 1 + r.nextInt(12);
        int diena = 1 + r.nextInt(28);

        return String.format("%04d-%02d-%02d", metai, menuo, diena);
    }
}



