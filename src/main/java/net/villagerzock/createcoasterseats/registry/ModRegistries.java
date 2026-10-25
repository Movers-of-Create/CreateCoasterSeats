package net.villagerzock.createcoasterseats.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import net.villagerzock.createcoasterseats.Createcoasterseats;
import net.villagerzock.createcoasterseats.restrictions.RestrictorType;

public class ModRegistries {
    public static final ResourceKey<Registry<RestrictorType>> RESTRICTOR_TYPES_KEY = ResourceKey.createRegistryKey(
            ResourceLocation.fromNamespaceAndPath(Createcoasterseats.MOD_ID, "restrictor_types")
    );

    public static final Registry<RestrictorType> RESTRICTOR_TYPES =
            new RegistryBuilder<>(RESTRICTOR_TYPES_KEY)
                    .sync(false)
                    .create();

    public static void register(NewRegistryEvent event) {
        event.register(RESTRICTOR_TYPES);
    }
}
