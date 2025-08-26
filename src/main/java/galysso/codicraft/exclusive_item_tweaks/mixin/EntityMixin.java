package galysso.codicraft.exclusive_item_tweaks.mixin;

import galysso.codicraft.exclusive_item_tweaks.util.EiSync;
import galysso.codicraft.exclusive_item_tweaks.util.Helper;
import galysso.codicraft.exclusive_item_tweaks.util.ServerAccessor;
import galysso.codicraft.exclusive_item_tweaks.util.TimerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.pixeldreamstudios.exclusiveitem.ExclusiveItemStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(
            method = "discard",
            at = @At("HEAD")
    )
    private void onDiscard(CallbackInfo ci) {
        Entity this_entity = (Entity) (Object) this;

        if (this_entity.getWorld().isClient()) return;
        if (!(this_entity instanceof ItemEntity itemEntity)) return;

        ItemStack itemStack = itemEntity.getStack();

        ServerPlayerEntity player = Helper.getServerPlayerEntity(itemStack);

        if (player == null) return;

        EiSync.ensureExclusiveFor(player, itemStack);
        TimerUtil.setDelayTicks(ServerAccessor.getOverworld(), itemStack);
        ExclusiveItemStorage.add(player, itemStack);
        ExclusiveItemStorage.syncToClient(player);
    }

}