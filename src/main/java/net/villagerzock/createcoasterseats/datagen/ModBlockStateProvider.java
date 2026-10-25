package net.villagerzock.createcoasterseats.datagen;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.villagerzock.createcoasterseats.block.SeatStyle;
import net.villagerzock.createcoasterseats.block.SeatType;
import net.villagerzock.createcoasterseats.block.SecurableSeatBlock;
import net.villagerzock.createcoasterseats.registry.ModBlocks;

import java.util.Map;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, String modid, ExistingFileHelper exFileHelper) {
        super(output, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (DyeColor color : DyeColor.values()) {
            for (SeatStyle style : SeatStyle.values()) {
                for (boolean powered : new boolean[]{false, true}) {
                    for (SeatType type : SeatType.values()) {
                        ModelFile model = seatModel(color, type, style, powered);
                        for (Direction direction : Direction.Plane.HORIZONTAL) {
                            getVariantBuilder(ModBlocks.SECURABLE_SEAT.get())
                                .partialState()
                                .with(SecurableSeatBlock.COLOR, color)
                                .with(SecurableSeatBlock.SEAT_TYPE, type)
                                .with(SecurableSeatBlock.SEAT_STYLE, style)
                                .with(SecurableSeatBlock.POWERED, powered)
                                .with(SecurableSeatBlock.FACING, direction)
                                .modelForState()
                                .modelFile(model)
                                .rotationY(style == SeatStyle.BASE
                                        ? ((int) direction.toYRot() + 180) % 360
                                        : (int) direction.toYRot())
                                .addModel();
                        }
                    }
                }
            }
        }
    }

    private ModelFile seatModel(DyeColor color, SeatType type, SeatStyle style, boolean powered) {
        if (style == SeatStyle.BASE && type == SeatType.WOOL) {
            return models().getExistingFile(ResourceLocation.fromNamespaceAndPath(
                    "create", "block/" + color.getName() + "_seat"));
        }

        boolean lapBar = style == SeatStyle.LAP_BAR;
        String baseModel = style == SeatStyle.BASE ? "base_seat" : lapBar ? "lapbar_seat" : "restrictor_seat";
        String poweredName = powered ? "on" : "off";
        ModelBuilder<?> model = models().withExistingParent(
                color.getName() + "_" + type.getSerializedName() + "_" + baseModel + "_" + poweredName,
                modLoc("block/" + baseModel));
        String top = type == SeatType.PLASTIC
                ? "createcoasterseats:block/secured_seat/top_" + color.getName() + "_plastic"
                : "create:block/seat/top_" + color.getName();
        String side = type == SeatType.PLASTIC
                ? "createcoasterseats:block/secured_seat/side_" + color.getName() + "_plastic"
                : "create:block/seat/side_" + color.getName();
        Map<String, String> textures = Map.of(
                "0", "createcoasterseats:block/secured_seat/sidelow_" + poweredName,
                "2", "createcoasterseats:block/secured_seat/casing_" + poweredName,
                "3", "create:block/andesite_casing",
                "41", "createcoasterseats:block/secured_seat/link",
                "particle", top,
                lapBar ? "7" : "15", top,
                lapBar ? "22" : "30", side);
        textures.forEach((key, texture) -> model.texture(key, ResourceLocation.parse(texture)));
        return model;
    }
}
