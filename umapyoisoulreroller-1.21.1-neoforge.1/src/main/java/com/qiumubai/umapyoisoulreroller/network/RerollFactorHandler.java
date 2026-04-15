package com.qiumubai.umapyoisoulreroller.network;

import com.qiumubai.umapyoisoulreroller.Config;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.tracen.umapyoi.container.RetireRegisterMenu;

public class RerollFactorHandler {

    public static void handle(RerollFactorPayload payload, IPayloadContext context) {
        // enqueueWork ensures this runs safely on the main server thread
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {

                // 1. Verify the player is currently using the Retire Menu
                if (player.containerMenu instanceof RetireRegisterMenu menu) {

                    // 2. Define the EXP cost
                    int cost = Config.REROLL_COST.getAsInt();

                    if (player.experienceLevel < cost && !player.isCreative()) {
                        player.displayClientMessage(
                                Component.literal("❌ Not enough EXP! Need: " + cost + ", Have: " + player.experienceLevel),
                                true
                        );
                        return;
                    }

                    // 3. Check if the player has enough EXP (or is in Creative mode)
                    if (player.experienceLevel >= cost || player.isCreative()) {

                        // 4. Deduct the EXP (if not in Creative)
                        if (!player.isCreative()) {
                            player.giveExperienceLevels(-cost);
                        }

                        // 5. Generate a new seed and apply it to the menu
                        int newSeed = player.getRandom().nextInt();
                        menu.getFactorSeed().set(newSeed);

                        // 6. Force the menu to recalculate the result slot!
                        // This updates the item the player sees.
                        menu.createResult();
                    }
                }
            }
        });
    }
}