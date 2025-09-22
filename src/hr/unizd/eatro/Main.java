package hr.unizd.eatro;

import hr.unizd.eatro.ui.StartFrame;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                new StartFrame().setVisible(true);
            }
        });
    }
}
