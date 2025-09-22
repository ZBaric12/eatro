package hr.unizd.eatro.ui;

import hr.unizd.eatro.model.Meal;
import hr.unizd.eatro.model.MealType;
import hr.unizd.eatro.service.MealService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Panel za prikaz obroka u tablici i uklanjanje označenog obroka.
 * Kolone: Naziv | Sastojci | Tip | Veg | Bez glutena
 */
public class MealsPanel extends JPanel {
    private final DefaultTableModel tableModel;
    private final JTable table;

    public MealsPanel(final MealService service) {
        setLayout(new BorderLayout(8, 8));

        // Naslov i gumbi
        JPanel top = new JPanel(new BorderLayout());
        top.add(UiKit.section("Moji obroci"), BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        JButton btnOsvjezi = new JButton("Osvježi");
        JButton btnUkloni = new JButton("Ukloni označeno");
        actions.add(btnOsvjezi);
        actions.add(btnUkloni);
        top.add(actions, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);

        // Tablica (dodana kolona "Sastojci")
        String[] kolone = {"Naziv", "Sastojci", "Tip", "Veg", "Bez glutena"};
        tableModel = new DefaultTableModel(kolone, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(24);

        // širi stupac za sastojke
        table.getColumnModel().getColumn(1).setPreferredWidth(250);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Gumbi akcija
        btnOsvjezi.addActionListener(new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                reload(service);
            }
        });

        btnUkloni.addActionListener(new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                int selRow = table.getSelectedRow();
                if (selRow >= 0) {
                    Meal selMeal = service.all().get(selRow);
                    int ans = JOptionPane.showConfirmDialog(MealsPanel.this,
                            "Želite li ukloniti obrok \"" + selMeal.getNaziv() + "\"?",
                            "Potvrda brisanja",
                            JOptionPane.YES_NO_OPTION);
                    if (ans == JOptionPane.YES_OPTION) {
                        service.remove(selMeal);
                        reload(service);
                    }
                }
            }
        });

        // inicijalno punjenje
        reload(service);
    }

    /** Napuni tablicu podacima iz servisa. */
    public void reload(MealService service) {
        tableModel.setRowCount(0);
        for (Meal m : service.all()) {
            Object[] row = {
                    safe(m.getNaziv()),
                    safe(m.getSastojci()),
                    hrTip(m.getTip()),
                    m.isVegetarijanski() ? "Da" : "Ne",
                    m.isBezGlutena() ? "Da" : "Ne"
            };
            tableModel.addRow(row);
        }
    }

    private String safe(String s) { return s == null ? "" : s; }

    private String hrTip(MealType t) {
        if (t == null) return "";
        switch (t) {
            case DORUCAK: return "Doručak";
            case RUCAK:   return "Ručak";
            case VECERA:  return "Večera";
            default:      return t.name();
        }
    }
}
