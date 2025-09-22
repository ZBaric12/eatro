package hr.unizd.eatro.ui;

import hr.unizd.eatro.model.Meal;
import hr.unizd.eatro.model.MealType;
import hr.unizd.eatro.service.MealService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class NewMealPanel extends JPanel {
    private final JTextField tfNaziv = new JTextField();
    private final JTextArea  taSastojci = new JTextArea(5, 20);
    private final JComboBox<DayOfWeek> cbDan = new JComboBox<>(DayOfWeek.values());
    private final JRadioButton rbDorucak = new JRadioButton("Doručak");
    private final JRadioButton rbRucak   = new JRadioButton("Ručak");
    private final JRadioButton rbVecera  = new JRadioButton("Večera");
    private final JCheckBox chVeg = new JCheckBox("Vegetarijansko");
    private final JCheckBox chGF  = new JCheckBox("Bez glutena");
    private final JButton btnDodaj = new JButton("Dodaj obrok");

    public NewMealPanel(final MealService service, final Runnable onAdded) {
        setLayout(new BorderLayout(10,10));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(6,6,6,6));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6,6,6,6);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.NORTHWEST;

        // renderer HR naziva dana
        cbDan.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                                    boolean isSelected, boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof DayOfWeek) lbl.setText(hrDan((DayOfWeek)value));
                return lbl;
            }
        });
        cbDan.setSelectedItem(LocalDate.now().getDayOfWeek());

        // 1) Osnovno
        gc.gridx=0; gc.gridy=0; gc.gridwidth=2;
        form.add(UiKit.section("Osnovni podaci"), gc);

        gc.gridwidth=1; gc.gridy++;
        form.add(new JLabel("Naziv:"), gc);
        gc.gridx=1; tfNaziv.setPreferredSize(new Dimension(280,28));
        form.add(tfNaziv, gc);

        gc.gridx=0; gc.gridy++;
        form.add(new JLabel("Sastojci:"), gc);
        gc.gridx=1;
        taSastojci.setLineWrap(true); taSastojci.setWrapStyleWord(true);
        form.add(new JScrollPane(taSastojci), gc);

        gc.gridx=0; gc.gridy++;
        form.add(new JLabel("Dan:"), gc);
        gc.gridx=1; form.add(cbDan, gc);

        // 2) Tip i oznake
        gc.gridx=0; gc.gridy++; gc.gridwidth=2;
        form.add(UiKit.section("Tip i oznake"), gc);

        ButtonGroup group = new ButtonGroup();
        group.add(rbDorucak); group.add(rbRucak); group.add(rbVecera);
        rbRucak.setSelected(true);

        JPanel tip = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        tip.add(rbDorucak); tip.add(rbRucak); tip.add(rbVecera);

        gc.gridwidth=1; gc.gridy++;
        gc.gridx=0; form.add(new JLabel("Tip obroka:"), gc);
        gc.gridx=1; form.add(tip, gc);

        JPanel ozn = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        ozn.add(chVeg); ozn.add(chGF);
        gc.gridx=0; gc.gridy++;
        form.add(new JLabel("Oznake:"), gc);
        gc.gridx=1; form.add(ozn, gc);

        add(form, BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnDodaj.setPreferredSize(new Dimension(150, 30));
        south.add(btnDodaj);
        add(south, BorderLayout.SOUTH);

        btnDodaj.addActionListener(new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                Meal m = new Meal();
                m.setNaziv(tfNaziv.getText());
                m.setSastojci(taSastojci.getText());
                m.setDan((DayOfWeek) cbDan.getSelectedItem());
                if (rbDorucak.isSelected())      m.setTip(MealType.DORUCAK);
                else if (rbVecera.isSelected())  m.setTip(MealType.VECERA);
                else                              m.setTip(MealType.RUCAK);
                m.setVegetarijanski(chVeg.isSelected());
                m.setBezGlutena(chGF.isSelected());

                List<String> errors = service.addMeal(m);
                if (errors.isEmpty()) {
                    JOptionPane.showMessageDialog(NewMealPanel.this, "Obrok dodan.");
                    if (onAdded != null) onAdded.run();
                    clear();
                } else {
                    JOptionPane.showMessageDialog(NewMealPanel.this,
                            String.join("\n", errors),
                            "Provjera podataka",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    private void clear() {
        tfNaziv.setText("");
        taSastojci.setText("");
        rbRucak.setSelected(true);
        chVeg.setSelected(false);
        chGF.setSelected(false);
        tfNaziv.requestFocusInWindow();
    }

    private String hrDan(DayOfWeek d) {
        switch (d) {
            case MONDAY:    return "Ponedjeljak";
            case TUESDAY:   return "Utorak";
            case WEDNESDAY: return "Srijeda";
            case THURSDAY:  return "Četvrtak";
            case FRIDAY:    return "Petak";
            case SATURDAY:  return "Subota";
            case SUNDAY:    return "Nedjelja";
            default:        return d.name();
        }
    }
}
