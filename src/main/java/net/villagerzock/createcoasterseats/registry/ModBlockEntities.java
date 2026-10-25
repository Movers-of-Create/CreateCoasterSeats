package net.villagerzock.createcoasterseats.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.villagerzock.createcoasterseats.Createcoasterseats;
import net.villagerzock.createcoasterseats.block.SecurableSeatBlock;
import net.villagerzock.createcoasterseats.block.entity.SecurableSeatBlockEntity;

import java.util.ArrayList;
import java.util.List;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Createcoasterseats.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SecurableSeatBlockEntity>> SECURABLE_SEAT =
        BLOCK_ENTITIES.register("securable_seat", () -> {

            return BlockEntityType.Builder.of(
                    SecurableSeatBlockEntity::new,
                    ModBlocks.SECURABLE_SEAT.get()
            ).build(null);
        });

    private ModBlockEntities() {
    }
}
