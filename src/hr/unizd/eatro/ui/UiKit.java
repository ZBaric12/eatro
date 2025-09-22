package hr.unizd.eatro.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/** Jednostavan UI "kit" za zaglavlje i razmake, bez vanjskih biblioteka. */
public final class UiKit {
    private UiKit() {}

    public static final Color GREEN = new Color(204, 230, 204);   // blago zeleno
    public static final Color TEXT = new Color(20, 20, 20);

    /** Vraća panel za zaglavlje (zelena traka s naslovom). */
    public static JComponent header(String title) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(GREEN);
        JLabel lbl = new JLabel("   " + (title == null ? "Eatro" : title));
        lbl.setForeground(TEXT);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 18f));
        p.add(lbl, BorderLayout.WEST);
        p.setBorder(new EmptyBorder(8, 8, 8, 8));
        return p;
    }

    /** Omota komponentu unutarnjim razmakom. */
    public static JComponent padded(Component inner) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new EmptyBorder(12, 12, 12, 12));
        p.add(inner, BorderLayout.CENTER);
        return p;
    }

    /** Naslov za sekcije unutar panela. */
    public static JLabel section(String text) {
        JLabel l = new JLabel(text);
        l.setFont(l.getFont().deriveFont(Font.BOLD, 14f));
        return l;
    }
}
