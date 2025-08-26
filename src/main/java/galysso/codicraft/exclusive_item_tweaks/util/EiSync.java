package galysso.codicraft.exclusive_item_tweaks.util;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public final class EiSync {
    private EiSync() {}

    public static void ensureExclusiveFor(ServerPlayerEntity player, ItemStack stack) {
        NbtComponent comp = stack.get(DataComponentTypes.CUSTOM_DATA);
        NbtCompound nbt = comp != null ? comp.copyNbt() : new NbtCompound();

        if (!nbt.getBoolean("ExclusiveItem")) nbt.putBoolean("ExclusiveItem", true);
        if (!nbt.containsUuid("exclusiveOwner")) {
            nbt.putUuid("exclusiveOwner", player.getUuid());
            nbt.putString("exclusiveOwnerName", player.getName().getString());
        }
        if (!nbt.containsUuid("exclusiveID")) {
            nbt.putUuid("exclusiveID", UUID.randomUUID());
        }
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
    }
}