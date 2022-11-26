package com.guflimc.brick.leaderboards.spigot;

import com.guflimc.brick.leaderboards.common.BrickLeaderboardsManager;
import com.guflimc.brick.leaderboards.spigot.api.SpigotLeaderboardsManager;
import com.guflimc.brick.leaderboards.spigot.domain.podium.SpigotBrickPodiumBuilder;
import com.guflimc.brick.orm.api.database.DatabaseContext;

public class SpigotBrickLeaderboardsManager extends BrickLeaderboardsManager implements SpigotLeaderboardsManager {

    public SpigotBrickLeaderboardsManager(DatabaseContext databaseContext) {
        super(databaseContext);
    }

    @Override
    public SpigotBrickPodiumBuilder podium() {
        return new SpigotBrickPodiumBuilder();
    }

}
