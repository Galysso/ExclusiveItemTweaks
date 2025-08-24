package galysso.codicraft.exclusive_item_tweaks;

import galysso.codicraft.exclusive_item_tweaks.util.ServerAccessor;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.pixeldreamstudios.exclusiveitem.api.ExclusiveItemEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExclusiveItemTweaksMain implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("modid");

	@Override
	public void onInitialize() {
        ServerAccessor.init();
        ExclusiveItemTweaksMain.LOGGER.info("Initialisation du client de Mon Mod!");
        ExclusiveItemEvents.SHOULD_ADD_TO_STORAGE.register((player, stack) -> false);
    }
}