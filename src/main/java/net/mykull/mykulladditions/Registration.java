package net.mykull.mykulladditions;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.mykull.mykulladditions.common.blockentities.CableBlockEntity;
import net.mykull.mykulladditions.common.blockentities.ComplexBlockEntity;
import net.mykull.mykulladditions.common.blockentities.GeneratorBlockEntity;
import net.mykull.mykulladditions.common.blocks.ComplexBlock;
import net.mykull.mykulladditions.common.blocks.SimpleBlock;
import net.mykull.mykulladditions.common.blocks.machine.GeneratorBlock;
import net.mykull.mykulladditions.common.cables.blocks.CableBlock;
import net.mykull.mykulladditions.common.containers.GeneratorContainer;
import net.mykull.mykulladditions.common.items.RadioactiveItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

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


    // Blocks
    //public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block", BlockBehaviour.Properties.of().mapColor(MapColor.STONE));
    public static final DeferredBlock<Block> URANIUM_ORE = BLOCKS.registerSimpleBlock("uranium_ore", BlockBehaviour.Properties.of().strength(3.5f).sound(SoundType.STONE));
    public static final DeferredBlock<SimpleBlock> SIMPLE_BLOCK = BLOCKS.registerBlock("simple_block", SimpleBlock::new, BlockBehaviour.Properties.of().strength(3.5f).requiresCorrectToolForDrops().sound(SoundType.METAL).randomTicks());
    public static final DeferredBlock<ComplexBlock> COMPLEX_BLOCK = BLOCKS.registerBlock("complex_block", ComplexBlock::new, BlockBehaviour.Properties.of().strength(3.5f).requiresCorrectToolForDrops().sound(SoundType.METAL));
    public static final DeferredBlock<GeneratorBlock> GENERATOR_BLOCK = BLOCKS.registerBlock("generator_block", GeneratorBlock::new, BlockBehaviour.Properties.of().strength(3.5f).requiresCorrectToolForDrops().sound(SoundType.METAL));
    public static final DeferredBlock<CableBlock> CABLE_BLOCK = BLOCKS.registerBlock("cable_block", CableBlock::new, BlockBehaviour.Properties.of().strength(3.5f).requiresCorrectToolForDrops().sound(SoundType.METAL));


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


    // Block Entities
    public static final Supplier<BlockEntityType<ComplexBlockEntity>> COMPLEX_BLOCK_ENTITY = BLOCK_ENTITIES.register("complex_block",
            () -> BlockEntityType.Builder.of(ComplexBlockEntity::new, COMPLEX_BLOCK.get()).build(null));
    public static final Supplier<BlockEntityType<GeneratorBlockEntity>> GENERATOR_BLOCK_ENTITY = BLOCK_ENTITIES.register("generator_block_entity",
            () -> BlockEntityType.Builder.of(GeneratorBlockEntity::new, GENERATOR_BLOCK.get()).build(null));
    public static final Supplier<BlockEntityType<CableBlockEntity>> CABLE_BLOCK_ENTITY = BLOCK_ENTITIES.register("cable_block_entity",
            () -> BlockEntityType.Builder.of(CableBlockEntity::new, CABLE_BLOCK.get()).build(null));


    //menus
    public static final Supplier<MenuType<GeneratorContainer>> GENERATOR_CONTAINER = MENU_TYPES.register("generator_block",
            () -> IMenuTypeExtension.create((windowId, inv, data) -> new GeneratorContainer(windowId, inv.player, data.readBlockPos())));


    // Items
    //public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("example_item", new Item.Properties().food(new FoodProperties.Builder()
    //        .alwaysEdible().nutrition(1).saturationModifier(2f).build()));
    public static final DeferredItem<RadioactiveItem> RAW_URANIUM = ITEMS.register("raw_uranium",
            () -> new RadioactiveItem(new Item.Properties(), 10));
    public static final DeferredItem<RadioactiveItem> URANIUM_INGOT = ITEMS.register("uranium_ingot",
            () -> new RadioactiveItem(new Item.Properties(), 20));

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
                output.accept(URANIUM_ORE_ITEM.get());
                output.accept(RAW_URANIUM.get());
                output.accept(URANIUM_INGOT.get());
            }).build());

    public static void init(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        MENU_TYPES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
