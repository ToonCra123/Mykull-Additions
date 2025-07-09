package net.mykull.mykulladditions;

import net.mykull.lib.config.JsonConfig;
import net.mykull.mykulladditions.compat.TOP.TopCompatibility;
import net.mykull.mykulladditions.datagen.DataGeneration;
import net.mykull.mykulladditions.events.PlayerRadiationEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(MykullsAdditions.MODID)
public class MykullsAdditions {
    public static final String MODID = "mykullsadditions";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static final JsonConfig<ConfigModel> CONFIG = new JsonConfig<>(
            "mykulls_config.json",
            ConfigModel.class
    );

    public MykullsAdditions(IEventBus modEventBus, ModContainer modContainer) {
        // Loads Blocks, Items, BE and Creative Tabs
        Registration.init(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addListener(DataGeneration::generate);

        NeoForge.EVENT_BUS.register(new PlayerRadiationEvent());
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, Registration.COMPLEX_BLOCK_ENTITY.get(), (o, direction) -> o.getItemHandler());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, Registration.GENERATOR_BLOCK_ENTITY.get(), (o, direction) -> o.getItemHandler());
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, Registration.GENERATOR_BLOCK_ENTITY.get(), (o, direction) -> o.getEnergyHandler());
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, Registration.CABLE_BLOCK_ENTITY.get(), (o, direction) -> o.getEnergyHandler());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        TopCompatibility.register();
        CONFIG.load();
    }


}
