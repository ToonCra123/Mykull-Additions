package net.mykull.mykulladditions;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.mykull.mykulladditions.common.blockentities.CableBlockEntity;
import net.mykull.mykulladditions.common.blockentities.ComplexBlockEntity;
import net.mykull.mykulladditions.common.blockentities.GeneratorBlockEntity;
import net.mykull.mykulladditions.common.blocks.ComplexBlock;
import net.mykull.mykulladditions.common.blocks.SimpleBlock;
import net.mykull.mykulladditions.common.blocks.machine.GeneratorBlock;
import net.mykull.mykulladditions.common.cables.blocks.CableBlock;
import net.mykull.mykulladditions.common.containers.GeneratorContainer;
import net.mykull.mykulladditions.common.items.RadioactiveItem;
import net.mykull.mykulladditions.multiblocks.reactor.*;
import net.mykull.mykulladditions.multiblocks.reactor.client.FuelInputContainer;
import net.mykull.mykulladditions.multiblocks.reactor.client.MainReactorContainer;
import net.mykull.mykulladditions.multiblocks.reactor.client.MainReactorGUI;
import net.mykull.mykulladditions.multiblocks.reactor.io.FuelIOBlock;
import net.mykull.mykulladditions.multiblocks.reactor.io.FuelIOBlockEntity;
import net.mykull.mykulladditions.multiblocks.reactor.io.PowerTapBlock;
import net.mykull.mykulladditions.multiblocks.reactor.io.PowerTapBlockEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.*;

import java.util.function.Supplier;

import static net.mykull.mykulladditions.MykullsAdditions.MODID;


// Mykull Stuff
public class Registration {

    // Init bruv
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU, MODID);
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);


    // Blocks
    //public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block", BlockBehaviour.Properties.of().mapColor(MapColor.STONE));
    public static final DeferredBlock<Block> URANIUM_ORE = BLOCKS.registerSimpleBlock("uranium_ore", BlockBehaviour.Properties.of().strength(3.5f).sound(SoundType.STONE));
    public static final DeferredBlock<SimpleBlock> SIMPLE_BLOCK = BLOCKS.registerBlock("simple_block", SimpleBlock::new, BlockBehaviour.Properties.of().strength(3.5f).requiresCorrectToolForDrops().sound(SoundType.METAL).randomTicks());
    public static final DeferredBlock<ComplexBlock> COMPLEX_BLOCK = BLOCKS.registerBlock("complex_block", ComplexBlock::new, BlockBehaviour.Properties.of().strength(3.5f).requiresCorrectToolForDrops().sound(SoundType.METAL));
    public static final DeferredBlock<GeneratorBlock> GENERATOR_BLOCK = BLOCKS.registerBlock("generator_block", GeneratorBlock::new, BlockBehaviour.Properties.of().strength(3.5f).requiresCorrectToolForDrops().sound(SoundType.METAL));
    public static final DeferredBlock<CableBlock> CABLE_BLOCK = BLOCKS.registerBlock("cable_block", CableBlock::new, BlockBehaviour.Properties.of().strength(3.5f).requiresCorrectToolForDrops().sound(SoundType.METAL));
    public static final DeferredBlock<ReactorCasingBlock> REACTOR_CASING = BLOCKS.registerBlock("reactor_casing", ReactorCasingBlock::new, BlockBehaviour.Properties.of().strength(3.5f).requiresCorrectToolForDrops().sound(SoundType.METAL));
    public static final DeferredBlock<ReactorControllerBlock> REACTOR_CONTROLLER = BLOCKS.registerBlock("reactor_controller", ReactorControllerBlock::new, BlockBehaviour.Properties.of().strength(3.5f).requiresCorrectToolForDrops().sound(SoundType.METAL));
    public static final DeferredBlock<ControlRodBlock> REACTOR_CONTROL_ROD = BLOCKS.registerBlock("reactor_control_rod", ControlRodBlock::new, BlockBehaviour.Properties.of().strength(3.5f).requiresCorrectToolForDrops().sound(SoundType.METAL));
    public static final DeferredBlock<FuelRodBlock> REACTOR_FUEL_ROD = BLOCKS.registerBlock("reactor_fuel_rod", FuelRodBlock::new, BlockBehaviour.Properties.of().strength(3.5f).requiresCorrectToolForDrops().sound(SoundType.METAL));
    public static final DeferredBlock<PowerTapBlock> REACTOR_POWER_TAP = BLOCKS.registerBlock("reactor_power_tap", PowerTapBlock::new, BlockBehaviour.Properties.of().strength(3.5f).requiresCorrectToolForDrops().sound(SoundType.METAL));
    public static final DeferredBlock<FuelIOBlock> REACTOR_FUEL_IO = BLOCKS.registerBlock("reactor_fuel_io", FuelIOBlock::new, BlockBehaviour.Properties.of().strength(3.5f).requiresCorrectToolForDrops().sound(SoundType.METAL));



    // Block Items
    //public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);
    public static final DeferredItem<BlockItem> URANIUM_ORE_ITEM = ITEMS.register("uranium_ore",
            () -> new BlockItem(URANIUM_ORE.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> SIMPLE_BLOCK_ITEM = ITEMS.register("simple_block",
            () -> new BlockItem(SIMPLE_BLOCK.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> COMPLEX_BLOCK_ITEM = ITEMS.register("complex_block",
            () -> new BlockItem(COMPLEX_BLOCK.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> GENERATOR_BLOCK_ITEM = ITEMS.register("generator_block",
            () -> new BlockItem(GENERATOR_BLOCK.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> CABLE_BLOCK_ITEM = ITEMS.register("cable_block",
            () -> new BlockItem(CABLE_BLOCK.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> REACTOR_CASING_ITEM = ITEMS.register("reactor_casing",
            () -> new BlockItem(REACTOR_CASING.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> REACTOR_CONTROL_ROD_ITEM = ITEMS.register("reactor_control_rod",
            () -> new BlockItem(REACTOR_CONTROL_ROD.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> REACTOR_CONTROLLER_ITEM = ITEMS.register("reactor_controller",
            () -> new BlockItem(REACTOR_CONTROLLER.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> REACTOR_FUEL_ROD_ITEM = ITEMS.register("reactor_fuel_rod",
            () -> new BlockItem(REACTOR_FUEL_ROD.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> REACTOR_POWER_TAP_ITEM = ITEMS.register("reactor_power_tap",
            () -> new BlockItem(REACTOR_POWER_TAP.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> REACTOR_FUEL_IO_ITEM = ITEMS.register("reactor_fuel_io",
            () -> new BlockItem(REACTOR_FUEL_IO.get(), new Item.Properties()));




    // Block Entities
    public static final Supplier<BlockEntityType<ComplexBlockEntity>> COMPLEX_BLOCK_ENTITY = BLOCK_ENTITIES.register("complex_block",
            () -> BlockEntityType.Builder.of(ComplexBlockEntity::new, COMPLEX_BLOCK.get()).build(null));
    public static final Supplier<BlockEntityType<GeneratorBlockEntity>> GENERATOR_BLOCK_ENTITY = BLOCK_ENTITIES.register("generator_block_entity",
            () -> BlockEntityType.Builder.of(GeneratorBlockEntity::new, GENERATOR_BLOCK.get()).build(null));
    public static final Supplier<BlockEntityType<CableBlockEntity>> CABLE_BLOCK_ENTITY = BLOCK_ENTITIES.register("cable_block_entity",
            () -> BlockEntityType.Builder.of(CableBlockEntity::new, CABLE_BLOCK.get()).build(null));
    public static final Supplier<BlockEntityType<ReactorControllerBlockEntity>> REACTOR_CONTROLLER_ENTITY = BLOCK_ENTITIES.register("reactor_controller_block_entity",
            () -> BlockEntityType.Builder.of(ReactorControllerBlockEntity::new, REACTOR_CONTROLLER.get()).build(null));
    public static final Supplier<BlockEntityType<ReactorCasingBlockEntity>> REACTOR_CASINNG_ENTITY = BLOCK_ENTITIES.register("reactor_casing_block_entity",
            () -> BlockEntityType.Builder.of(ReactorCasingBlockEntity::new, REACTOR_CASING.get()).build(null));
    public static final Supplier<BlockEntityType<ControlRodBlockEntity>> REACTOR_CONTROL_ROD_ENTITY = BLOCK_ENTITIES.register("reactor_control_rod_block_entity",
            () -> BlockEntityType.Builder.of(ControlRodBlockEntity::new, REACTOR_CONTROL_ROD.get()).build(null));
    public static final Supplier<BlockEntityType<FuelRodBlockEntity>> REACTOR_FUEL_ROD_ENTITY = BLOCK_ENTITIES.register("reactor_fuel_rod_block_entity",
            () -> BlockEntityType.Builder.of(FuelRodBlockEntity::new, REACTOR_FUEL_ROD.get()).build(null));
    public static final Supplier<BlockEntityType<PowerTapBlockEntity>> REACTOR_POWER_TAP_ENTITY = BLOCK_ENTITIES.register("reactor_power_tap_block_entity",
            () -> BlockEntityType.Builder.of(PowerTapBlockEntity::new, REACTOR_POWER_TAP.get()).build(null));
    public static final Supplier<BlockEntityType<FuelIOBlockEntity>> REACTOR_FUEL_IO_ENTITY = BLOCK_ENTITIES.register("reactor_fuel_io_block_entity",
            () -> BlockEntityType.Builder.of(FuelIOBlockEntity::new, REACTOR_FUEL_IO.get()).build(null));


    //menus
    public static final Supplier<MenuType<GeneratorContainer>> GENERATOR_CONTAINER = MENU_TYPES.register("generator_block",
            () -> IMenuTypeExtension.create((windowId, inv, data) -> new GeneratorContainer(windowId, inv.player, data.readBlockPos())));
    public static final Supplier<MenuType<FuelInputContainer>> REACTOR_FUEL_CONTAINER = MENU_TYPES.register("reactor_fuel_io",
            () -> IMenuTypeExtension.create((windowId, inv, data) -> new FuelInputContainer(windowId, inv.player, data.readBlockPos())));
    public static final Supplier<MenuType<MainReactorContainer>> REACTOR_MAIN_CONTAINER = MENU_TYPES.register("reactor_main",
            () -> IMenuTypeExtension.create((windowId, inv, data) -> new MainReactorContainer(windowId, inv.player, data.readBlockPos())));


    // Items
    //public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("example_item", new Item.Properties().food(new FoodProperties.Builder()
    //        .alwaysEdible().nutrition(1).saturationModifier(2f).build()));
    public static final DeferredItem<RadioactiveItem> RAW_URANIUM = ITEMS.register("raw_uranium",
            () -> new RadioactiveItem(new Item.Properties(), 1));
    public static final DeferredItem<RadioactiveItem> URANIUM_INGOT = ITEMS.register("uranium_ingot",
            () -> new RadioactiveItem(new Item.Properties(), 2));
    public static final DeferredItem<RadioactiveItem> LEU_PELLET = ITEMS.register("leu_fuel_pellet",
            () -> new RadioactiveItem(new Item.Properties(), 2));
    public static final DeferredItem<RadioactiveItem> DEPLETED_LEU_PELLET = ITEMS.register("depleted_leu_fuel_pellet",
            () -> new RadioactiveItem(new Item.Properties(), 2));

    // Creative Tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.mykullsadditions")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> GENERATOR_BLOCK_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(SIMPLE_BLOCK_ITEM.get());
                output.accept(COMPLEX_BLOCK_ITEM.get());
                output.accept(GENERATOR_BLOCK_ITEM.get());
                output.accept(CABLE_BLOCK_ITEM.get());

                // Uranium
                output.accept(URANIUM_ORE_ITEM.get());
                output.accept(RAW_URANIUM.get());
                output.accept(URANIUM_INGOT.get());
                output.accept(LEU_PELLET.get());
                output.accept(DEPLETED_LEU_PELLET.get());

                //Reactor
                output.accept(REACTOR_CASING_ITEM.get());
                output.accept(REACTOR_CONTROLLER_ITEM.get());
                output.accept(REACTOR_CONTROL_ROD_ITEM.get());
                output.accept(REACTOR_FUEL_ROD_ITEM.get());
                output.accept(REACTOR_POWER_TAP_ITEM.get());
                output.accept(REACTOR_FUEL_IO_ITEM.get());
            }).build());

    // ATTACHMENTS
    public static final Supplier<AttachmentType<Integer>> RADIATION = ATTACHMENTS.register(
            "radiation", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build()
    );


    public static void init(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        MENU_TYPES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        ATTACHMENTS.register(modEventBus);
    }
}
