package net.mykull.mykulladditions.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.mykull.mykulladditions.MykullsAdditions;
import net.mykull.mykulladditions.common.blockentities.GeneratorBlockEntity;
import net.mykull.mykulladditions.common.containers.GeneratorContainer;

public class GeneratorScreen extends AbstractContainerScreen<GeneratorContainer> {
    private static final int ENERGY_LEFT = 152;
    private static final int ENERGY_WIDTH = 14;
    private static final int ENERGY_TOP = 10;
    private static final int ENERGY_HEIGHT = 42;

    private final ResourceLocation GUI = ResourceLocation.fromNamespaceAndPath(MykullsAdditions.MODID, "textures/gui/generator.png");
    private final ResourceLocation ENERGY_GUI = ResourceLocation.fromNamespaceAndPath(MykullsAdditions.MODID, "textures/gui/utils.png");


    public GeneratorScreen(GeneratorContainer container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.imageHeight = 152;
        this.imageWidth = 180;
        this.inventoryLabelY = this.imageHeight - 96;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        graphics.blit(GUI, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
        int power = menu.getPower();
        int p = (int) ((power / (float) GeneratorBlockEntity.CAPACITY) * ENERGY_HEIGHT);

        // Energy Container shit
        graphics.blit(ENERGY_GUI, leftPos + ENERGY_LEFT, topPos + ENERGY_TOP, 0, 144, ENERGY_WIDTH, ENERGY_HEIGHT);
        graphics.blit(ENERGY_GUI, leftPos + ENERGY_LEFT, topPos + ENERGY_TOP + (ENERGY_HEIGHT-p), 16, 144 + (ENERGY_HEIGHT-p), ENERGY_WIDTH, p);
    }

    @Override
    public void render(GuiGraphics graphics, int mousex, int mousey, float partialTick) {
        super.render(graphics, mousex, mousey, partialTick);
        // Render tooltip with power if in the energy box
        if (mousex >= leftPos + ENERGY_LEFT && mousex < leftPos + ENERGY_LEFT + ENERGY_WIDTH && mousey >= topPos + ENERGY_TOP && mousey < topPos + ENERGY_TOP + ENERGY_HEIGHT) {
            int power = menu.getPower();
            graphics.renderTooltip(this.font, Component.literal(power + " RF"), mousex, mousey);
        }

        this.renderTooltip(graphics, mousex, mousey);
    }
}
