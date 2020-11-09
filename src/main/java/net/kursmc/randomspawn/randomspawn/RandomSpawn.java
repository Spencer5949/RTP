package net.kursmc.randomspawn.randomspawn;

import net.kursmc.randomspawn.randomspawn.listeners.NewPlayer;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class RandomSpawn extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        TeleportUtils teleportUtils = new TeleportUtils(this);
        RandomTPCommand randomTpCommand = new RandomTPCommand(this);

        getServer().getPluginManager().registerEvents(new NewPlayer(), this);

        getCommand("rtp").setExecutor(new RandomTPCommand(this));
        //Setup Config
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            LuckPerms api = provider.getProvider();



        }

    }
}
