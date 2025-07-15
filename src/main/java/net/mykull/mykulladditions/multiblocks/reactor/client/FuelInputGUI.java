package net.mykull.mykulladditions.multiblocks.reactor.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.mykull.mykulladditions.MykullsAdditions;
import net.mykull.mykulladditions.common.containers.GeneratorContainer;

public class FuelInputGUI extends AbstractContainerScreen<FuelInputContainer> {

    public static final ResourceLocation GUI = ResourceLocation.fromNamespaceAndPath(MykullsAdditions.MODID, "textures/gui/reactor/fuel_input.png");

    public FuelInputGUI(FuelInputContainer menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageHeight = 152;
        this.imageWidth = 180;
        this.inventoryLabelY = this.imageHeight - 96;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(GUI, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
