package de.curse.bansys;

import de.curse.bansys.mysql.MySQL;
import net.md_5.bungee.api.plugin.Plugin;

public final class BanSystem extends Plugin {

    public static MySQL mysql;

    @Override
    public void onEnable() {
        ConnectMySQL();
    }

    public void ConnectMySQL() {
        mysql = new MySQL(this, "localhost", "BanSystem", "root", "");
        mysql.update("CREATE TABLE IF NOT EXISTS BannedPlayers(Name VARCHAR(40), UUID VARCHAR(100), Ende VARCHAR(100), Grund VARCHAR(100))");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
