package com.kitnjinx.modogs.screens;

import com.kitnjinx.modogs.MoDogs;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GenoPrinterScreen extends ItemCombinerScreen<GenoPrinterMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(MoDogs.MOD_ID, "textures/gui/geno_printer_screen.png");

    public GenoPrinterScreen(GenoPrinterMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, TEXTURE);
        this.titleLabelX = 60;
        this.titleLabelY = 20;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pX, int pY) {
        super.renderBg(pPoseStack, pPartialTick, pX, pY);
        blit(pPoseStack, this.leftPos + 59, this.topPos + 20, 0,
                this.imageHeight + (this.menu.getSlot(0).hasItem() ? 0 : 16),
                110, 16);
    }

    @Override
    protected void renderErrorIcon(PoseStack pPoseStack, int pX, int pY) {
        if ((this.menu.getSlot(0).hasItem() && this.menu.getSlot(1).hasItem()) &&
                !this.menu.getSlot(this.menu.getResultSlot()).hasItem()) {
            blit(pPoseStack, pX + 99, pY + 45, this.imageWidth, 0, 28, 21);
        }
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTick);
        renderTooltip(poseStack, mouseX, mouseY);
    }
}
