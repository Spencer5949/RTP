package net.kursmc.randomspawn.randomspawn;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class RandomTPCommand implements CommandExecutor {

    public static HashMap<UUID, Long> cooldown = new HashMap<UUID, Long>();


    public static double PreTpXLoc;
    public static double PreTpYLoc;
    public static double PreTpZLoc;
    static RandomSpawn plugin;

    public RandomTPCommand(RandomSpawn plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("randomspawn.rtp")) {
                if (cooldown.get(player.getUniqueId()) == null || player.hasPermission("randomspawn.cooldown.bypass") || System.currentTimeMillis() / 1000 >= cooldown.get(player.getUniqueId())) {
                    double PreX = player.getLocation().getX();
                    PreTpXLoc = PreX;
                    double PreY = player.getLocation().getY();
                    PreTpYLoc = PreY;
                    double PreZ = player.getLocation().getZ();
                    PreTpZLoc = PreZ;
                    if (args.length == 0) {
                        Location randomLocation = TeleportUtils.findSafeLocation(player);
                        TeleportUtils.doTeleport(player, randomLocation);
                    } else if (args.length == 1) {
                        if (player.hasPermission("randomspawn.rtp.others")) {
                            Player target = Bukkit.getPlayer(args[0]);
                            Location randomLocation = TeleportUtils.findSafeLocation(target);
                            target.teleport(randomLocation);
                            target.sendMessage(ChatColor.GREEN + player.getDisplayName() + ChatColor.GOLD + " just Random Teleported you!");
                            target.sendMessage(ChatColor.AQUA + "New Coordinates: " + ChatColor.LIGHT_PURPLE + randomLocation.getX() + " " + randomLocation.getY() + " " + randomLocation.getZ());
                            player.sendMessage(ChatColor.RED + "Player successfully teleported to: " + ChatColor.LIGHT_PURPLE + randomLocation.getX() + " " + randomLocation.getY() + " " + randomLocation.getZ());
                        }
                    }
                } else {
                    int cd = Math.toIntExact(cooldown.get(player.getUniqueId()) - System.currentTimeMillis() / 1000);
                    int p1 = cd % 60;
                    int p2 = cd / 60;
                    int p3 = p2 % 60;
                    p2 = p2 / 60;
                    player.sendMessage(ChatColor.RED + "You are still on cooldown for " + p2 + " hours " + p3 + " minutes and " + p1 + " seconds!");
                }
            } else {
                player.sendMessage(ChatColor.RED + "You do not have permission to perform this action.");
            }
        } else {
            System.out.println("You need to be a player to execute this command.");
        }
        return true;
    }
}