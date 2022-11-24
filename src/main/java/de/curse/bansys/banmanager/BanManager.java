package de.curse.bansys.banmanager;

import de.curse.bansys.BanSystem;
import net.md_5.bungee.api.ProxyServer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BanManager {

    public static List<String> bannedPlayers = new ArrayList<>();

    public static void ban(String uuid, String playername, String reason, long seconds) {
        long end = 0;
        if(seconds == -1) {
            end = -1;
        }else {
            long current = System.currentTimeMillis();
            long millis = seconds * 1000;
            end = current + millis;
        }
        BanSystem.mysql.update("INSERT INTO BannedPlayers(NAME, UUID, ENDE, GRUND) VALUES ('" + playername + "','" + uuid + "','" + end + "','" + reason + "')");
        if(ProxyServer.getInstance().getPlayer(playername) != null) {
            ProxyServer.getInstance().getPlayer(playername).disconnect("§cDu wurdest vom Server gebannt!\n" +
                    "\n" +
                    "§cGrund: §4" + getReason(uuid) + "\n" +
                    "\n" +
                    "§cVerbleibende Zeit: §e" + getReamingTime(uuid) + "\n" +
                    "\n");
        }
    }

    public static void unban(String uuid) {
        BanSystem.mysql.update("DELETE FROM BannedPlayers WHERE UUID='" + uuid + "'");
    }

    public static boolean isBanned(String uuid) {
        ResultSet rs = BanSystem.mysql.query("SELECT * FROM BannedPlayers WHERE UUID='" + uuid + "'");
        try{
            while(rs.next()) {
                return rs != null;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getReason(String uuid) {
        ResultSet rs = BanSystem.mysql.query("SELECT * FROM BannedPlayers WHERE UUID='" + uuid + "'");
        try{
            while(rs.next()) {
                return rs.getString("GRUND");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Long getEnd(String uuid) {
        ResultSet rs = BanSystem.mysql.query("SELECT * FROM BannedPlayers WHERE UUID='" + uuid + "'");
        try{
            while(rs.next()) {
                return rs.getLong("ENDE");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getBannedPlayers() {
        List<String> list = new ArrayList<>();
        ResultSet rs = BanSystem.mysql.query("SELECT * FROM BannedPlayers");
        try {
            while(rs.next()) {
                list.add(rs.getString("UUID"));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String getReamingTime(String uuid) {
        long current = System.currentTimeMillis();
        long end = getEnd(uuid);
        long millis = end - current;
        if(end == -1) {
            return "§4PERMANENT";
        }

        long seconds = 0;
        long minutes = 0;
        long hours = 0;
        long days = 0;

        while(millis > 1000) {
            millis-=1000;
            seconds++;
        }while(seconds > 60) {
            seconds-=60;
            minutes++;
        }while(minutes > 60) {
            minutes-=60;
            hours++;
        }while(hours > 24) {
            hours-=24;
            days++;
        }        return "§e" + days + " Tag(e) " + hours + " Stunde(n) " + minutes + " Minute(n) " + seconds + " Sekunde(n)";

    }

    public static String getUUID(String playername) {
        String i = "";
        try {
            ResultSet rs = BanSystem.mysql.query("SELECT * FROM BannedPlayers WHERE NAME= '" + playername + "'");

            if ((!rs.next()) || (String.valueOf(rs.getString("UUID")) == null))
                ;

            i = rs.getString("UUID");

        } catch (SQLException e) {

        }
        return i;
    }
}
