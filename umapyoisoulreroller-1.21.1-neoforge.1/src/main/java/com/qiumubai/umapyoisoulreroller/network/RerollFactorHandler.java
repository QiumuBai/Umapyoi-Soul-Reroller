package com.qiumubai.umapyoisoulreroller.network;

import com.qiumubai.umapyoisoulreroller.Config;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.tracen.umapyoi.container.RetireRegisterMenu;



public class RerollFactorHandler {

    public static void handle(RerollFactorPayload payload, IPayloadContext context) {
        // enqueueWork ensures this runs safely on the main server thread
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                if (player.containerMenu instanceof RetireRegisterMenu menu) {
                    int cost = Config.REROLL_COST.getAsInt();
                    if (player.experienceLevel < cost && !player.isCreative()) {
                        return;
                    }
                    if (!player.isCreative()) {
                        player.giveExperienceLevels(-cost);
                    }
                    int newSeed = player.getRandom().nextInt();
                    menu.getFactorSeed().set(newSeed);

                    menu.createResult();
                }
            }
        });
    }
}