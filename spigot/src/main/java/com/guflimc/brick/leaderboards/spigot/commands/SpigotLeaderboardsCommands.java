package com.guflimc.brick.leaderboards.spigot.commands;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import com.guflimc.brick.i18n.spigot.api.SpigotI18nAPI;
import com.guflimc.brick.leaderboards.common.domain.DStatsLeaderboard;
import com.guflimc.brick.leaderboards.spigot.SpigotBrickLeaderboardsManager;
import com.guflimc.brick.maths.api.geo.pos.CardinalDirection;
import com.guflimc.brick.maths.api.geo.pos.Location;
import com.guflimc.brick.maths.spigot.api.SpigotMaths;
import com.guflimc.brick.stats.api.key.StatsKey;
import org.bukkit.entity.Player;

public class SpigotLeaderboardsCommands {

    private final SpigotBrickLeaderboardsManager manager;

    public SpigotLeaderboardsCommands(SpigotBrickLeaderboardsManager manager) {
        this.manager = manager;
    }

    @CommandMethod("podium create <name> <statskey> <actorType> <amount>")
    @CommandPermission("leaderboards.podium.create")
    public void create(Player sender,
                       @Argument("name") String name,
                       @Argument("statskey") String statsKey,
                       @Argument("actorType") String actorType,
                       @Argument("amount") int amount) {

        if ( manager.findStatsLeaderboard(name).isPresent() ) {
            SpigotI18nAPI.get(this).send(sender, "cmd.podium.create.error.already.exists", name);
            return;
        }

        Location[] positions = new Location[amount];
        CardinalDirection dir = CardinalDirection.fromYaw(sender.getLocation().getYaw());
        Location first = SpigotMaths.toBrickLocation(sender.getLocation());
        positions[0] = first;

        dir = dir.clockwise();
        for (int i = 1; i < amount; i++) {
            positions[i] = (Location) dir.forwards(first, (int) Math.ceil(i / 2.0d)).addY(-Math.ceil(i / 2.0d));
            dir = dir.opposite();
        }

        manager.createStatsPodium(name, StatsKey.of(statsKey), actorType, positions);
        SpigotI18nAPI.get(this).send(sender, "cmd.podium.create", name);
    }

    @CommandMethod("podium delete <name>")
    @CommandPermission("leaderboards.podium.create")
    public void delete(Player sender, @Argument("name") String name) {
        DStatsLeaderboard leaderboard = manager.findStatsLeaderboard(name).orElse(null);
        if ( leaderboard == null ) {
            SpigotI18nAPI.get(this).send(sender, "cmd.error.args.leaderboard", name);
            return;
        }

        manager.remove(leaderboard);
        SpigotI18nAPI.get(this).send(sender, "cmd.podium.delete", name);
    }

}
