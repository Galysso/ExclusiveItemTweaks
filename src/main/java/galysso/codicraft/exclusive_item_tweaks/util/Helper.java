package galysso.codicraft.exclusive_item_tweaks.util;

import com.github.theredbrain.rpginventory.RPGInventory;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.pixeldreamstudios.exclusiveitem.ExclusiveItemStorage;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class Helper {
    public static @Nullable ServerPlayerEntity getServerPlayerEntity(ItemStack itemStack) {
        if (itemStack == null || itemStack.isEmpty()) return null;

        if (!itemStack.contains(RPGInventory.PLAYER_BOUND)) return null;

        ProfileComponent profileComponent = itemStack.get(RPGInventory.PLAYER_BOUND);
        if (profileComponent == null) return null;

        MinecraftServer server = ServerAccessor.getServer();
        if (server == null) return null;

        Optional<UUID> optUuid = profileComponent.id();
        if (optUuid.isEmpty()) return null;

        return server.getPlayerManager().getPlayer(optUuid.get());
    }

    public static void removeItemFromStorage(ServerPlayerEntity player, ItemStack itemStack) {
        TimerUtil.clearTimer(itemStack);
        ExclusiveItemStorage.remove(player, itemStack);
        ExclusiveItemStorage.syncToClient(player);
    }
}
