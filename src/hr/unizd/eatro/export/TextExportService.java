package hr.unizd.eatro.export;

import hr.unizd.eatro.model.Meal;
import hr.unizd.eatro.model.MealPlan;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/** SRP: izvoz plana u običan .txt */
public class TextExportService {
    public void exportTxt(MealPlan plan, File file) {
        if (plan == null || file == null) return;
        FileWriter fw = null;
        try {
            fw = new FileWriter(file);
            fw.write("Eatro – Plan obroka\n\n");
            for (Meal m : plan.all()) {
                fw.write(m.toString());
                fw.write("\n");
            }
            fw.flush();
        } catch (IOException e) {
            System.err.println("Greška pri izvozu: " + e.getMessage());
        } finally {
            if (fw != null) try { fw.close(); } catch (IOException ignored) {}
        }
    }
}
