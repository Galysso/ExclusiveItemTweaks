package galysso.codicraft.exclusive_item_tweaks.mixin;

import galysso.codicraft.exclusive_item_tweaks.util.Helper;
import galysso.codicraft.exclusive_item_tweaks.util.ServerAccessor;
import galysso.codicraft.exclusive_item_tweaks.util.TimerUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryOps;
import net.minecraft.server.network.ServerPlayerEntity;
import net.pixeldreamstudios.exclusiveitem.network.ClaimExclusiveItemPayload;
import net.pixeldreamstudios.exclusiveitem.network.ExclusiveItemClaimHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExclusiveItemClaimHandler.class)
public class ExclusiveItemHandlerMixin {

    @Inject(
            method = "handleClaimPayload",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void onHandleClaimPayloadHead(ServerPlayerEntity player, ClaimExclusiveItemPayload payload, CallbackInfo ci) {
        System.out.println("[Exclusive Item Tweaks] Handling claim payload for player: " + player.getName().getString());
        RegistryOps<NbtElement> ops = RegistryOps.of(NbtOps.INSTANCE, player.getRegistryManager());
        ItemStack claimed = ItemStack.CODEC.parse(ops, payload.itemNbt()).result().orElseThrow();
        if (!TimerUtil.canBeClaimed(ServerAccessor.getOverworld(), claimed)) {
            System.out.println("[Exclusive Item Tweaks] Claimed item is expired, removing from exclusive storage: " + claimed);
            Helper.removeItemFromStorage(player, claimed);
            ci.cancel();
        }
    }
}
