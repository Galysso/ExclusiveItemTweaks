package galysso.codicraft.exclusive_item_tweaks.mixin;

import com.github.theredbrain.rpginventory.RPGInventory;
import galysso.codicraft.exclusive_item_tweaks.util.Helper;
import galysso.codicraft.exclusive_item_tweaks.util.ServerAccessor;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.pixeldreamstudios.exclusiveitem.ExclusiveItemStorage;
import net.pixeldreamstudios.exclusiveitem.ExclusiveItemUtil;
import net.pixeldreamstudios.exclusiveitem.ExclusiveItemWorldStorage;
import net.pixeldreamstudios.exclusiveitem.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(
            method = "discard",
            at = @At("HEAD")
    )
    private void onSetRemoved(CallbackInfo ci) {
        System.out.println("Entity remove called");

        Entity this_entity = (Entity) (Object) this;

        if (this_entity.getWorld().isClient()) return;
        if (!(this_entity instanceof ItemEntity itemEntity)) return;

        ItemStack itemStack = itemEntity.getStack();

        ServerPlayerEntity player = Helper.getServerPlayerEntity(itemStack);

        System.out.println("Player is " + player);

        if (player == null) return;

        System.out.println("Item thrown: " + itemStack);
        System.out.println("Is owned: " + ExclusiveItemUtil.isOwned(itemStack));
        System.out.println("Owner name: " + ExclusiveItemUtil.getOwnerName(itemStack));

        ExclusiveItemStorage.add(player, itemStack);
    }

}
