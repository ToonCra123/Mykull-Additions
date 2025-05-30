package net.mykull.mykulladditions;

import net.mykull.mykulladditions.datagen.DataGeneration;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
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
    private static final Logger LOGGER = LogUtils.getLogger();

    public MykullsAdditions(IEventBus modEventBus, ModContainer modContainer) {
        // Loads Blocks, Items, BE and Creative Tabs
        Registration.init(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addListener(DataGeneration::generate);
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, Registration.COMPLEX_BLOCK_ENTITY.get(), (o, direction) -> o.getItemHandler());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
    }


}
