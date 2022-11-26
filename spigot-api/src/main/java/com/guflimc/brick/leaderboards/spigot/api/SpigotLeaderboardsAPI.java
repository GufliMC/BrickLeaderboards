package com.guflimc.brick.leaderboards.spigot.api;

import com.guflimc.brick.leaderboards.api.LeaderboardsAPI;
import org.jetbrains.annotations.ApiStatus;

public class SpigotLeaderboardsAPI {

    private static SpigotLeaderboardsManager manager;

    @ApiStatus.Internal
    public static void setManager(SpigotLeaderboardsManager _manager) {
        manager = _manager;
        LeaderboardsAPI.setManager(_manager);
    }

    //

    public static SpigotLeaderboardsManager get() {
        return manager;
    }

}
