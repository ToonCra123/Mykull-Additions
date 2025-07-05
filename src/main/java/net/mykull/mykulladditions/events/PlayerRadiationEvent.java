package net.mykull.mykulladditions.events;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.mykull.mykulladditions.Registration;
import net.mykull.mykulladditions.common.items.RadioactiveBlockItem;
import net.mykull.mykulladditions.common.items.RadioactiveItem;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class PlayerRadiationEvent {
    @SubscribeEvent
    public void playerTick(PlayerTickEvent.Post event) {
        int radiationInTick = 0;
        Player player = event.getEntity();

        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof RadioactiveItem radItem) {
                radiationInTick += (int) (radItem.getRadiationLevel() * stack.getCount());
            }
            if (stack.getItem() instanceof RadioactiveBlockItem radItem) {
                radiationInTick += (int) (radItem.getRadiationLevel() * stack.getCount());
            }
        }

        event.getEntity().setData(Registration.RADIATION, event.getEntity().getData(Registration.RADIATION) + radiationInTick);
    }
}
