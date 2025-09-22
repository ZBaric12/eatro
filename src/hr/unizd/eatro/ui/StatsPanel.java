package hr.unizd.eatro.ui;

import hr.unizd.eatro.service.MealService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class StatsPanel extends JPanel {
    private final JLabel lblUkupno = new JLabel("Ukupno obroka: 0");
    private final JLabel lblVeg = new JLabel("Vegetarijanskih: 0");
    private final JLabel lblPosto = new JLabel("Udio vegetarijanskih: 0.0 %");
    private final JButton btnOsvjezi = new JButton("Osvježi");

    public StatsPanel(final MealService service) {
        setLayout(new BorderLayout(8, 8));

        JPanel top = new JPanel(new BorderLayout());
        top.add(UiKit.section("Statistika"), BorderLayout.WEST);
        top.add(btnOsvjezi, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0, 1, 8, 8));
        grid.add(lblUkupno);
        grid.add(lblVeg);
        grid.add(lblPosto);

        add(grid, BorderLayout.CENTER);

        btnOsvjezi.addActionListener(new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { reload(service); }
        });

        reload(service);
    }

    public void reload(MealService service) {
        int ukupno = service.getPlan().size();
        int veg = service.countVegetarian();
        double posto = service.vegetarianSharePercent();
        lblUkupno.setText("Ukupno obroka: " + ukupno);
        lblVeg.setText("Vegetarijanskih: " + veg);
        lblPosto.setText("Udio vegetarijanskih: " + String.format("%.1f", posto) + " %");
    }
}
