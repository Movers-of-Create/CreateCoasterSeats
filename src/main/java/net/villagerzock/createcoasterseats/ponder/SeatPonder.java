package net.villagerzock.createcoasterseats.ponder;

import com.simibubi.create.AllItems;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.createmod.ponder.foundation.instruction.RotateSceneInstruction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.villagerzock.createcoasterseats.block.entity.SecurableSeatBlockEntity;

import java.lang.reflect.UndeclaredThrowableException;

public class SeatPonder {
    public static void sceneOne(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("securable_seat", "Securable Seat");
        scene.configureBasePlate(0, 0, 5);
        scene.removeShadow();

        Selection basePlate = util.select().layer(0);
        scene.world().showSection(
                util.select().everywhere(),
                Direction.DOWN
        );

        scene.idle(20);

        scene.overlay()
                .showControls(
                        util.vector().topOf(new BlockPos(2,1,2)),
                        Pointing.DOWN,
                        20
                )
                .rightClick()
                .withItem(AllItems.WRENCH.asStack());
        scene.overlay()
                .showText(20)
                .text("You can change the Activation Mode using a Wrench")
                .pointAt(util.vector().blockSurface(new BlockPos(2,1,2), Direction.DOWN));

        BlockPos seatPos = util.grid().at(2, 1, 2);
        Selection seatSelection = util.select().position(seatPos);

        ItemStack iron = new ItemStack(Items.IRON_INGOT);
        ItemStack sapling = new ItemStack(Items.OAK_SAPLING);

        scene.world().hideSection(basePlate, Direction.DOWN);
        scene.idle(20);

        scene.addInstruction(
                new RotateSceneInstruction(90, 0, true)
        );

        scene.idle(20);
        scene.addKeyframe();

        Vec3 lowerSlot = util.vector()
                .blockSurface(seatPos, Direction.DOWN)
                .add(0, 0.15, -0.15);

        Vec3 upperSlot = util.vector()
                .blockSurface(seatPos, Direction.DOWN)
                .add(0, 0.15, 0.15);

        scene.overlay()
                .showControls(lowerSlot, Pointing.DOWN, 30)
                .withItem(iron);

        scene.idle(7);

        scene.world().modifyBlockEntityNBT(
                seatSelection,
                SecurableSeatBlockEntity.class,
                nbt -> nbt.put(
                        "FrequencyLast",
                        iron.saveOptional(scene.world().getHolderLookupProvider())
                )
        );

        scene.overlay()
                .showControls(upperSlot, Pointing.UP, 30)
                .withItem(sapling);

        scene.idle(7);

        scene.world().modifyBlockEntityNBT(
                seatSelection,
                SecurableSeatBlockEntity.class,
                nbt -> nbt.put(
                        "FrequencyFirst",
                        sapling.saveOptional(scene.world().getHolderLookupProvider())
                )
        );

        scene.idle(20);

        scene.addInstruction(
                new RotateSceneInstruction(-90, 0, true)
        );

        scene.idle(20);

        scene.world().showSection(
                basePlate,
                Direction.UP
        );

        scene.idle(80);
    }

    public static void sceneTwo(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("securable_seat_link", "Link Mode");
        scene.configureBasePlate(0, 0, 5);

        Selection basePlate = util.select().layer(0);

        Selection seat = util.select().position(2,1,2);
        scene.world().showSection(basePlate.add(seat), Direction.DOWN);

        scene.idle(10);

        Selection link = util.select().position(0,1,2);
        Selection torch = util.select().position(1,1,2);

        scene.world().showSection(link.add(link), Direction.DOWN);

        scene.idle(10);

        scene.world().showSection(torch.add(torch), Direction.DOWN);
    }
}
