package net.mykull.mykulladditions.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public class DataGeneration {
    public static void generate(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeClient(), new MykullBlockState(packOutput, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new MykullItemModels(packOutput, event.getExistingFileHelper()));

        //generator.addProvider(event.includeClient(), new TutLanguageProvider(packOutput, "en_us"));

        //TutBlockTags blockTags = new TutBlockTags(packOutput, lookupProvider, event.getExistingFileHelper());
        //generator.addProvider(event.includeServer(), blockTags);
        //generator.addProvider(event.includeServer(), new TutItemTags(packOutput, lookupProvider, blockTags, event.getExistingFileHelper()));
        //generator.addProvider(event.includeServer(), new TutRecipes(packOutput));
        //generator.addProvider(event.includeServer(), new LootTableProvider(packOutput, Collections.emptySet(),
                //List.of(new LootTableProvider.SubProviderEntry(TutLootTables::new, LootContextParamSets.BLOCK))));
    }
}
