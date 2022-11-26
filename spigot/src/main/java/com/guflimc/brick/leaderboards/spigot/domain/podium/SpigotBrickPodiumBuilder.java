package com.guflimc.brick.leaderboards.spigot.domain.podium;

import com.guflimc.brick.leaderboards.common.domain.podium.BrickPodiumBuilder;
import com.guflimc.brick.leaderboards.spigot.api.domain.podium.SpigotPodium;
import com.guflimc.brick.leaderboards.spigot.api.domain.podium.SpigotPodiumBuilder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Function;

public class SpigotBrickPodiumBuilder extends BrickPodiumBuilder<SpigotBrickPodiumBuilder> implements SpigotPodiumBuilder {

    private Function<UUID, ItemStack> supplier;

    @Override
    public SpigotPodiumBuilder withItemSupplier(@NotNull Function<UUID, ItemStack> supplier) {
        this.supplier = supplier;
        return this;
    }

    @Override
    public SpigotPodium build() {
        if ( positions == null || positions.length == 0 ) {
            throw new IllegalStateException("No positions defined");
        }
        if ( supplier == null ) {
            throw new IllegalStateException("No item supplier defined");
        }

        return new SpigotBrickPodium(positions, title, name, supplier);
    }
}
