package com.guflimc.brick.leaderboards.spigot.commands;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import com.guflimc.brick.leaderboards.spigot.SpigotBrickLeaderboardsManager;
import com.guflimc.brick.maths.api.geo.pos.Location;
import com.guflimc.brick.maths.spigot.api.SpigotMaths;
import com.guflimc.brick.stats.api.key.StatsKey;
import org.bukkit.entity.Player;

import java.util.function.BiFunction;

public class SpigotLeaderboardsCommands {

    private final SpigotBrickLeaderboardsManager manager;

    public SpigotLeaderboardsCommands(SpigotBrickLeaderboardsManager manager) {
        this.manager = manager;
    }

    @CommandMethod("podium create <name> <statskey> <amount>")
    @CommandPermission("leaderboards.podium.create")
    public void create(Player sender,
                       @Argument("name") String name,
                       @Argument("statskey") String statsKey,
                       @Argument("amount") int amount) {

        Location[] positions = new Location[amount];
        CardinalDirection dir = direction(sender.getLocation().getYaw());
        Location first = SpigotMaths.toBrickLocation(sender.getLocation());
        positions[0] = first;

        dir = dir.clockwise();
        for ( int i = 1; i < amount; i++ ) {
            positions[i] = dir.forwards(first, (int) Math.ceil(i / 2.0d)).addY(-Math.ceil(i / 2.0d));
            dir = dir.opposite();
        }

        manager.createStatsPodium(name, StatsKey.of(statsKey), positions);
    }

    //

    public enum CardinalDirection {
        NORTH((x, n) -> x, (z, n) -> z - n),
        EAST((x, n) -> x + n, (z, n) -> z),
        SOUTH((x, n) -> x, (z, n) -> z + n),
        WEST((x, n) -> x - n, (z, n) -> z);

        private final BiFunction<Double, Integer, Double> x;
        private final BiFunction<Double, Integer, Double> z;

        CardinalDirection(BiFunction<Double, Integer, Double> x, BiFunction<Double, Integer, Double> z) {
            this.x = x;
            this.z = z;
        }

        CardinalDirection opposite() {
            CardinalDirection[] values = values();
            int ordinal = (this.ordinal() + 2) % values.length;
            return values[ordinal];
        }

        CardinalDirection clockwise() {
            CardinalDirection[] values = values();
            int ordinal = (this.ordinal() + 1) % values.length;
            return values[ordinal];
        }

        CardinalDirection counterclockwise() {
            CardinalDirection[] values = values();
            int ordinal = (this.ordinal() + 3) % values.length;
            return values[ordinal];
        }

        public Location forwards(Location location, int blocks) {
            return location
                    .withX(x.apply(location.x(), blocks))
                    .withZ(z.apply(location.z(), blocks));
        }

        public Location backwards(Location location, int blocks) {
            return location
                    .withX(x.apply(location.x(), -blocks))
                    .withZ(z.apply(location.z(), -blocks));
        }
    }

    public CardinalDirection direction(float yaw) {
        if (yaw >= 135 || yaw < -135) {
            return CardinalDirection.NORTH;
        }
        if (yaw >= -135 && yaw < -45) {
            return CardinalDirection.EAST;
        }
        if (yaw >= -45 && yaw < 45) {
            return CardinalDirection.SOUTH;
        }
        return CardinalDirection.WEST;
    }
}
