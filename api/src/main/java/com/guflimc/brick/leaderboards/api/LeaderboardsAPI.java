package com.guflimc.brick.leaderboards.api;

import org.jetbrains.annotations.ApiStatus;

public class LeaderboardsAPI {

    private static LeaderboardsManager manager;

    @ApiStatus.Internal
    public static void setManager(LeaderboardsManager _manager) {
        manager = _manager;
    }

    //

    public static LeaderboardsManager get() {
        return manager;
    }

}
