package net.villagerzock.createcoasterseats.item;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.actors.seat.SeatBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.villagerzock.createcoasterseats.block.SecurableSeatBlock;
import net.villagerzock.createcoasterseats.block.SeatType;
import net.villagerzock.createcoasterseats.block.entity.SecurableSeatBlockEntity;
import net.villagerzock.createcoasterseats.restrictions.RestrictorType;
import net.villagerzock.createcoasterseats.registry.ModBlocks;

import java.util.function.Supplier;

public class RestrictorItem extends Item {
    private final Supplier<RestrictorType> restrictorTypeSupplier;
    public RestrictorItem(Properties properties, Supplier<RestrictorType> restrictorTypeSupplier) {
        super(properties);
        this.restrictorTypeSupplier = restrictorTypeSupplier;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();

        BlockState state = level.getBlockState(pos);

        boolean createSeat = state.getBlock() instanceof SeatBlock seat
                && AllBlocks.SEATS.get(seat.getColor()).get() == seat;
        if (!(state.getBlock() instanceof SecurableSeatBlock) && !createSeat)
            return super.useOn(context);

        if (level.isClientSide())
            return InteractionResult.SUCCESS;

        if (createSeat) {
            SeatBlock seat = (SeatBlock) state.getBlock();
            BlockState converted = ModBlocks.SECURABLE_SEAT.get().defaultBlockState()
                    .setValue(SecurableSeatBlock.COLOR, seat.getColor())
                    .setValue(SecurableSeatBlock.SEAT_TYPE, SeatType.WOOL)
                    // Create seats have no facing; orient the new backrest away from the player.
                    .setValue(SecurableSeatBlock.FACING, context.getHorizontalDirection().getOpposite())
                    .setValue(BlockStateProperties.WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED));
            if (!level.setBlockAndUpdate(pos, converted))
                return InteractionResult.FAIL;
        }

        if (level.getBlockEntity(pos) instanceof SecurableSeatBlockEntity blockEntity) {
            ItemStack previousRestrictor = blockEntity.getRestrictorItem();
            blockEntity.updateRestrictor(restrictorTypeSupplier.get());
            Player player = context.getPlayer();
            if (player != null && !player.isCreative()) {
                context.getItemInHand().shrink(1);
            }
            if (player != null && !player.isCreative() && !previousRestrictor.isEmpty()) {
                player.getInventory().placeItemBackInInventory(previousRestrictor);
            }
        }

        return InteractionResult.SUCCESS;
    }
}
