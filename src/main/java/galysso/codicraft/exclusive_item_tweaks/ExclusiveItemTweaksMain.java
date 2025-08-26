package galysso.codicraft.exclusive_item_tweaks;

import galysso.codicraft.exclusive_item_tweaks.network.NetworkUtil;
import galysso.codicraft.exclusive_item_tweaks.util.ServerAccessor;
import galysso.codicraft.exclusive_item_tweaks.util.TimerComponent;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.pixeldreamstudios.exclusiveitem.api.ExclusiveItemEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExclusiveItemTweaksMain implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("modid");

	@Override
	public void onInitialize() {
        ServerAccessor.init();
        TimerComponent.init();
        ExclusiveItemTweaksMain.LOGGER.info("Initialisation du client de Mon Mod!");
        ExclusiveItemEvents.SHOULD_ADD_TO_STORAGE.register((player, stack) -> false);

        NetworkUtil.init();
    }
}