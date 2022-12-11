package com.guflimc.brick.leaderboards.api.type.podium;

import com.guflimc.brick.maths.api.geo.pos.Location;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface Podium {

    @NotNull Location[] positions();

    @Nullable Title title();

    record Title(Component content, Location position) {}

    @NotNull Component display();

    void render();

    @NotNull List<Member> members();

    void update(@NotNull Member member);

    record Member(@NotNull UUID entityId, @NotNull Component displayName, int score) {}

}
