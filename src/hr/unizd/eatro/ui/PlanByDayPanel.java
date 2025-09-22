package hr.unizd.eatro.ui;

import hr.unizd.eatro.model.Meal;
import hr.unizd.eatro.model.MealType;
import hr.unizd.eatro.service.MealService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel "Plan po danu" u tabličnom prikazu.
 * Kolone: Dan | Naziv | Tip | Veg | Bez glutena
 * Sadrži filter po danu (Svi dani ili jedan od dana u tjednu).
 */
public class PlanByDayPanel extends JPanel {

    private final JComboBox<Object> cbFilter; // "Svi dani" + DayOfWeek values
    private final JButton btnOsvjezi = new JButton("Osvježi");

    private final DefaultTableModel tableModel;
    private final JTable table;

    public PlanByDayPanel(final MealService service) {
        setLayout(new BorderLayout(8, 8));

        // Gornji red: naslov + filter + gumb
        JPanel top = new JPanel(new BorderLayout(8, 8));
        top.add(UiKit.section("Plan po danu"), BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        cbFilter = new JComboBox<>();
        cbFilter.addItem("Svi dani");
        for (DayOfWeek d : DayOfWeek.values()) cbFilter.addItem(d);
        cbFilter.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                                    boolean isSelected, boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof DayOfWeek) lbl.setText(hrDan((DayOfWeek) value));
                return lbl;
            }
        });
        right.add(new JLabel("Prikaži:"));
        right.add(cbFilter);
        right.add(btnOsvjezi);
        top.add(right, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        // Tablica
        String[] kolone = {"Dan", "Naziv", "Tip", "Veg", "Bez glutena"};
        tableModel = new DefaultTableModel(kolone, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
            // kako bi se dani sortirali po tjednu (ako ikad dodaš sorter)
            @Override public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? DayOfWeek.class : String.class;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(24);
        // prikaz HR naziva dana u tabeli
        table.getColumnModel().getColumn(0).setCellRenderer((tbl, value, isSelected, hasFocus, row, col) -> {
            JLabel l = new JLabel();
            l.setOpaque(true);
            if (isSelected) {
                l.setBackground(tbl.getSelectionBackground());
                l.setForeground(tbl.getSelectionForeground());
            } else {
                l.setBackground(tbl.getBackground());
                l.setForeground(tbl.getForeground());
            }
            if (value instanceof DayOfWeek) l.setText(hrDan((DayOfWeek) value));
            return l;
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Akcije
        btnOsvjezi.addActionListener(new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                reload(service);
            }
        });
        cbFilter.addActionListener(new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                reload(service);
            }
        });

        // inicijalno punjenje
        reload(service);
    }

    /** Napuni tablicu podacima, uz poštivanje filtera dana. */
    public void reload(MealService service) {
        tableModel.setRowCount(0);

        Object filter = cbFilter.getSelectedItem();
        List<Meal> source;
        if (filter instanceof DayOfWeek) {
            source = service.byDay((DayOfWeek) filter);
        } else {
            source = service.all();
        }

        // Ako je "Svi dani", želimo redove poredati po danima u tjednu
        List<Meal> ordered = new ArrayList<>();
        if (!(filter instanceof DayOfWeek)) {
            for (DayOfWeek d : DayOfWeek.values()) {
                for (Meal m : source) if (m.getDan() == d) ordered.add(m);
            }
            // dodaj sve bez dan-a (ako postoje)
            for (Meal m : source) if (m.getDan() == null) ordered.add(m);
        } else {
            ordered = source;
        }

        for (Meal m : ordered) {
            tableModel.addRow(new Object[] {
                    m.getDan(),
                    safe(m.getNaziv()),
                    hrTip(m.getTip()),
                    m.isVegetarijanski() ? "Da" : "Ne",
                    m.isBezGlutena() ? "Da" : "Ne"
            });
        }
    }

    private String safe(String s) { return s == null ? "" : s; }

    private String hrDan(DayOfWeek d) {
        if (d == null) return "";
        switch (d) {
            case MONDAY: return "Ponedjeljak";
            case TUESDAY: return "Utorak";
            case WEDNESDAY: return "Srijeda";
            case THURSDAY: return "Četvrtak";
            case FRIDAY: return "Petak";
            case SATURDAY: return "Subota";
            case SUNDAY: return "Nedjelja";
            default: return d.name();
        }
    }

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
