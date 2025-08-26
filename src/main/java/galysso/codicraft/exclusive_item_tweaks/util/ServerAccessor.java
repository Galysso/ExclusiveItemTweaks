package galysso.codicraft.exclusive_item_tweaks.util;

import com.github.theredbrain.rpginventory.RPGInventory;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.pixeldreamstudios.exclusiveitem.ExclusiveItemUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class ServerAccessor {
    private static MinecraftServer server;

    public static void init() {
        ServerLifecycleEvents.SERVER_STARTED.register(s -> server = s);
        ServerLifecycleEvents.SERVER_STOPPED.register(s -> server = null);
    }

    public static MinecraftServer getServer() {
        return server;
    }

    @Nullable
    public static ServerWorld getOverworld() {
        return server.getWorld(World.OVERWORLD);
    }
}
