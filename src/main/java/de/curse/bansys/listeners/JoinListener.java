package de.curse.bansys.listeners;

import de.curse.bansys.banmanager.BanManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinListener implements Listener {

    @EventHandler
    public void handlePlayerJoin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if(BanManager.isBanned(player.getUniqueId().toString())) {
            long current = System.currentTimeMillis();
            long end = BanManager.getEnd(player.getUniqueId().toString());
            if (current < end | end == -1) {
                player.disconnect("§cDu wurdest vom Server gebannt!\n" +
                        "\n" +
                        "§cGrund: §4" + BanManager.getReason(player.getUniqueId().toString()) + "\n" +
                        "\n" +
                        "§cVerbleibende Zeit: §e" + BanManager.getReamingTime(player.getUniqueId().toString()) + "\n" +
                        "\n");
            }else if(current > end) {
                if(!(end == -1)) {
                    BanManager.unban(player.getUniqueId().toString());
                }
            }
        }
    }

}
