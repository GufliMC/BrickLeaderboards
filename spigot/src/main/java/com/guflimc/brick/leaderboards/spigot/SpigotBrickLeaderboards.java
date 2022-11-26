package com.guflimc.brick.leaderboards.spigot;

import com.google.gson.Gson;
import com.guflimc.brick.leaderboards.api.LeaderboardsAPI;
import com.guflimc.brick.leaderboards.common.BrickLeaderboardsConfig;
import com.guflimc.brick.leaderboards.common.BrickLeaderboardsDatabaseContext;
import com.guflimc.brick.leaderboards.common.BrickLeaderboardsManager;
import com.guflimc.brick.scheduler.spigot.api.SpigotScheduler;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

public class SpigotBrickLeaderboards extends JavaPlugin {

    public static final Gson gson = new Gson();

    private BrickLeaderboardsManager manager;

    @Override
    public void onEnable() {

        // load config
        saveResource("config.json", false);
        BrickLeaderboardsConfig config;
        try (
                InputStream is = new FileInputStream(new File(getDataFolder(), "config.json"));
                InputStreamReader isr = new InputStreamReader(is)
        ) {
            config = gson.fromJson(isr, BrickLeaderboardsConfig.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // initialize database
        BrickLeaderboardsDatabaseContext databaseContext = new BrickLeaderboardsDatabaseContext(config.database);

        // create scheduler
        SpigotScheduler scheduler = new SpigotScheduler(this, getName());

        // create manager
        manager = new BrickLeaderboardsManager(databaseContext);
        LeaderboardsAPI.setManager(manager);

        // register events
        PluginManager pm = getServer().getPluginManager();

        getLogger().info("Enabled " + nameAndVersion() + ".");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled " + nameAndVersion() + ".");
    }

    private String nameAndVersion() {
        return getName() + " v" + getDescription().getVersion();
    }

}
