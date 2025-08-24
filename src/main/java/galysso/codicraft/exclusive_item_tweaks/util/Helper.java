package galysso.codicraft.exclusive_item_tweaks.util;

import com.github.theredbrain.rpginventory.RPGInventory;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class Helper {
    public static @Nullable ServerPlayerEntity getServerPlayerEntity(ItemStack itemStack) {
        System.out.println("A");
        if (itemStack == null || itemStack.isEmpty()) return null;

        System.out.println("B");
        if (!itemStack.contains(RPGInventory.PLAYER_BOUND)) return null;

        System.out.println("C");
        ProfileComponent profileComponent = itemStack.get(RPGInventory.PLAYER_BOUND);
        if (profileComponent == null) return null;

        System.out.println("D");
        MinecraftServer server = ServerAccessor.getServer();
        if (server == null) return null;

        System.out.println("E");
        Optional<UUID> optUuid = profileComponent.id();
        if (optUuid.isEmpty()) return null;

        System.out.println("F");
        return server.getPlayerManager().getPlayer(optUuid.get());
    }
}
