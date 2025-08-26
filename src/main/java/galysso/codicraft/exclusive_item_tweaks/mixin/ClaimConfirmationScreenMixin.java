package galysso.codicraft.exclusive_item_tweaks.mixin;

import galysso.codicraft.exclusive_item_tweaks.network.ForgetItemPayload;
import galysso.codicraft.exclusive_item_tweaks.util.TimerUtil;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryOps;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.pixeldreamstudios.exclusiveitem.client.ClaimConfirmationScreen;
import net.pixeldreamstudios.exclusiveitem.client.ReclaimScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ClaimConfirmationScreen.class)
public class ClaimConfirmationScreenMixin extends Screen {
    @Final
    @Shadow
    private ItemStack itemToClaim;

    @Unique
    int x;
    @Unique
    int y;
    @Unique
    int forgetButtonWidth = 48;
    @Unique
    int forgetButtonHeight = 20;
    @Unique
    int spacing = 8;
    @Unique
    int forgetButtonX;
    @Unique
    int forgetButtonY;
    @Unique
    int forgetButtonColor = 0xFF444444;
    @Unique
    int forgetButtonColorHovered = 0xFFAA0000;
    @Unique
    int forgetTextColor = 0xFF5555;
    @Unique
    int forgetTextColorHovered = 0xFF5555;

    protected ClaimConfirmationScreenMixin(Text title) {
        super(title);
    }

    @ModifyVariable(
        method = "render",
        at = @At("STORE"),
        ordinal = 5
    )
    private boolean changeCanClaim(boolean canClaim) {
        return canClaim && !TimerUtil.isExpired(MinecraftClient.getInstance().world, itemToClaim);
    }

    @Redirect(
        method = "render", // ← ex: render(...)/draw(...), le nom réel
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
            ordinal = 0
        ),
        require = 1
    )
    private boolean ei$afterMissingRequirementTooltip(List<Text> tooltips, Object element /* = Text */) {
        tooltips.add((Text) element);

        if (TimerUtil.isExpired(MinecraftClient.getInstance().world, itemToClaim)) {
            tooltips.add(Text.translatable("exclusiveitem.gui.not_expired"));
        }

        return true;
    }

    private boolean mouseOnForget(double mouseX, double mouseY) {
        return mouseX >= forgetButtonX && mouseX <= forgetButtonX + forgetButtonWidth && mouseY >= forgetButtonY && mouseY <= forgetButtonY + forgetButtonHeight;
    }

    @Inject(
        method = "render",
        at = @At("HEAD")
    )
    private void onRenderHead(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        x = (this.width - 324) / 2;
        y = (this.height - 200) / 2;
        forgetButtonX = x + spacing;
        forgetButtonY = y + spacing;
    }

    @Inject(
        method = "render",
        at = @At("TAIL")
    )
    private void onRenderTail(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        boolean hovered = mouseOnForget(mouseX, mouseY);
        context.fill(forgetButtonX, forgetButtonY, forgetButtonX + forgetButtonWidth, forgetButtonY + forgetButtonHeight, hovered ? forgetButtonColorHovered : forgetButtonColor);
        //System.out.println("Rendering Forget button at (" + btnX + ", " + btnY + ")");
        context.drawCenteredTextWithShadow(textRenderer, Text.translatable("forget"), forgetButtonX + forgetButtonWidth / 2, forgetButtonY + 6, hovered ? forgetTextColorHovered : forgetTextColor); //exclusiveitem.gui.forget
        if (hovered) {
            List<Text> forgetTooltip = List.of(Text.translatable("exclusiveitem.gui.forget.tooltip").formatted(Formatting.RED));
            context.drawTooltip(textRenderer, forgetTooltip, mouseX, mouseY);
        }
    }

    @Inject(
        method = "mouseClicked",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (mouseOnForget(mouseX, mouseY)) {

            var mc = MinecraftClient.getInstance();
            if (mc.world == null) return;

            // Encode l’ItemStack en NBT avec les registries du client
            var ops = RegistryOps.of(NbtOps.INSTANCE, mc.world.getRegistryManager());
            var encoded = ItemStack.CODEC.encodeStart(ops, itemToClaim.copy()).result().orElse(null);
            if (!(encoded instanceof NbtCompound tag)) return;

            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeNbt(tag);
            ForgetItemPayload payload = new ForgetItemPayload(tag);
            ClientPlayNetworking.send(payload);

            MinecraftClient.getInstance().setScreen(new ReclaimScreen(false));
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
