package hr.unizd.eatro.ui;

import hr.unizd.eatro.export.TextFileService;
import hr.unizd.eatro.model.MealPlan;
import hr.unizd.eatro.repo.MealRepository;
import hr.unizd.eatro.service.MealService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * Glavni prozor aplikacije: tabovi, izbornik, zaglavlje s pozdravom i odjavom.
 * Poslovna logika je u MealService; ovdje je samo orkestracija UI-ja.
 */
public class MainFrame extends JFrame {
    // servisni sloj i spremanje
    private final MealService service;
    private final MealRepository repo = new MealRepository();
    private final TextFileService textIO = new TextFileService();

    // paneli koje osvježavamo
    private final MealsPanel mealsPanel;
    private final PlanByDayPanel planByDayPanel;
    private final StatsPanel statsPanel;

    // ime korisnika (dobiveno iz StartFrame)
    private final String korisnickoIme;

    public MainFrame(String imeKorisnika) {
        super("Eatro – Plan obroka");
        this.korisnickoIme = imeKorisnika;
        this.service = new MealService(new MealPlan());

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 620);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header (zelena traka s brendom, pozdravom i odjavom)
        add(buildHeader(), BorderLayout.NORTH);

        // Menu
        setJMenuBar(buildMenuBar());

        // Tabovi
        JTabbedPane tabs = new JTabbedPane();

        mealsPanel = new MealsPanel(service);
        planByDayPanel = new PlanByDayPanel(service);
        statsPanel = new StatsPanel(service);

        tabs.addTab("Novi obrok", UiKit.padded(new NewMealPanel(service, new Runnable() {
            @Override public void run() { refreshAll(); }
        })));
        tabs.addTab("Moji obroci", UiKit.padded(mealsPanel));
        tabs.addTab("Plan po danu", UiKit.padded(planByDayPanel));
        tabs.addTab("Statistika", UiKit.padded(statsPanel));

        add(tabs, BorderLayout.CENTER);
    }

    /** Zelena traka s lijeve strane nazivom, desno pozdravom i gumbom Odjava. */
    private JComponent buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UiKit.GREEN);

        JLabel brand = new JLabel("   Eatro");
        brand.setFont(brand.getFont().deriveFont(Font.BOLD, 18f));
        header.add(brand, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
        right.setOpaque(false);

        JLabel welcome = new JLabel("Dobrodošao/la, " + (korisnickoIme == null ? "" : korisnickoIme));
        JButton btnOdjava = new JButton("ODJAVA");
        btnOdjava.setFocusable(false);
        btnOdjava.addActionListener(new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                // vrati na početni ekran
                new StartFrame().setVisible(true);
                dispose();
            }
        });

        right.add(welcome);
        right.add(btnOdjava);
        header.add(right, BorderLayout.EAST);

        return header;
    }

    /** Izbornik Datoteka sa Spremi/Učitaj (.dat) i Izvoz/Uvoz (.txt). */
    private JMenuBar buildMenuBar() {
        JMenuBar mb = new JMenuBar();

        JMenu file = new JMenu("Datoteka");
        JMenuItem miSpremi = new JMenuItem("Spremi .dat");
        JMenuItem miUcitaj = new JMenuItem("Učitaj .dat");
        JMenuItem miIzvoz = new JMenuItem("Izvoz .txt");
        JMenuItem miUvoz  = new JMenuItem("Uvezi .txt");

        file.add(miSpremi);
        file.add(miUcitaj);
        file.addSeparator();
        file.add(miIzvoz);
        file.add(miUvoz);

        mb.add(file);

        // SPREMI .DAT
        miSpremi.addActionListener(new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                if (fc.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
                    repo.save(service.getPlan(), ensureExt(fc.getSelectedFile(), ".dat"));
                }
            }
        });

        // UČITAJ .DAT
        miUcitaj.addActionListener(new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                if (fc.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
                    MealPlan loaded = repo.load(fc.getSelectedFile());
                    service.setPlan(loaded);
                    refreshAll();
                }
            }
        });

        // IZVOZ .TXT
        miIzvoz.addActionListener(new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                if (fc.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
                    textIO.exportTxt(service.getPlan(), ensureExt(fc.getSelectedFile(), ".txt"));
                    JOptionPane.showMessageDialog(MainFrame.this, "Izvezeno u .txt");
                }
            }
        });

        // UVOZ .TXT
        miUvoz.addActionListener(new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                if (fc.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
                    MealPlan imported = textIO.importTxt(fc.getSelectedFile());
                    service.setPlan(imported);
                    refreshAll();
                    JOptionPane.showMessageDialog(MainFrame.this, "Uvezeno iz .txt");
                }
            }
        });

        return mb;
    }

    /** Osvježi sve panele nakon dodavanja/učitavanja/uvoza. */
    private void refreshAll() {
        mealsPanel.reload(service);
        planByDayPanel.reload(service);
        statsPanel.reload(service);
    }

    /** Ako datoteka nema zadanu ekstenziju, dodaj je. */
    private File ensureExt(File f, String ext) {
        String p = f.getAbsolutePath().toLowerCase();
        if (!p.endsWith(ext)) return new File(f.getAbsolutePath() + ext);
        return f;
    }
}
