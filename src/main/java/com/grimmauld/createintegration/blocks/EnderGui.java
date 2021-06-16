package com.grimmauld.createintegration.blocks;

import com.grimmauld.createintegration.CreateIntegration;
import com.grimmauld.createintegration.tools.Lang;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.ParametersAreNonnullByDefault;

public class EnderGui extends ContainerScreen<EnderContainer> {
    private final ResourceLocation GUI = CreateIntegration.generateResourceLocation("textures/gui/ender_crate.png");

    public EnderGui(EnderContainer screenContainer, PlayerInventory inv, ITextComponent name) {
        super(screenContainer, inv, name);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        // TODO no idea what the replacement is called
        // this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawBackground(MatrixStack matrixStack, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        // GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        /*assert this.minecraft != null;
        this.minecraft.getTextureManager().bindTexture(GUI);
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.blit(matrixStack, relX, relY, 0, 0, this.xSize, this.ySize);*/
    }

    @Override
    protected void drawForeground(MatrixStack matrixStack, int p_230451_2_, int p_230451_3_) {
        super.drawForeground(matrixStack, p_230451_2_, p_230451_3_);
    }
}
