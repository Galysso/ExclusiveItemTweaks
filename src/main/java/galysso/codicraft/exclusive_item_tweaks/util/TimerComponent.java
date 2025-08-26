package galysso.codicraft.exclusive_item_tweaks.util;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class TimerComponent {
    public static final ComponentType<Long> DEADLINE = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of("exclusiveitem", "timer_seconds"),
            ComponentType.<Long>builder().codec(Codec.LONG).build()
    );

    public static void init() {}
}
