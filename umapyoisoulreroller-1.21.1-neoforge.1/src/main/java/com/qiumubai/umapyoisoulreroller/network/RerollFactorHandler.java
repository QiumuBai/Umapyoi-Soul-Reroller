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
            ServerPlayer player = context.player();
            if (player == null) return;

            // 1. Verify the player is currently using the Retire Menu
            if (!(player.containerMenu instanceof RetireRegisterMenu menu)) {
                return;
            }

            // 2. Get the EXP cost from config
            int cost = Config.REROLL_COST.getAsInt();

            // 3. SECURITY CHECK: Verify player has enough EXP (or is in Creative mode)
            // This prevents cheaters from sending packets with disabled buttons
            if (player.experienceLevel < cost && !player.isCreative()) {
                // Silently reject - button should be disabled client-side
                // If this happens, it's likely a hacking attempt
                return;
            }

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
        });
    }
}