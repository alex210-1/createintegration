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
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
        drawCenteredString(matrixStack, Minecraft.getInstance().fontRenderer, Lang.translate("generic.ender_id") + ": " + container.getEnderId(), 60, 5, 0xffffff);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        // GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        assert this.minecraft != null;
        this.minecraft.getTextureManager().bindTexture(GUI);
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.blit(matrixStack, relX, relY, 0, 0, this.xSize, this.ySize);
    }
}
