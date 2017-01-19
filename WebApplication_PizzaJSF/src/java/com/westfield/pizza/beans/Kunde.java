/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.westfield.pizza.beans;

import com.westfield.pizza.dao.DataAccess;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;


/**
 *
 * @author John Westfield
 */
@ManagedBean
@SessionScoped
public class Kunde extends DataAccess {
    
     /*** Validation ***/
    private List<String> errors = new LinkedList<String>();
    private boolean valid = false;
    
    private int kundennummer;
    private String email;
    private String password;
    private String vorname;
    private String nachname;
    private String ort;
    private String plz;
    private String strasse;
    
    public Kunde(){        
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password.trim();
    }
       
    public int getKundennummer() {
        return kundennummer;
    }

    public void setKundennummer(String nummer){
        int kundennummer;
        try {
            kundennummer = Integer.parseInt(nummer);
        } catch (NumberFormatException e) {
            System.out.println("Kunde: NumberFormatException - keine Zahl aus 'String nummer': " + nummer);
            this.kundennummer = 0; 
            return;
        }
        this.kundennummer = kundennummer;  
    }     
    
    public void setKundennummer(int kundennummer) {
        this.kundennummer = kundennummer;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname.trim();
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname.trim();
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort.trim();
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz.trim();
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse.trim();
    }
    
    public int getNewKundennummer(){
        Set<Integer> kdnr = snatchKundennummern();
        int length = kdnr.size();
        int newkdnr = 0;
        Random rand = new Random();
        while (length == kdnr.size()){
            newkdnr = rand.nextInt(Integer.MAX_VALUE);
            kdnr.add(new Integer(newkdnr));              
        }
        return newkdnr;
        
    }
     
    public boolean checkEmail(String email){    
        Set emailAdressen = snatchEmail();
        if(emailAdressen.contains(email)){
            return true;
        }else{
            return false;
        }        
    }
    
    public boolean checkEmail(){  
        
        return this.checkEmail(this.email);
       
    }
    
    public boolean checkKundennummer(int nummer){    
        Set kdnr = snatchKundennummern();
        if(kdnr.contains(new Integer(nummer))){
            return true;
        }else{
            return false;
        }        
    }
    
    public boolean checkKundennummer(){  
        
        return this.checkKundennummer(this.kundennummer);
       
    }
    
    public boolean checkKundennummer(String nummer){ 
        
        int kundennummer;
        try {
           kundennummer = Integer.parseInt(nummer);
        } catch (NumberFormatException e) {
              return false;
        }
        return this.checkKundennummer(kundennummer); 
    }   
   
    public Set<Integer> snatchKundennummern(){
        Connection con = null;
        Statement stm = null;
        Set<Integer> kdnr = new HashSet<>();
        ResultSet rs = null;

        try {
            
            con = getConnectionPool();
            
             if (con == null) {
                return null;
            }
            stm = con.createStatement();
            rs = stm.executeQuery("SELECT kundennummer FROM benutzer");
            
            while (rs.next()) {
                kdnr.add((Integer)rs.getInt("kundennummer"));                   
            }

        } catch (SQLException ex) {
            
        } finally {
            try { if (rs != null) { rs.close(); } } catch (Exception e) {}
            try { if (stm != null) { stm.close(); } } catch (Exception e) {}
            try { if (con != null) { con.close(); } } catch (Exception e) {}
        }
        return kdnr;
    }
    
    public Set<String> snatchEmail(){
        Connection con = null;
        Statement stm = null;
        Set<String> kdnemail = new HashSet<>();
        ResultSet rs = null;

        try {
            
            con = getConnectionPool();
            
             if (con == null) {
                return null;
            }
            stm = con.createStatement();
            rs = stm.executeQuery("SELECT email FROM benutzer");
            
            while (rs.next()) {
                kdnemail.add(rs.getString("email").trim());                   
            }

        } catch (SQLException ex) {
            
        } finally {
            try { if (rs != null) { rs.close(); } } catch (Exception e) {}
            try { if (stm != null) { stm.close(); } } catch (Exception e) {}
            try { if (con != null) { con.close(); } } catch (Exception e) {}
        }
        return kdnemail;
    }
    
    public boolean store(){
        
         if(this.checkKundennummer(this.getKundennummer())){ 
            return this.updateKunde(); 
         } else {
            return this.insertKunde();            
         }
    }
    
    public boolean updateKunde(){
        Connection con = null;
        PreparedStatement stm = null;
        boolean stored = false;
        try {
            //System.out.println("Kunde update() - start");
            con = this.getConnectionPool();
            if(con == null) {
                //System.out.println("Kunde update() - no Connection Pool");
                return false;
            }
            stm = con.prepareStatement("UPDATE benutzer SET email = ?, password = ?, vorname = ?, nachname = ?, strasse = ?, ort = ?, plz = ? WHERE kundennummer = ?");
            stm.setString(1, this.getEmail());
            stm.setString(2, this.getPassword());
            stm.setString(3, this.getVorname());
            stm.setString(4, this.getNachname());
            stm.setString(5, this.getStrasse());
            stm.setString(6, this.getOrt());
            stm.setString(7, this.getPlz());
            stm.setInt(8, this.getKundennummer());
            int rows = stm.executeUpdate();
            con.commit();
            stored = rows == 1;
        } catch (SQLException ex) {          
            stored = false;
        } finally {
            try { if( stm != null) stm.close(); } catch(Exception e) {}
            try { if( con != null) con.close(); } catch(Exception e) {}
        }
        return stored;
    }
          
    public boolean insertKunde(){
        Connection con = null;
        PreparedStatement stm = null;
        boolean stored = false;
        try {
            //System.out.println("Kunde store() - start");
            con = this.getConnectionPool();
            if(con == null) {
                //System.out.println("Kunde store() - no Connection Pool");
                return false;
            }
            stm = con.prepareStatement("INSERT INTO benutzer (email, password, vorname, nachname, strasse, ort, plz, kundennummer) VALUES(?,?,?,?,?,?,?,?)");
           
            stm.setString(1, this.getEmail());
            stm.setString(2, this.getPassword());
            stm.setString(3, this.getVorname());
            stm.setString(4, this.getNachname());
            stm.setString(5, this.getStrasse());
            stm.setString(6, this.getOrt());
            stm.setString(7, this.getPlz());
            stm.setInt(8, this.getKundennummer());
            int rows = stm.executeUpdate();
            con.commit();
            stored = rows == 1;
        } catch (SQLException ex) {          
            stored = false;
        } finally {
            try { if( stm != null) stm.close(); } catch(Exception e) {}
            try { if( con != null) con.close(); } catch(Exception e) {}
        }
        return stored;
    }
    
    public Kunde snatch (String email) {
        
        Connection con = null;
        Statement stm = null;        
        ResultSet rs = null;  
        
        System.out.println("Kunde:snatch ("+ email + ")");
        
        try {
            
            con = getConnectionPool();
            
             if (con == null) {
                return null;
            }
            stm = con.createStatement();
            rs = stm.executeQuery("SELECT * FROM benutzer WHERE email = '" + email + "'");
            
            while (rs.next()) {
                
                this.kundennummer = rs.getInt("kundennummer");
                this.vorname = rs.getString("vorname");
                this.nachname = rs.getString("nachname");
                this.ort = rs.getString("ort");
                this.plz = rs.getString("plz");
                this.strasse = rs.getString("strasse");
                this.email = rs.getString("email");
                this.password = rs.getString("password");     
            }

        } catch (SQLException ex) {
            ex.printStackTrace();       
            System.out.println("Kunde:snatch ("+ email + ") - ERROR");
            return this;
            
        } finally {
            
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if( stm != null) stm.close(); } catch(Exception e) {}
            try { if( con != null) con.close(); } catch(Exception e) {}  
               
        }
        
        System.out.println("Kunde:snatch ("+ this.email + ") + SUCCESS");
        
        return this;
    }
    
////    public Kunde snatch (String nummer) {
//        
//        int kundennummer;
//        try {
//           kundennummer = Integer.parseInt(nummer);
//        } catch (NumberFormatException e) {
//              return new Kunde();
//        }
//        return this.snatch(kundennummer); 
//    }
    
   public Kunde snatch (int kundennummer) {
        Connection con = null;
        Statement stm = null;        
        ResultSet rs = null;

        try {
            
            con = getConnectionPool();
            
             if (con == null) {
                return null;
            }
            stm = con.createStatement();
            rs = stm.executeQuery("SELECT * FROM benutzer WHERE kundennummer = '" + kundennummer + "'");
            
            while (rs.next()) {
                
                this.kundennummer = rs.getInt("kundennummer");
                this.vorname = rs.getString("vorname");
                this.nachname = rs.getString("nachname");
                this.ort = rs.getString("ort");
                this.plz = rs.getString("plz");
                this.strasse = rs.getString("strasse");
                this.email = rs.getString("email");
                this.password = rs.getString("password");     
            }

        } catch (SQLException ex) {
            
        } finally {
            
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if( stm != null) stm.close(); } catch(Exception e) {}
            try { if( con != null) con.close(); } catch(Exception e) {}            
           
        }
        return this;
    }
    
        
   /*** Validation ***/

   public List<String> getErrors() {
      return errors;
   }   
   
   public boolean isValid() {
      return valid;
   }

   /* input validation */
   
   public void validate_kundennumer() {
        
         // Zurücksetzen des Flags und der Fehlerliste
        this.valid = true;
        this.errors = new LinkedList<String>();
        
        // Überprüfe die eingegebene Kundennummer
        if (kundennummer == 0){
            
            this.valid = false;
            this.errors.add("Bitte geben sie eine gültige Kundennumer an.");
            
        }
        
    }
   
   public void validate() {
       
        // Initialization methid as well!!
        this.validate_kundennumer(); 
   
        // Überprüfe den eingegebenen Vornamen
        if (vorname == null || vorname.length() == 0) {
           this.valid = false;
           this.errors.add("Bitte geben sie einen gültigen Vornamen an.");
        }

        // Überprüfe den eingegebenen Nachnamen
        if (nachname == null || nachname.length() == 0) {
           this.valid = false;
           this.errors.add("Bitte geben sie einen gültigen Nachnamen an.");
        }

        // Überprüfe den eingegebenen Ort
        if (ort == null || ort.length() == 0) {
           this.valid = false;
           this.errors.add("Bitte geben sie einen gültigen Ortsnamen an.");
        }

         // Überprüfe den eingegebenen Plz
        if (plz == null || plz.length() == 0 || plz.length() > 5) {
           this.valid = false;
           this.errors.add("Bitte geben sie eine gültige Postleitzahl an. (5-stellig)");
        }

        // Überprüfe die Syntax des Strassennamen
        if (strasse == null || strasse.length() == 0) {
           this.valid = false;
           this.errors.add("Bitte geben sie einen gültigen Strassennamen an.");
        }
     
   }
    
    
}
