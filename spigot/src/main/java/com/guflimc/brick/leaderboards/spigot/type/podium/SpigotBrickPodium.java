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
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

public class SpigotBrickPodium extends BrickPodium implements SpigotPodium {

    private final JavaPlugin plugin;
    private final Function<UUID, ItemStack> supplier;

    private final Set<Entity> entities = new HashSet<>();

    public SpigotBrickPodium(@NotNull Location[] positions, Title title, Component name,
                             @NotNull JavaPlugin plugin, @NotNull Function<UUID, ItemStack> supplier) {
        super(positions, title, name);
        this.plugin = plugin;
        this.supplier = supplier;
    }

    @Override
    public void remove() {
        entities.forEach(Entity::remove);
        entities.clear();
    }

    @Override
    protected void render() {
        ItemStack[] items = members().stream()
                .map(member -> supplier.apply(member.entityId()))
                .toArray(ItemStack[]::new);

        if (Arrays.stream(items).allMatch(is -> is.getType() == Material.PLAYER_HEAD)) {
            renderArmorStands(items);
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

    private final static EulerAngle[][] ARM_ANGLES = new EulerAngle[][] {
            new EulerAngle[] {
                    new EulerAngle(Math.toRadians(200), 0, Math.toRadians(38)),
                    new EulerAngle(Math.toRadians(330), Math.toRadians(25), 0),
                    new EulerAngle(Math.toRadians(340), 0, Math.toRadians(345)),
            },
            new EulerAngle[] {
                    new EulerAngle(Math.toRadians(250), 0, Math.toRadians(110)),
                    new EulerAngle(Math.toRadians(335), Math.toRadians(25), 0),
                    new EulerAngle(0, 0, 0),
                    new EulerAngle(Math.toRadians(330), Math.toRadians(10), Math.toRadians(15))
            },
            new EulerAngle[] {
                    new EulerAngle(0, 0, 0),
                    new EulerAngle(Math.toRadians(250), Math.toRadians(290), Math.toRadians(100)),
            }
    };

    private void renderArmorStands(ItemStack[] heads) {
        entities.forEach(Entity::remove);
        entities.clear();

        for (int i = 0; i < positions().length; i++) {
            Location position = positions()[i];
            int index = i;

            org.bukkit.Location bloc = SpigotMaths.toSpigotLocation(position);
            ArmorStand statue = bloc.getWorld().spawn(bloc.clone().add(0, -0.1, 0), ArmorStand.class, false, a -> {
                a.setGravity(false);
                a.setArms(true);
                a.setBasePlate(false);
                a.setSmall(true);
                a.setVisible(true);
                a.setCustomNameVisible(false);
                a.setMetadata("LEADERBOARD", new FixedMetadataValue(plugin, true));

                EntityEquipment eq = a.getEquipment();
                Objects.requireNonNull(eq);

                ItemStack item = ARMOR_ITEMS[index == 0 ? 0 : ARMOR_ITEMS.length % index];
                if (item.getType().name().contains("CHESTPLATE")) {
                    eq.setChestplate(item);
                } else {
                    eq.setLeggings(item);
                }

                eq.setItemInMainHand(HAND_ITEMS[index == 0 ? 0 : HAND_ITEMS.length % index]);

                int m = index == 0 ? 0 : ARM_ANGLES.length % index;
                if ( ARM_ANGLES[m].length >= 1 ) a.setLeftArmPose(ARM_ANGLES[m][0]);
                if ( ARM_ANGLES[m].length >= 2 ) a.setRightArmPose(ARM_ANGLES[m][1]);
                if ( ARM_ANGLES[m].length >= 3 ) a.setLeftLegPose(ARM_ANGLES[m][2]);
                if ( ARM_ANGLES[m].length >= 4 ) a.setRightLegPose(ARM_ANGLES[m][3]);

                if (index >= heads.length) {
                    eq.setHelmet(QUESTION_MARK);
                }
            });
            entities.add(statue);

            // failsave
            statue.getNearbyEntities(0.1, 0.4, 0.1).stream()
                    .filter(e -> e instanceof ArmorStand)
                    .forEach(Entity::remove);

            ArmorStand holo = bloc.getWorld().spawn(bloc.clone().add(0, 0.25, 0), ArmorStand.class, false, a -> {
                a.setVisible(false);
                a.setSmall(true);
                a.setGravity(false);
                a.setCustomNameVisible(false);
                a.setMetadata("LEADERBOARD", new FixedMetadataValue(plugin, true));
            });
            entities.add(holo);

            if (heads.length > i) {
                Member member = members().get(i);
                statue.getEquipment().setHelmet(heads[i]);

                Component sub = display().replaceText(b -> b.match("(\\{[" + Pattern.quote("0") + "]})")
                        .replacement(member.score() + ""));
                statue.setCustomName(LegacyComponentSerializer.legacySection().serialize(sub));
                statue.setCustomNameVisible(true);

                holo.setCustomName(LegacyComponentSerializer.legacySection().serialize(member.displayName()));
                holo.setCustomNameVisible(true);
            }
        }
    }

}
