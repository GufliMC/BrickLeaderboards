package com.guflimc.brick.leaderboards.api.domain.podium;

import com.guflimc.brick.maths.api.geo.pos.Location;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public interface PodiumBuilder {

    PodiumBuilder withTitle(@NotNull Component title, @NotNull Location position);

    PodiumBuilder withName(@NotNull Component name);

    PodiumBuilder withPositions(@NotNull Location... positions);

    Podium build();

}
