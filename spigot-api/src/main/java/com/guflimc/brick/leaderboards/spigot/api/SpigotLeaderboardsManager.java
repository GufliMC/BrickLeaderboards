package com.guflimc.brick.leaderboards.spigot.api;

import com.guflimc.brick.leaderboards.api.LeaderboardsManager;
import com.guflimc.brick.leaderboards.spigot.api.type.podium.SpigotPodiumBuilder;

public interface SpigotLeaderboardsManager extends LeaderboardsManager {

    @Override
    SpigotPodiumBuilder podium();
}
