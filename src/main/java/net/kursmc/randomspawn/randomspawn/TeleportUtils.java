package net.kursmc.randomspawn.randomspawn;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class TeleportUtils {

    //Get an instance of the main class so we can access config
    static RandomSpawn plugin;

    public TeleportUtils(RandomSpawn plugin) {
        this.plugin = plugin;
    }

    //List of all the unsafe blocks
    public static HashSet<Material> bad_blocks = new HashSet<>();

    static {
        bad_blocks.add(Material.LAVA);
        bad_blocks.add(Material.FIRE);
        bad_blocks.add(Material.CACTUS);
        bad_blocks.add(Material.WATER);
    }

    public static Location generateLocation(Player player) {
        //Generate Random Location

        Random random = new Random();


        int x = 0;
        int z = 0;
        int y = 0;

        if (plugin.getConfig().getBoolean("world-border")) { //If they want to limit the distance
            x = random.nextInt(plugin.getConfig().getInt("MaxX") - plugin.getConfig().getInt("MinX")) + plugin.getConfig().getInt("MinX");
            z = random.nextInt(plugin.getConfig().getInt("MaxY") - plugin.getConfig().getInt("MinY")) + plugin.getConfig().getInt("MinY");
            y = 150;
        } else if (!plugin.getConfig().getBoolean("world-border")) { //If they dont
            x = random.nextInt(1000000 - -1000000) + -1000000;
            z = random.nextInt(1000000 - -1000000) + -1000000;
            y = 150;
        }

        Location randomLocation = new Location(player.getWorld(), x + 0.5, y, z + 0.5);
        y = randomLocation.getWorld().getHighestBlockYAt(randomLocation) + 1;
        randomLocation.setY(y);

        return randomLocation;
    }

    public static Location findSafeLocation(Player player) {

        Location randomLocation = generateLocation(player);

        while (!isLocationSafe(randomLocation)) {
            //Keep looking for a safe location
            randomLocation = generateLocation(player);
        }

        return randomLocation;
    }

    public static boolean isLocationSafe(Location location) {

        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        //Get instances of the blocks around where the player would spawn
        Block block = location.getWorld().getBlockAt(x, y, z);
        Block below = location.getWorld().getBlockAt(x, y - 1, z);
        Block above = location.getWorld().getBlockAt(x, y + 1, z);

        //Check to see if the surroundings are safe or not

        return !(bad_blocks.contains(below.getType())) || (block.getType().isSolid()) || (above.getType().isSolid() || (below.getType().isAir()));

    }

    public static void doTeleport(Player player, Location randomLocation) {

        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        AtomicInteger count = new AtomicInteger(5);
        AtomicInteger id = new AtomicInteger();
        id.getAndSet(scheduler.scheduleSyncRepeatingTask(plugin, () -> {
            if (count.get() > 0) {
                player.sendTitle(ChatColor.GOLD + "Teleporting in", ChatColor.GRAY + "" + count.get(), 0, 21, 0);
                count.getAndDecrement();
                if (player.getLocation().getX() != (RandomTPCommand.PreTpXLoc) || player.getLocation().getY() != (RandomTPCommand.PreTpYLoc) || player.getLocation().getZ() != RandomTPCommand.PreTpZLoc) {
                    player.sendTitle(ChatColor.RED + "Teleport Cancelled", "", 5, 20, 5);
                    scheduler.cancelTask(id.get());
                }

            } else {
                player.teleport(randomLocation);
                player.sendMessage(ChatColor.GOLD + "Teleported to: " + ChatColor.WHITE + ChatColor.BOLD + randomLocation.getX() + " " + randomLocation.getY() + " " + randomLocation.getZ());
                scheduler.cancelTask(id.get());
                if (player.hasPermission("group.default")) {
                    RandomTPCommand.cooldown.put(player.getUniqueId(), System.currentTimeMillis() / 1000 + plugin.getConfig().getInt("default"));
                } else if (player.hasPermission("group.vip")) {
                    RandomTPCommand.cooldown.put(player.getUniqueId(), System.currentTimeMillis() / 1000 + plugin.getConfig().getInt("vip"));
                } else if (player.hasPermission("group.premium")) {
                    RandomTPCommand.cooldown.put(player.getUniqueId(), System.currentTimeMillis() / 1000 + plugin.getConfig().getInt("premium"));
                } else if (player.hasPermission("group.elite")) {
                    RandomTPCommand.cooldown.put(player.getUniqueId(), System.currentTimeMillis() / 1000 + plugin.getConfig().getInt("elite"));
                } else if (player.hasPermission("group.supreme")) {
                    RandomTPCommand.cooldown.put(player.getUniqueId(), System.currentTimeMillis() / 1000 + plugin.getConfig().getInt("supreme"));
                } else if (player.hasPermission("group.kurs")) {
                    RandomTPCommand.cooldown.put(player.getUniqueId(), System.currentTimeMillis() / 1000 + plugin.getConfig().getInt("kurs"));
                } else {
                    RandomTPCommand.cooldown.put(player.getUniqueId(), System.currentTimeMillis() / 1000 + plugin.getConfig().getInt("default"));
                }
            }
        }, 0L, 20L));

    }

}