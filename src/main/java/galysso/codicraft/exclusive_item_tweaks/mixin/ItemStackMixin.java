package galysso.codicraft.exclusive_item_tweaks.mixin;

import com.github.theredbrain.rpginventory.RPGInventory;
import galysso.codicraft.exclusive_item_tweaks.util.Helper;
import net.minecraft.component.ComponentType;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.pixeldreamstudios.exclusiveitem.ExclusiveItemUtil;
import net.pixeldreamstudios.exclusiveitem.api.ExclusiveItemEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(
            method = "set(Lnet/minecraft/component/ComponentType;Ljava/lang/Object;)Ljava/lang/Object;",
            at = @At("TAIL")
    )
    private void codicraft_onSet(
            ComponentType<?> type,
            Object component,
            CallbackInfoReturnable<Object> cir
    ) {
        if (type != RPGInventory.PLAYER_BOUND) return;

        ItemStack stack = (ItemStack) (Object) this;
        ServerPlayerEntity player = Helper.getServerPlayerEntity(stack);

        if (player == null) return;

        MinecraftServer server = player.getServer();
        if (server == null) return;

        server.execute(() -> {
            ExclusiveItemUtil.bindToPlayer(stack, player);
        });
    }
}
