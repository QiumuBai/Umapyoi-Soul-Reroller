package com.qiumubai.umapyoisoulreroller.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record RerollFactorPayload() implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("umapyoi_soul_reroller", "reroll_factor");

    public static final StreamCodec<RegistryFriendlyByteBuf, RerollFactorPayload> STREAM_CODEC =
            StreamCodec.unit(new RerollFactorPayload());
    public static final Type<RerollFactorPayload> TYPE = new Type<>(ID);
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}