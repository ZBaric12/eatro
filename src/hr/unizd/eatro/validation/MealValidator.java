package hr.unizd.eatro.validation;

import hr.unizd.eatro.model.Meal;
import java.util.ArrayList;
import java.util.List;

/** SRP: provjera podataka odvojena od domene i UI-a. */
public class MealValidator {
    public static List<String> validate(Meal m) {
        List<String> errors = new ArrayList<>();
        if (m == null) { errors.add("Obrok je null"); return errors; }
        if (m.getNaziv() == null || m.getNaziv().trim().isEmpty())
            errors.add("Naziv je obavezan");
        if (m.getDan() == null)
            errors.add("Dan je obavezan");
        if (m.getTip() == null)
            errors.add("Tip obroka je obavezan");
        return errors;
    }
}
