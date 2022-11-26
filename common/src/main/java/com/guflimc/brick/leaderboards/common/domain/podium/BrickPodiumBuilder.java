package com.guflimc.brick.leaderboards.common.domain.podium;

import com.guflimc.brick.leaderboards.api.domain.podium.Podium;
import com.guflimc.brick.leaderboards.api.domain.podium.PodiumBuilder;
import com.guflimc.brick.maths.api.geo.pos.Location;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public abstract class BrickPodiumBuilder<T extends BrickPodiumBuilder<T>> implements PodiumBuilder {

    protected Podium.Title title;
    protected Component name;
    protected Location[] positions;

    private T thiz() {
        return (T) this;
    }

    @Override
    public T withTitle(@NotNull Component title, @NotNull Location position) {
        this.title = new Podium.Title(title, position);
        return thiz();
    }

    @Override
    public PodiumBuilder withName(@NotNull Component name) {
        this.name = name;
        return thiz();
    }

    @Override
    public PodiumBuilder withPositions(@NotNull Location... positions) {
        this.positions = positions;
        return thiz();
    }
}
