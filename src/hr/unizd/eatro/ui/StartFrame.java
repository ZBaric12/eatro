package hr.unizd.eatro.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Početni ekran (registracija/prijava). Ne sprema korisnika u bazu;
 * samo prosljeđuje upisano ime u glavni prozor (MainFrame).
 */
public class StartFrame extends JFrame {

    private final JTextField tfIme = new JTextField();
    private final JTextField tfEmail = new JTextField();
    private final JPasswordField pfLozinka = new JPasswordField();
    private final JButton btnPrijavi = new JButton("PRIJAVI SE");

    private final JLabel naslov = new JLabel("Stvori novi račun", SwingConstants.CENTER);
    private final JLabel link = new JLabel("<html><u>Već imaš račun? Prijavi se</u></html>", SwingConstants.CENTER);

    private boolean loginMode = false; // false=registracija, true=prijava

    public StartFrame() {
        super("Eatro");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(760, 520);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Zelena traka (header)
        add(UiKit.header("Eatro"), BorderLayout.NORTH);

        // Središnji panel (forma)
        JPanel center = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 8, 8, 8);
        gc.fill = GridBagConstraints.HORIZONTAL;

        naslov.setFont(naslov.getFont().deriveFont(Font.BOLD, 28f));
        link.setForeground(new Color(33, 75, 150));
        link.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        gc.gridx = 0; gc.gridy = 0; gc.gridwidth = 2;
        center.add(naslov, gc);
        gc.gridy++;
        center.add(link, gc);

        gc.gridwidth = 1; gc.gridy++;
        center.add(new JLabel("IME"), gc);
        gc.gridx = 1; tfIme.setPreferredSize(new Dimension(300, 28));
        center.add(tfIme, gc);

        gc.gridx = 0; gc.gridy++;
        center.add(new JLabel("EMAIL"), gc);
        gc.gridx = 1;
        center.add(tfEmail, gc);

        gc.gridx = 0; gc.gridy++;
        center.add(new JLabel("LOZINKA"), gc);
        gc.gridx = 1;
        center.add(pfLozinka, gc);

        gc.gridx = 0; gc.gridy++;
        gc.gridwidth = 2;
        btnPrijavi.setPreferredSize(new Dimension(180, 34));
        center.add(btnPrijavi, gc);

        add(UiKit.padded(center), BorderLayout.CENTER);

        // Promjena moda (registracija ↔ prijava)
        link.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) { toggleMode(); }
        });

        // Akcija prijave/registracije
        btnPrijavi.addActionListener(new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                String ime = tfIme.getText().trim();
                String email = tfEmail.getText().trim();
                String loz = new String(pfLozinka.getPassword());

                if (email.isEmpty() || loz.isEmpty() || (!loginMode && ime.isEmpty())) {
                    JOptionPane.showMessageDialog(StartFrame.this,
                            "Molimo popunite sva polja.", "Podaci nisu potpuni",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Otvori glavni prozor i proslijedi ime
                new MainFrame(ime).setVisible(true);
                dispose();
            }
        });

        applyMode(); // inicijalno: registracija
    }

    private void toggleMode() { loginMode = !loginMode; applyMode(); }

    private void applyMode() {
        if (loginMode) {
            naslov.setText("Prijava");
            link.setText("<html><u>Nemaš račun? Registriraj se</u></html>");
            tfIme.setEnabled(false);
            tfIme.setText("");
        } else {
            naslov.setText("Stvori novi račun");
            link.setText("<html><u>Već imaš račun? Prijavi se</u></html>");
            tfIme.setEnabled(true);
        }
    }
}
