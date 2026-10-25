package net.villagerzock.createcoasterseats.registry;

import com.simibubi.create.content.fluids.VirtualFluid;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.neoforged.bus.api.IEventBus;
import net.villagerzock.createcoasterseats.Createcoasterseats;

public class ModFluids {

    private static final CreateRegistrate REGISTRATE =
            CreateRegistrate.create(Createcoasterseats.MOD_ID);

    public static final FluidEntry<VirtualFluid> MOLTEN_PLASTIC =
            REGISTRATE.virtualFluid("molten_plastic")
                    .register();

    public static void register(IEventBus modEventBus) {
        REGISTRATE.registerEventListeners(modEventBus);
    }
}
