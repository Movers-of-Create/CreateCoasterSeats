package net.villagerzock.createcoasterseats.restrictions;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.jfr.Environment;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;
import net.villagerzock.createcoasterseats.block.IPlayerAnimationModificator;
import net.villagerzock.createcoasterseats.block.SeatStyle;
import net.villagerzock.createcoasterseats.lib.ClientOnly;

public class RestrictorType {
    private final float closedAngle;
    private final float openAngle;
    private final Vec3 offset;
    @OnlyIn(Dist.CLIENT)
    private PartialModel model;

    private final SeatStyle style;
    private final Item item;
    private final IPlayerAnimationModificator playerAnimationModificator;

    public RestrictorType(ResourceLocation model, float closedAngle, float openAngle, Vec3 offset, SeatStyle style, Item item, IPlayerAnimationModificator playerAnimationModificator){
        this.closedAngle = closedAngle;
        this.openAngle = openAngle;
        this.offset = offset;
        this.style = style;
        this.item = item;
        this.playerAnimationModificator = playerAnimationModificator;
        if (FMLEnvironment.dist == Dist.CLIENT){
            this.model = PartialModel.of(model.withPrefix("block/restrictor/"));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public PartialModel getModel() {
        return model;
    }

    public SeatStyle getStyle() {
        return style;
    }

    public Item getItem(){
        return this.item;
    }

    public Vec3 getOffset() {
        return offset;
    }

    public float getOpenAngle() {
        return openAngle;
    }

    public float getClosedAngle() {
        return closedAngle;
    }

    public IPlayerAnimationModificator getPlayerAnimationModificator() {
        return playerAnimationModificator;
    }
}
