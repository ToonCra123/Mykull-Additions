package net.mykull.mykulladditions.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.mykull.mykulladditions.Registration;
import net.mykull.mykulladditions.client.blockentityrenderer.CableBlockEntityRenderer;
import net.mykull.mykulladditions.client.blockentityrenderer.ComplexBlockEntityRenderer;
import net.mykull.mykulladditions.client.screen.GeneratorScreen;
import net.mykull.mykulladditions.common.cables.client.CableModelLoader;
import net.mykull.mykulladditions.multiblocks.reactor.glass.ReactorGlassModelLoader;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import static net.mykull.mykulladditions.MykullsAdditions.MODID;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        event.register(Registration.GENERATOR_CONTAINER.get(), GeneratorScreen::new);
    }

    @SubscribeEvent
    public static void modelInit(ModelEvent.RegisterGeometryLoaders event) {
        CableModelLoader.register(event);
        ReactorGlassModelLoader.register(event);
    }

    @SubscribeEvent
    public static void initClient(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(Registration.COMPLEX_BLOCK_ENTITY.get(), ComplexBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(Registration.CABLE_BLOCK_ENTITY.get(), CableBlockEntityRenderer::new);
    }
}
