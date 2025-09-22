package hr.unizd.eatro.export;

import hr.unizd.eatro.model.Meal;
import hr.unizd.eatro.model.MealPlan;
import hr.unizd.eatro.model.MealType;

import java.io.*;
import java.time.DayOfWeek;

/**
 * Tekstualni I/O: izvoz i uvoz plana u .txt datoteku.
 */
public class TextFileService {

    public void exportTxt(MealPlan plan, File file) {
        if (plan == null || file == null) return;
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(file));
            for (Meal m : plan.all()) {
                pw.println(safe(m.getNaziv()) + "|" +
                        safe(m.getDan() == null ? "" : m.getDan().name()) + "|" +
                        safe(m.getTip() == null ? "" : m.getTip().name()) + "|" +
                        m.isVegetarijanski() + "|" +
                        m.isBezGlutena() + "|" +
                        safe(m.getSastojci()));
            }
            pw.flush();
        } catch (IOException e) {
            System.err.println("Greška pri izvozu TXT: " + e.getMessage());
        } finally {
            if (pw != null) pw.close();
        }
    }

    public MealPlan importTxt(File file) {
        MealPlan plan = new MealPlan();
        if (file == null || !file.exists()) return plan;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|", -1); // zadrži prazna polja
                if (parts.length < 6) continue;
                Meal m = new Meal();
                m.setNaziv(parts[0]);
                m.setDan(parseDay(parts[1]));
                m.setTip(parseType(parts[2]));
                m.setVegetarijanski(Boolean.parseBoolean(parts[3]));
                m.setBezGlutena(Boolean.parseBoolean(parts[4]));
                m.setSastojci(parts[5]);
                plan.add(m);
            }
        } catch (IOException e) {
            System.err.println("Greška pri uvozu TXT: " + e.getMessage());
        } finally {
            try { if (br != null) br.close(); } catch (IOException ignored) {}
        }
        return plan;
    }

    private String safe(String s) { return s == null ? "" : s.replace("\n", " "); }

    private DayOfWeek parseDay(String s) {
        try { return s == null || s.isEmpty() ? null : DayOfWeek.valueOf(s); }
        catch (IllegalArgumentException ex) { return null; }
    }

    private MealType parseType(String s) {
        try { return s == null || s.isEmpty() ? null : MealType.valueOf(s); }
        catch (IllegalArgumentException ex) { return null; }
    }
}
