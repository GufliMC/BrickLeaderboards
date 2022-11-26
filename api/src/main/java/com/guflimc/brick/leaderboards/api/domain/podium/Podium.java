package com.guflimc.brick.leaderboards.api.domain.podium;

import com.guflimc.brick.leaderboards.api.domain.Leaderboard;
import com.guflimc.brick.maths.api.geo.pos.Location;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface Podium extends Leaderboard {

    @NotNull Location[] positions();

    @Nullable Title title();

    record Title(Component content, Location position) {}

    @NotNull Component name();

    @NotNull List<Member> members();

    void update(@NotNull UUID entityId, int score);

    record Member(UUID entityId, Component displayName, int score) {}

}
