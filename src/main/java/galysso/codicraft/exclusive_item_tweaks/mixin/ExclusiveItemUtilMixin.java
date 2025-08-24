package galysso.codicraft.exclusive_item_tweaks.mixin;

import com.github.theredbrain.rpginventory.util.ItemUtils;
import galysso.codicraft.exclusive_item_tweaks.util.Helper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.pixeldreamstudios.exclusiveitem.ExclusiveItemUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.item.ItemStack;

@Mixin(ExclusiveItemUtil.class)
public class ExclusiveItemUtilMixin {

    /*@Redirect(
        method = "bindToPlayer(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/PlayerEntity;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/pixeldreamstudios/exclusiveitem/ExclusiveItemStorage;add(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/item/ItemStack;)V"
        )
    )
    private static void codicraft_cancelAdd(
            net.minecraft.server.network.ServerPlayerEntity serverPlayer,
            net.minecraft.item.ItemStack stack
    ) { }*/

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static boolean isOwner(ItemStack stack, PlayerEntity player) {
        return ItemUtils.isOwnedByPlayer(stack, player.getGameProfile());
    }
}
