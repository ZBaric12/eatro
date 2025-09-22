package hr.unizd.eatro.model;

import java.io.Serializable;
import java.time.DayOfWeek;

public class Meal implements Serializable {
    private String naziv;
    private String sastojci;
    private DayOfWeek dan;
    private MealType tip;
    private boolean vegetarijanski;
    private boolean bezGlutena;

    public Meal() { }

    public Meal(String naziv, String sastojci, DayOfWeek dan, MealType tip,
                boolean vegetarijanski, boolean bezGlutena) {
        this.naziv = naziv;
        this.sastojci = sastojci;
        this.dan = dan;
        this.tip = tip;
        this.vegetarijanski = vegetarijanski;
        this.bezGlutena = bezGlutena;
    }

    public String getNaziv() { return naziv; }
    public void setNaziv(String naziv) { this.naziv = naziv; }

    public String getSastojci() { return sastojci; }
    public void setSastojci(String sastojci) { this.sastojci = sastojci; }

    public DayOfWeek getDan() { return dan; }
    public void setDan(DayOfWeek dan) { this.dan = dan; }

    public MealType getTip() { return tip; }
    public void setTip(MealType tip) { this.tip = tip; }

    public boolean isVegetarijanski() { return vegetarijanski; }
    public void setVegetarijanski(boolean vegetarijanski) { this.vegetarijanski = vegetarijanski; }

    public boolean isBezGlutena() { return bezGlutena; }
    public void setBezGlutena(boolean bezGlutena) { this.bezGlutena = bezGlutena; }

    @Override
    public String toString() {
        return "Meal{" +
                "naziv='" + naziv + '\'' +
                ", dan=" + dan +
                ", tip=" + tip +
                ", veg=" + vegetarijanski +
                ", bezGlutena=" + bezGlutena +
                '}';
    }
}
