package com.guflimc.brick.leaderboards.spigot.type.podium;

import com.guflimc.brick.gui.spigot.item.ItemStackBuilder;
import com.guflimc.brick.gui.spigot.item.specific.LeatherArmorBuilder;
import com.guflimc.brick.leaderboards.common.type.podium.BrickPodium;
import com.guflimc.brick.leaderboards.spigot.api.type.podium.SpigotPodium;
import com.guflimc.brick.maths.api.geo.pos.Location;
import com.guflimc.brick.maths.spigot.api.SpigotMaths;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Pattern;

public class SpigotBrickPodium extends BrickPodium implements SpigotPodium {

    private final Function<UUID, ItemStack> supplier;

    public SpigotBrickPodium(@NotNull Location[] positions, Title title, Component name, @NotNull Function<UUID, ItemStack> supplier) {
        super(positions, title, name);
        this.supplier = supplier;
    }

    @Override
    protected void render() {
        ItemStack[] items = members().stream()
                .map(member -> supplier.apply(member.entityId()))
                .toArray(ItemStack[]::new);

        if (Arrays.stream(items).allMatch(is -> is.getType() == Material.PLAYER_HEAD)) {
            renderArmorStands(items);
            return;
        }

    }

    private final static ItemStack QUESTION_MARK = ItemStackBuilder.skull()
            .withTexture("d34e063cafb467a5c8de43ec78619399f369f4a52434da8017a983cdd92516a0")
            .build();

    private final static ItemStack[] HAND_ITEMS = new ItemStack[]{
            ItemStackBuilder.of(Material.DIAMOND_SWORD).withEnchantment(Enchantment.DAMAGE_ALL).build(),
            ItemStackBuilder.of(Material.DIAMOND_AXE).build(),
            ItemStackBuilder.of(Material.BOW).withEnchantment(Enchantment.ARROW_DAMAGE).build(),
            ItemStackBuilder.of(Material.IRON_PICKAXE).build(),
            ItemStackBuilder.of(Material.STONE_SWORD).build(),
            ItemStackBuilder.of(Material.WOODEN_AXE).build(),
    };

    private final static ItemStack[] ARMOR_ITEMS = new ItemStack[]{
            ItemStackBuilder.leatherArmor(LeatherArmorBuilder.ArmorType.CHESTPLATE).withArmorColor(Color.YELLOW).build(),
            ItemStackBuilder.leatherArmor(LeatherArmorBuilder.ArmorType.LEGGINGS).withArmorColor(Color.SILVER).build(),
            ItemStackBuilder.leatherArmor(LeatherArmorBuilder.ArmorType.CHESTPLATE).withArmorColor(Color.AQUA).build(),
            ItemStackBuilder.leatherArmor(LeatherArmorBuilder.ArmorType.LEGGINGS).withArmorColor(Color.PURPLE).build(),
            ItemStackBuilder.leatherArmor(LeatherArmorBuilder.ArmorType.CHESTPLATE).withArmorColor(Color.BLUE).build()
    };

    private void renderArmorStands(ItemStack[] heads) {
        for (int i = 0; i < positions().length; i++) {
            Location position = positions()[i];

            org.bukkit.Location bloc = SpigotMaths.toSpigotLocation(position);
            ArmorStand as = (ArmorStand) bloc.getWorld().spawnEntity(bloc, EntityType.ARMOR_STAND);
            ArmorStand holo = (ArmorStand) bloc.getWorld().spawnEntity(bloc.clone().add(0, 0.25, 0), EntityType.ARMOR_STAND);

            as.getNearbyEntities(0.1, 0.3, 0.1).stream()
                    .filter(e -> e instanceof ArmorStand)
                    .forEach(Entity::remove);

            as.setSmall(true);
            as.setGravity(false);
            as.setArms(true);
            as.setBasePlate(false);
            as.setVisible(true);

            holo.setSmall(true);
            holo.setGravity(false);
            holo.setVisible(false);

            if (heads.length > i) {
                Member member = members().get(i);
                as.getEquipment().setHelmet(heads[i]);

                Component sub = display().replaceText(b -> b.match("(\\{[" + Pattern.quote("0") + "]})")
                        .replacement(member.score() + ""));
                holo.setCustomName(LegacyComponentSerializer.legacySection().serialize(sub));
                as.setCustomNameVisible(true);

                holo.setCustomName(LegacyComponentSerializer.legacySection().serialize(member.displayName()));
                holo.setCustomNameVisible(true);
            } else {
                as.getEquipment().setHelmet(QUESTION_MARK);
            }

            if (ARMOR_ITEMS.length > i) {
                ItemStack item = ARMOR_ITEMS[i];
                if (item.getType().name().contains("CHESTPLATE")) {
                    as.getEquipment().setChestplate(item);
                } else {
                    as.getEquipment().setLeggings(item);
                }
            }

            if (HAND_ITEMS.length > i) {
                as.getEquipment().setItemInMainHand(HAND_ITEMS[i]);
            }
        }
    }

}
