package net.villagerzock.createcoasterseats.datagen;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.villagerzock.createcoasterseats.registry.ModBlocks;

import java.util.Map;
import java.util.function.Function;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(
            PackOutput output,
            String modid,
            ExistingFileHelper exFileHelper
    ) {
        super(output, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        registerSeat(
                color -> ModBlocks.RESTRICTOR_SEATS.get(color).get(),
                "restrictor_seat",
                "%1$s_restrictor_seat_%2$s",
                Map.of(
                        "0", "createcoasterseats:block/secured_seat/sidelow_%2$s",
                        "2", "createcoasterseats:block/secured_seat/casing_%2$s",
                        "15", "create:block/seat/top_%1$s",
                        "30", "create:block/seat/side_%1$s"
                ),
                true
        );

        registerSeat(
                color -> ModBlocks.LAPBAR_SEATS.get(color).get(),
                "lapbar_seat",
                "%1$s_lapbar_seat_%2$s",
                Map.of(
                        "0", "createcoasterseats:block/secured_seat/sidelow_%2$s",
                        "2", "createcoasterseats:block/secured_seat/casing_%2$s",
                        "7", "create:block/seat/top_%1$s",
                        "22", "create:block/seat/side_%1$s"
                ),
                false
        );
    }

    private void registerSeat(
            Function<DyeColor, Block> blockGetter,
            String baseModel,
            String coloredModelPattern,
            Map<String, String> textures,
            boolean isOppositeDirection
    ) {
        for (DyeColor color : DyeColor.values()) {
            generateSeat(
                    color,
                    false,
                    baseModel,
                    coloredModelPattern,
                    textures
            );

            generateSeat(
                    color,
                    true,
                    baseModel,
                    coloredModelPattern,
                    textures
            );

            for (boolean powered : new boolean[]{false, true}) {
                String poweredName = powered ? "on" : "off";

                ModelFile model = models().getExistingFile(
                        modLoc(
                                "block/" + coloredModelPattern.formatted(
                                        color.getName(),
                                        poweredName
                                )
                        )
                );

                for (Direction direction : Direction.Plane.HORIZONTAL) {
                    getVariantBuilder(blockGetter.apply(color))
                            .partialState()
                            .with(BlockStateProperties.POWERED, powered)
                            .with(BlockStateProperties.HORIZONTAL_FACING, isOppositeDirection ? direction.getOpposite() : direction)
                            .modelForState()
                            .modelFile(model)
                            .rotationY(((int) direction.toYRot() + 180) % 360)
                            .addModel();
                }
            }
        }
    }

    private void generateSeat(
            DyeColor color,
            boolean powered,
            String baseModel,
            String modelPattern,
            Map<String, String> textures
    ) {
        String colorName = color.getName();
        String poweredName = powered ? "on" : "off";

        String modelName = modelPattern.formatted(
                colorName,
                poweredName
        );

        ModelBuilder<?> model = models().withExistingParent(
                modelName,
                modLoc("block/" + baseModel)
        );

        for (Map.Entry<String, String> entry : textures.entrySet()) {
            String texture = entry.getValue().formatted(
                    colorName,
                    poweredName
            );

            model.texture(
                    entry.getKey(),
                    ResourceLocation.parse(texture)
            );
        }
    }
}