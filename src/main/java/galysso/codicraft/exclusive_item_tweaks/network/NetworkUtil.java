package galysso.codicraft.exclusive_item_tweaks.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryOps;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.pixeldreamstudios.exclusiveitem.ExclusiveItemMod;
import net.pixeldreamstudios.exclusiveitem.ExclusiveItemStorage;
import net.pixeldreamstudios.exclusiveitem.ExclusiveItemUtil;

import static net.pixeldreamstudios.exclusiveitem.ExclusiveItemMod.MOD_ID;

public class NetworkUtil {
    public static final Identifier ForgetItem = Identifier.of(MOD_ID,"forget_item");

    public static void init() {
        PayloadTypeRegistry.playC2S().register(ForgetItemPayload.ID, ForgetItemPayload.CODEC);


        ServerPlayNetworking.registerGlobalReceiver(ForgetItemPayload.ID, (payload, context) -> {
            var tag = payload.itemNbt();
            var ops = RegistryOps.of(NbtOps.INSTANCE, context.player().getRegistryManager());
            ItemStack stack = ItemStack.CODEC.parse(ops, tag).result().orElse(ItemStack.EMPTY);
            context.server().execute(() -> {
                ExclusiveItemStorage.remove(context.player(), stack);
                ExclusiveItemStorage.syncToClient(context.player());
            });
        });
    }
}
