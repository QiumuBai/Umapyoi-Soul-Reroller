package com.qiumubai.umapyoisoulreroller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static ModConfigSpec.IntValue REROLL_COST;
    private static void setupRerollConfig() {
        BUILDER.push("reroll_settings");
        REROLL_COST = BUILDER
                .comment("The amount of EXP levels required to reroll the factor seed")
                .defineInRange("rerollCost", 1, 0,);
        BUILDER.pop();
    }

    static {
        setupRerollConfig();
    }

    static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }
}
