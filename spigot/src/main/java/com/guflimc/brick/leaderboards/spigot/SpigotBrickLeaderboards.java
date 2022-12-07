package com.guflimc.brick.leaderboards.spigot;

import cloud.commandframework.annotations.AnnotationParser;
import cloud.commandframework.bukkit.BukkitCommandManager;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.meta.SimpleCommandMeta;
import com.google.gson.Gson;
import com.guflimc.brick.i18n.spigot.api.SpigotI18nAPI;
import com.guflimc.brick.i18n.spigot.api.namespace.SpigotNamespace;
import com.guflimc.brick.leaderboards.api.LeaderboardsAPI;
import com.guflimc.brick.leaderboards.common.BrickLeaderboardsConfig;
import com.guflimc.brick.leaderboards.common.BrickLeaderboardsDatabaseContext;
import com.guflimc.brick.leaderboards.spigot.commands.SpigotLeaderboardsCommands;
import com.guflimc.brick.leaderboards.spigot.listeners.EntityListener;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.Locale;
import java.util.function.Function;

public class SpigotBrickLeaderboards extends JavaPlugin {

    public static final Gson gson = new Gson();

    private BrickLeaderboardsDatabaseContext databaseContext;
    private SpigotBrickLeaderboardsManager manager;

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

        // TRANSLATIONS
        SpigotNamespace namespace = new SpigotNamespace(this, Locale.ENGLISH);
        namespace.loadValues(this, "languages");
        SpigotI18nAPI.get().register(namespace);

        // initialize database
        databaseContext = new BrickLeaderboardsDatabaseContext(config.database);

        // create manager
        manager = new SpigotBrickLeaderboardsManager(databaseContext, this);
        LeaderboardsAPI.setManager(manager);

        // register events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new EntityListener(), this);

        // commands
        setupCommands();

        getLogger().info("Enabled " + nameAndVersion() + ".");
    }

    @Override
    public void onDisable() {
        if ( databaseContext != null ) {
            databaseContext.shutdown();
        }

        getLogger().info("Disabled " + nameAndVersion() + ".");
    }

    private String nameAndVersion() {
        return getName() + " v" + getDescription().getVersion();
    }

    private void setupCommands() {
        // COMMANDS
        try {
            BukkitCommandManager<CommandSender> commandManager = new BukkitCommandManager<>(
                    this,
                    CommandExecutionCoordinator.simpleCoordinator(),
                    Function.identity(),
                    Function.identity()
            );

//            commandManager.parserRegistry().registerParserSupplier(TypeToken.get(Clan.class),
//                    ps -> new ClanArgument.ClanParser<>());

            AnnotationParser<CommandSender> annotationParser = new AnnotationParser<>(
                    commandManager,
                    CommandSender.class,
                    parameters -> SimpleCommandMeta.empty()
            );

//            annotationParser.getParameterInjectorRegistry().registerInjector(Profile.class,
//                    (context, annotationAccessor) -> clanManager.findCachedProfile(((Player) context.getSender()).getUniqueId()).orElseThrow());

            annotationParser.parse(new SpigotLeaderboardsCommands(manager));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
