package de.curse.bansys.mysql;

import de.curse.bansys.BanSystem;

import java.sql.*;
public class MySQL {

    private final BanSystem plugin;

    private String HOST = "";
    private String DATABASE = ";";
    private String USER = "";
    private String PASSWORD = "";

    private Connection con;

    public MySQL(BanSystem plugin, String HOST, String DATABASE, String USER, String PASSWORD) {
        this.plugin = plugin;
        this.HOST = HOST;
        this.DATABASE = DATABASE;
        this.USER = USER;
        this.PASSWORD = PASSWORD;

        connect();
    }

    public void connect() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + HOST + ":3306/" + DATABASE + "?autoReconnect=true", USER, PASSWORD);
            System.out.println("[MySQL] Die Verbindung wurde hergestellt!");
        } catch (SQLException e) {
            System.out.println("[MySQL] Die Verbindung zur MySQL ist fehlgeschlagen! Fehler: " + e.getMessage());
        }
    }

    public void close(){
        try{
            if(con != null){
                con.close();
                System.out.println("[MySQL] Die Verbindung zur MySQL wurde erfolgreich beendet!");
            }
        }catch(SQLException e){
            System.out.println("[MySQL] Fehler beim beenden der Verbindung zu MySQL! Fehler: " + e.getMessage());
        }
    }

    public void update(String qry){
        try{
            Statement st = con.createStatement();
            st.executeUpdate(qry);
            st.close();
        }catch(SQLException e){
            connect();
            System.err.println(e);
        }
    }

    public ResultSet query(String qry){
        ResultSet rs = null;

        try{
            Statement st = con.createStatement();
            rs = st.executeQuery(qry);
        }catch(SQLException e){
            connect();
            System.err.println(e);
        }
        return rs;
    }
}