package galysso.codicraft.exclusive_item_tweaks.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.pixeldreamstudios.exclusiveitem.ExclusiveItemStorage;
import net.pixeldreamstudios.exclusiveitem.network.ExclusiveItemServerHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExclusiveItemServerHooks.class)
public class ExclusiveItemServerHooksMixin {
    @Inject(method = "beforeSyncAfterClaim", at = @At("HEAD"), cancellable = true)
    private static void beforeSyncAfterClaim(ServerPlayerEntity player, ItemStack claimed, CallbackInfo ci) {
        ExclusiveItemStorage.remove(player, claimed);
    }
}


