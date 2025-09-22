# Eatro – Plan obroka

**Eatro** je GUI aplikacija izrađena u Javi uz korištenje **Swing** biblioteke.  
Aplikacija omogućuje korisniku planiranje i pregled obroka po danima u tjednu, uz bilježenje tipa obroka (doručak, ručak, večera) i posebnih oznaka (vegetarijansko, bez glutena).  

Projekt je izrađen kao završni rad iz kolegija **Osnove objektno orijentiranog programiranja**.

---

## 🎯 Funkcionalnosti
- Kreiranje novog obroka s unosom:
  - naziva
  - sastojaka
  - dana u tjednu
  - tipa obroka (radio gumbi)
  - oznaka (checkbox)
- Pregled svih obroka u tabličnom obliku
- Pregled plana obroka po danima
- Statistika (ukupno obroka, udio vegetarijanskih…)
- Spremanje i učitavanje obroka u binarnu `.dat` datoteku (serijalizacija / deserializacija)
- Izvoz i uvoz plana u `.txt` datoteku
- Upravljanje iznimkama (pogrešan unos, prazna polja…)
- Login/registracija ekrani s prikazom korisničkog imena

---

## 🖼️ Izgled aplikacije
### Početni ekran (login/registracija)
![Login ekran](docs/screenshots/login.png)

### Glavni prozor
![Glavni prozor](docs/screenshots/main.png)

---

## 🛠️ Korišteno znanje
- **Java Swing** (`JFrame`, `JPanel`, `JButton`, `JTextField`, `JTextArea`, `JRadioButton`, `JCheckBox`, `JComboBox`, `JTable`, `JMenuBar`, `JOptionPane`…)
- **OOP principi**: enkapsulacija, SRP, model–repozitorij–servis–UI slojevi
- **Kolekcije** (`ArrayList`)
- **Enumeracije** (`MealType`, `DayOfWeek`)
- **Inner klase** (anonimne akcije, rendereri u JComboBoxu/JTable)
- **Serijalizacija i deserializacija** (`ObjectOutputStream`, `ObjectInputStream`)
- **Rukovanje iznimkama** (`try/catch`)

---

## ▶️ Pokretanje
1. Klonirati repozitorij:
```bash
   git clone https://github.com/ZBaric12/eatro.git
2. Otvoriti projekt u IntelliJ IDEA ili drugom IDE-u.
3.Pokrenuti klasu:
hr.unizd.eatro.Main

👩‍🎓 Autor

Zorica Barić
Sveučilište u Zadru, smjer Informacijske tehnologije
Kolegij: Osnove objektno orijentiranog programiranja
   ```bash
   git clone https://github.com/ZBaric12/eatro.git
