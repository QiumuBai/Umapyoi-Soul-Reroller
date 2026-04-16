package com.qiumubai.umapyoisoulreroller.client;

import com.qiumubai.umapyoisoulreroller.Config;
import com.qiumubai.umapyoisoulreroller.UmapyoiSoulReroller;
import com.qiumubai.umapyoisoulreroller.network.RerollFactorPayload;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.tracen.umapyoi.client.screen.RetireRegisterScreen;

@EventBusSubscriber(modid = UmapyoiSoulReroller.MODID, value = Dist.CLIENT)
public class ClientModEvents {

    private static final int UI_GREEN = 0xFF7FE817;
    private static final int UI_GREEN_DARK = 0xFF5CB85C;
    private static final int UI_GREEN_LIGHT = 0xFFA4FF4D;
    private static final int TEXT_WHITE = 0xFFFFFFFF;

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        // Check if the screen being opened is the Umapyoi Retire Screen
        if (event.getScreen() instanceof RetireRegisterScreen screen) {
            int screenLeftX = screen.getGuiLeft();
            int screenTopY = screen.getGuiTop();
            int buttonWidth = 80;
            int buttonHeight = 13;
            int buttonX = screenLeftX + 48;
            int buttonY = screenTopY + 43;

            int cost = Config.REROLL_COST.getAsInt();

            // Add the button to the screen
            event.addListener(new StyledRerollButton(
                    buttonX,
                    buttonY,
                    buttonWidth,
                    buttonHeight,
                    Component.translatable("message.reroll.exprequirement",cost),
                    (btn) -> PacketDistributor.sendToServer(new RerollFactorPayload())
            ));
        }
    }

    public static class StyledRerollButton extends Button {

        public StyledRerollButton(int x, int y, int width, int height, Component message, OnPress onPress) {
            super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        }

        private boolean hasEnoughExp() {
            var player = Minecraft.getInstance().player;
            if (player == null) return false;

            // Same logic as server - check EXP level or creative mode
            return player.experienceLevel >= rerollCost || player.isCreative();
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            // Determine button color based on hover state
            boolean hasExp = hasEnoughExp();
            int backgroundColor;
            int textColor;

            if (!hasExp) {
                // NOT ENOUGH EXP - Gray disabled color
                backgroundColor = UI_GRAY_DISABLED;
                textColor = TEXT_GRAY;
                this.active = false;  // Disable button click
            } else if (this.isHoveredOrFocused()) {
                // HOVER - Lighter green
                backgroundColor = UI_GREEN_LIGHT;
                textColor = TEXT_WHITE;
                this.active = true;
            } else {
                // NORMAL - Bright green
                backgroundColor = UI_GREEN;
                textColor = TEXT_WHITE;
                this.active = true;
            }

            // Fill button background
            guiGraphics.fill(this.getX(), this.getY(),
                    this.getX() + this.getWidth(), this.getY() + this.getHeight(),
                    backgroundColor);

            // Draw darker green border for definition
            guiGraphics.fill(this.getX(), this.getY(),
                    this.getX() + this.getWidth(), this.getY() + 1,
                    UI_GREEN_DARK); // Top border

            guiGraphics.fill(this.getX(), this.getY() + this.getHeight() - 1,
                    this.getX() + this.getWidth(), this.getY() + this.getHeight(),
                    UI_GREEN_DARK); // Bottom border

            guiGraphics.fill(this.getX(), this.getY(),
                    this.getX() + 1, this.getY() + this.getHeight(),
                    UI_GREEN_DARK); // Left border

            guiGraphics.fill(this.getX() + this.getWidth() - 1, this.getY(),
                    this.getX() + this.getWidth(), this.getY() + this.getHeight(),
                    UI_GREEN_DARK); // Right border

            // Draw white text centered on button
            // The font is Minecraft's default font (same as the UI)
            guiGraphics.drawCenteredString(
                    net.minecraft.client.Minecraft.getInstance().font,
                    this.getMessage(),
                    this.getX() + this.getWidth() / 2,
                    this.getY() + (this.getHeight() - 8) / 2,
                    TEXT_WHITE
            );
        }
    }
}

