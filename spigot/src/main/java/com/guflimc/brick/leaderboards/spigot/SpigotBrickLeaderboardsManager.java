package com.guflimc.brick.leaderboards.spigot;

import com.guflimc.brick.gui.spigot.item.ItemStackBuilder;
import com.guflimc.brick.leaderboards.api.type.podium.Podium;
import com.guflimc.brick.leaderboards.api.type.podium.PodiumBuilder;
import com.guflimc.brick.leaderboards.common.BrickLeaderboardsManager;
import com.guflimc.brick.leaderboards.common.domain.DStatsPodium;
import com.guflimc.brick.leaderboards.spigot.api.SpigotLeaderboardsManager;
import com.guflimc.brick.leaderboards.spigot.api.type.podium.SpigotPodiumBuilder;
import com.guflimc.brick.leaderboards.spigot.type.podium.SpigotBrickPodiumBuilder;
import com.guflimc.brick.orm.api.database.DatabaseContext;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SpigotBrickLeaderboardsManager extends BrickLeaderboardsManager implements SpigotLeaderboardsManager {

    private final JavaPlugin plugin;

    public SpigotBrickLeaderboardsManager(DatabaseContext databaseContext, @NotNull JavaPlugin plugin) {
        super(databaseContext);
        this.plugin = plugin;

        // podiums
        leaderboards().stream()
                .filter(sl -> sl instanceof DStatsPodium)
                .map(sl -> (DStatsPodium) sl)
                .forEach(this::spawn);
    }

    @Override
    protected Podium.Member member(@NotNull UUID entityId, int score) {
        String name = Bukkit.getOfflinePlayer(entityId).getName();
        Component displayName = name != null ? Component.text(name) : Component.text("Unknown");
        return new Podium.Member(entityId, displayName, score);
    }

    @Override
    protected void inject(PodiumBuilder podiumBuilder) {
        SpigotPodiumBuilder pb = (SpigotPodiumBuilder) podiumBuilder;
        pb.withItemSupplier(id -> ItemStackBuilder.skull().withPlayer(id).build());
    }

    @Override
    public SpigotBrickPodiumBuilder podium() {
        return new SpigotBrickPodiumBuilder(plugin);
    }


}
