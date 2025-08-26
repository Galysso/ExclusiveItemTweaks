package galysso.codicraft.exclusive_item_tweaks.mixin;

import galysso.codicraft.exclusive_item_tweaks.util.TimerUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.pixeldreamstudios.exclusiveitem.client.ReclaimScreen;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ReclaimScreen.class)
public class ReclaimScreenMixin {
    @Inject(method = "renderFloatingItem", at = @At("TAIL"), cancellable = true)
    private void onRenderFloatingItem(ItemStack stack, DrawContext context, int x, int y, float tickDelta, float time, Vector3f axis, float speed, float bobOffset, boolean isHovered, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;
        //String label = Long.toString(TimerUtil.getTicksLeft(MinecraftClient.getInstance().world, stack));
        String label = TimerUtil.getFormattedTimeLeft(MinecraftClient.getInstance().world, stack);

        if (label != null && !label.isEmpty()) {
            int centerX = x + 8;                    // centre horizontal du carré 16×16
            int baselineY = y + 32;             // juste sous l’icône (ou y - tr.fontHeight - 2 pour au-dessus)
            int color = TimerUtil.getFormatColor(MinecraftClient.getInstance().world, stack);
            drawCenteredLabel(context, label, centerX, baselineY, color, false);
        }
    }

    @Unique
    private void drawCenteredLabel(DrawContext ctx, String text, int centerX, int baselineY, int argb, boolean shadow) {
        var tr = MinecraftClient.getInstance().textRenderer;
        int w = tr.getWidth(text);
        int x0 = centerX - (w / 2);
        ctx.drawText(tr, text, x0, baselineY, argb, shadow);
    }
}
