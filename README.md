# Sistem de Admitere la Facultate 🏛️

Aceasta este o aplicație desktop Java care gestionează procesul de admitere la facultate. Permite autentificarea utilizatorilor în două roluri — **Student** și **Admin** — și oferă funcționalități pentru gestionarea candidaților, afișarea statusurilor de admitere și generarea de rapoarte.

## 🔧 Tehnologii utilizate

- **Java (Swing)** – interfață grafică
- **MySQL** – sistemul de gestiune al bazei de date
- **JDBC** – conectarea aplicației Java la baza de date
- **iText** – generarea fișierelor PDF
- **CSV** – import/export de date
- **Java AWT / IO / util** – funcționalități suplimentare

## 🧠 Funcționalități principale

### Student

- Autentificare pe baza datelor personale (Nume, Prenume, CNP)
- Vizualizare status (admis / respins)
- Vizualizare detalii despre opțiunea aleasă

### Admin

- Autentificare cu username și parolă
- Inserare / modificare / ștergere candidați
- Import / export de date (CSV și PDF)
- Actualizare statusuri în funcție de note și locuri disponibile
- Generare grafice de tip pie-chart privind situația admiterii

## 🧱 Structura bazei de date

### Tabele principale

- `student(idStudent, Nume, Prenume, CNP, Nota, idFacultateOptiune, Optiune)`
- `facultate(idFacultate, Nume_Facultate, Adresa, numar_locuri)`
- `admitere_status(idStudent, idFacultate, status)`
- `admin(idAdmin, username, password, last_login)`

Include relații și chei externe pentru integritate referențială.

## 🖼️ Interfață

Aplicația este împărțită în două panouri:

- **AuthenticationPanel** – logarea utilizatorilor
- **DataBase_UI_Admin** și **DataBase_UI_Student** – interfețe dedicate fiecărui rol

## 📥 Import/Export

- CSV: permite import rapid de candidați și export al datelor în format compatibil cu Excel
- PDF: generează rapoarte de status al admiterii
- Grafic: vizualizare procentuală a locurilor ocupate vs. disponibile

## 🛡️ Validări și tratări de excepții

- Verificări pe CNP, ID, note și opțiuni valide
- Gestionare excepții de conectare la baza de date
- Feedback clar pentru utilizator (mesaje cu `JOptionPane`)

## 🚀 Cum rulezi aplicația

1. Asigură-te că ai Java și MySQL instalate.
2. Creează baza de date folosind scripturile din documentație.
3. Adaugă driverul `mysql-connector-j` în classpath (din libs).
4. Rulează aplicația dintr-un IDE (NetBeans, IntelliJ etc.).
5. Loghează-te ca Admin sau Student.

## 🧪 Posibile îmbunătățiri

- Portare la o aplicație web (Spring Boot + React)
- Autentificare mai sigură (hashing parole)
- Teste unitare și modularizare extinsă

## 📚 Bibliografie

- [Java Swing – Oracle Docs](https://docs.oracle.com/javase/7/docs/api/javax/swing/)
- [GFG – Java Swing](https://www.geeksforgeeks.org/introduction-to-java-swing/)
- [W3Schools Java](https://www.w3schools.com/java/)
- ChatGPT 😄

---

## TL;DR

Aplicație Java pentru admitere la facultate, cu două roluri (Admin + Student), interfață Swing, baze de date MySQL, import/export CSV, export PDF și grafic de tip pie chart. Ușor de extins, clar structurat, good job!
