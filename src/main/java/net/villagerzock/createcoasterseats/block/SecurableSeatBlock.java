package net.villagerzock.createcoasterseats.block;

import com.simibubi.create.content.contraptions.actors.seat.SeatBlock;
import com.simibubi.create.content.redstone.link.LinkBehaviour;
import com.simibubi.create.foundation.utility.BlockHelper;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.villagerzock.createcoasterseats.block.entity.SecurableSeatBlockEntity;
import net.villagerzock.createcoasterseats.registry.ModBlockEntities;
import net.villagerzock.createcoasterseats.registry.ModBlocks;
import net.villagerzock.createcoasterseats.event.SeatMountHandler;
import org.jetbrains.annotations.Nullable;

public class SecurableSeatBlock extends SeatBlock implements EntityBlock, ISecurableSeat, IPlayerAnimationModificator {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private final IPlayerAnimationModificator playerAnimationModificator;

    public SecurableSeatBlock(Properties properties, DyeColor color, IPlayerAnimationModificator playerAnimationModificator) {
        super(properties, color);
        this.playerAnimationModificator = playerAnimationModificator;
        registerDefaultState(defaultBlockState()
                .setValue(POWERED, false)
                .setValue(FACING, net.minecraft.core.Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(POWERED, FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state == null ? null : state.setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                               Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!player.isShiftKeyDown()
                && level.getBlockEntity(pos) instanceof SecurableSeatBlockEntity blockEntity) {
            LinkBehaviour link = blockEntity.getBehaviour(LinkBehaviour.TYPE);
            if (link != null) {
                for (boolean first : new boolean[]{false, true}) {
                    if (!link.testHit(first, hitResult.getLocation()))
                        continue;

                    if (!level.isClientSide) {
                        link.setFrequency(first, stack);
                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM,
                                SoundSource.BLOCKS, 0.25F, 0.1F);
                    }
                    return ItemInteractionResult.SUCCESS;
                }
            }
        }

        DyeColor newColor = DyeColor.getColor(stack);
        if (newColor != null && newColor != getColor()) {
            if (!level.isClientSide) {
                BlockState recolored = BlockHelper.copyProperties(
                    state,
                    ModBlocks.SECURABLE_SEATS.get(newColor).get().defaultBlockState()
                );
                level.setBlockAndUpdate(pos, recolored);
            }
            return ItemInteractionResult.SUCCESS;
        }

        if (state.getValue(POWERED)) {
            if (!level.isClientSide)
                SeatMountHandler.preventMountForPlayer(player);
            return ItemInteractionResult.SUCCESS;
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SecurableSeatBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type) {
        if (type != ModBlockEntities.SECURABLE_SEAT.get())
            return null;

        return (tickerLevel, pos, tickerState, blockEntity) ->
                ((SecurableSeatBlockEntity) blockEntity).tick();
    }

    @Override
    public boolean isSecured(BlockState state, BlockPos pos, Level level) {
        return state.getValue(POWERED);
    }

    @Override
    public void updatePlayerAnimation(AbstractClientPlayer entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float partialTicks, PlayerModelBundle playerModelBundle, BlockPos pos, Level level) {
        this.playerAnimationModificator.updatePlayerAnimation(entity,limbSwing,limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, playerModelBundle, pos, level);
    }

    public int getRotation(BlockState state) {
        Direction direction = state.getValue(FACING);
        return switch (direction){
            case WEST -> 1;
            case NORTH -> 2;
            case EAST -> 3;
            default -> 0;
        };
    }
}
