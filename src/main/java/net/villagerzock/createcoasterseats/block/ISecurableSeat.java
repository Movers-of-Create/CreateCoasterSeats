package net.villagerzock.createcoasterseats.block;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public interface ISecurableSeat {
    boolean isSecured(BlockState state, BlockPos pos, Level level);
    int getMaxAngle();
    int getMinAngle();
    Vec3 getRestrictorOffset();
    PartialModel getRestrictorModel();
}
