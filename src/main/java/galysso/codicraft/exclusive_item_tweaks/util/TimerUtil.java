package galysso.codicraft.exclusive_item_tweaks.util;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static java.lang.Math.max;

public class TimerUtil {
    private static final long grace_period = 20 * 5; // 5 seconds
    // private static final long max_delay = 20 * 60 * 60 * 24 * 2; // 2 days
    private static final long max_delay = 20 * 10; // 10 seconds for testing

    private TimerUtil() {}

    public static final long NO_DEADLINE = -1;

    public static long getTicksLeft(World world, ItemStack stack) {
        long deadline = stack.getOrDefault(TimerComponent.DEADLINE, NO_DEADLINE);
        if (deadline == NO_DEADLINE) return NO_DEADLINE;
        return max(0, deadline - world.getTime());
    }

    public static void setDelayTicks(@Nullable World world, ItemStack stack) {
        long serverTicks = 0;
        if (world != null) {
            serverTicks = world.getTime();
        }
        stack.set(TimerComponent.DEADLINE, serverTicks + max_delay);
    }

    public static String getFormattedTimeLeft(World world, ItemStack stack) {
        long ticksLeft = getTicksLeft(world, stack);
        if (ticksLeft == NO_DEADLINE) return "∞";
        if (ticksLeft <= 0) return Text.translatable("item.exclusive_item_tweaks.expired").getString();

        long totalSeconds = ticksLeft / 20;
        long days = totalSeconds / 86400;
        long hours = (totalSeconds % 86400) / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days).append("j ");
        }
        if (hours > 0) {
            sb.append(hours).append("h ");
        }
        if (minutes > 0 || hours > 0) {
            sb.append(minutes).append("m ");
        }
        if (ticksLeft < 20 * 60) {
            sb.append(seconds).append("s");
        }

        return sb.toString().trim();
    }

    public static int getFormatColor(World world, ItemStack stack) {
        long ticksLeft = getTicksLeft(world, stack);
        if (ticksLeft == NO_DEADLINE) return 0xFFFFFF; // White
        if (ticksLeft <= 0) return 0xAA0000; // Dark red for expired
        if (ticksLeft < 20 * 60 * 10) return 0xFF5555; // Red for less than 10 minutes
        if (ticksLeft < 20 * 60 * 60) return 0xFFAA00; // Orange for less than one hour
        if (ticksLeft < 20 * 60 * 60 * 3) return 0xFFFF55; // yellow for less than three hours
        return 0x00AA00; // Dark green otherwise
    }

    public static boolean hasTimer(ItemStack stack) {
        return stack.contains(TimerComponent.DEADLINE);
    }

    public static void clearTimer(ItemStack stack) {
        stack.remove(TimerComponent.DEADLINE);
    }

    /* returns true if (and only if) the item is not expired or is still in grace period */
    public static boolean canBeClaimed(World world, ItemStack stack) {
        long deadline = stack.getOrDefault(TimerComponent.DEADLINE, NO_DEADLINE);
        if (deadline == NO_DEADLINE) return true;
        long ticksLeft = deadline - world.getTime() + grace_period;
        return ticksLeft < 0;
    }

    public static boolean isExpired(World world, ItemStack stack) {
        long deadline = stack.getOrDefault(TimerComponent.DEADLINE, NO_DEADLINE);
        if (deadline == NO_DEADLINE) return false;
        return deadline - world.getTime() <= 0;
    }
}
