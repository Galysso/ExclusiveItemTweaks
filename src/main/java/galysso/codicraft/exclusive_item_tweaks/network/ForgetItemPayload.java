package galysso.codicraft.exclusive_item_tweaks.network;

import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.impl.registry.sync.packet.DirectRegistryPacketHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import static net.pixeldreamstudios.exclusiveitem.ExclusiveItemMod.MOD_ID;

public record ForgetItemPayload(NbtCompound itemNbt) implements CustomPayload {
    public static final Id<ForgetItemPayload> ID = new Id<>(NetworkUtil.ForgetItem);

    public static final PacketCodec<ByteBuf, ForgetItemPayload> CODEC =
            PacketCodecs.NBT_COMPOUND.xmap(ForgetItemPayload::new, ForgetItemPayload::itemNbt);

    @Override
    public Id<? extends CustomPayload> getId() {return ID;}
}
