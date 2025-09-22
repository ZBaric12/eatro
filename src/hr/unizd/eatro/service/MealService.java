package hr.unizd.eatro.service;

import hr.unizd.eatro.model.Meal;
import hr.unizd.eatro.model.MealPlan;
import hr.unizd.eatro.validation.MealValidator;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

/** SRP: sva poslovna logika nad planom. */
public class MealService {
    private MealPlan plan;

    public MealService(MealPlan plan) { this.plan = plan; }

    public List<String> addMeal(Meal m) {
        List<String> errors = MealValidator.validate(m);
        if (errors.isEmpty()) plan.add(m);
        return errors;
    }

    public void remove(Meal m) { plan.remove(m); }

    public List<Meal> all() { return plan.all(); }

    public List<Meal> byDay(DayOfWeek day) {
        List<Meal> result = new ArrayList<>();
        for (Meal m : plan.all()) {
            if (m.getDan() == day) result.add(m);
        }
        return result;
    }

    public int countVegetarian() {
        int count = 0;
        for (Meal m : plan.all()) if (m.isVegetarijanski()) count++;
        return count;
    }

    public double vegetarianSharePercent() {
        if (plan.size() == 0) return 0.0;
        return (countVegetarian() * 100.0) / plan.size();
    }

    public MealPlan getPlan() { return plan; }
    public void setPlan(MealPlan p) { this.plan = (p != null) ? p : new MealPlan(); }
}
