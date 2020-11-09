package net.kursmc.randomspawn.randomspawn.listeners;

import net.kursmc.randomspawn.randomspawn.TeleportUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class NewPlayer implements Listener {

    @EventHandler
    public boolean onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        if (!player.hasPlayedBefore()) {
            Location randomLocation = TeleportUtils.findSafeLocation(player);
            player.teleport(randomLocation);
            System.out.println(player.getName() + "has not played before, teleporting to random location");
        }
        return true;
    }
}
