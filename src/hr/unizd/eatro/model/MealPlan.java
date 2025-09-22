package hr.unizd.eatro.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** SRP: spremnik za obroke (bez poslovne logike). */
public class MealPlan implements Serializable {
    private final List<Meal> meals = new ArrayList<>();

    public void add(Meal m) { if (m != null) meals.add(m); }
    public void remove(Meal m) { meals.remove(m); }
    public List<Meal> all() { return new ArrayList<>(meals); }
    public int size() { return meals.size(); }
}
