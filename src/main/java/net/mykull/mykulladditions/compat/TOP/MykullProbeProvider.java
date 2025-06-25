package net.mykull.mykulladditions.compat.TOP;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.mykull.mykulladditions.MykullsAdditions;
import net.mykull.mykulladditions.common.blockentities.CableBlockEntity;
import net.mykull.mykulladditions.common.cables.blocks.CableBlock;

public class MykullProbeProvider implements IProbeInfoProvider {
    @Override
    public ResourceLocation getID() {
        return ResourceLocation.fromNamespaceAndPath(MykullsAdditions.MODID, "cable_top_provider");
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, Player player, Level level,
                             BlockState blockState, IProbeHitData data) {

        BlockEntity be = level.getBlockEntity(data.getPos());
        if (be instanceof CableBlockEntity cbe) {
            // Add any custom info here – but do NOT add energy
            // No energy shown
            probeInfo.text(Component.literal("Sup"));
        }
    }
}