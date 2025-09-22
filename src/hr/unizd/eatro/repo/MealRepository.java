package hr.unizd.eatro.repo;

import hr.unizd.eatro.model.MealPlan;
import java.io.*;

/** SRP: serijalizacija .dat s osnovnim try/catch. */
public class MealRepository {

    public void save(MealPlan plan, File file) {
        if (plan == null || file == null) return;
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(plan);
            oos.flush();
        } catch (IOException e) {
            System.err.println("Greška pri spremanju: " + e.getMessage());
        } finally {
            if (oos != null) try { oos.close(); } catch (IOException ignored) {}
        }
    }

    public MealPlan load(File file) {
        if (file == null || !file.exists()) return new MealPlan();
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(file));
            Object obj = ois.readObject();
            if (obj instanceof MealPlan) return (MealPlan) obj;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Greška pri učitavanju: " + e.getMessage());
        } finally {
            if (ois != null) try { ois.close(); } catch (IOException ignored) {}
        }
        return new MealPlan();
    }
}
