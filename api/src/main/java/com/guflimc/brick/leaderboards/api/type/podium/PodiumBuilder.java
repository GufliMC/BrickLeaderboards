package com.guflimc.brick.leaderboards.api.type.podium;

import com.guflimc.brick.maths.api.geo.pos.Location;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public interface PodiumBuilder {

    PodiumBuilder withTitle(@NotNull Component title, @NotNull Location position);

    PodiumBuilder withDisplay(@NotNull Component display);

    PodiumBuilder withPositions(@NotNull Location... positions);

    Podium build();

}
