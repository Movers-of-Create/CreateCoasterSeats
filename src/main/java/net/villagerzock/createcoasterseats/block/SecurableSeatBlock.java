package net.villagerzock.createcoasterseats.block;

import com.simibubi.create.content.contraptions.actors.seat.SeatBlock;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.redstone.link.LinkBehaviour;
import com.simibubi.create.foundation.utility.BlockHelper;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
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
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.villagerzock.createcoasterseats.block.entity.SecurableSeatBlockEntity;
import net.villagerzock.createcoasterseats.item.RestrictorItem;
import net.villagerzock.createcoasterseats.item.SecurableSeatBlockItem;
import net.villagerzock.createcoasterseats.registry.ModBlockEntities;
import net.villagerzock.createcoasterseats.registry.ModBlocks;
import net.villagerzock.createcoasterseats.registry.ModItems;
import net.villagerzock.createcoasterseats.event.SeatMountHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class SecurableSeatBlock extends SeatBlock implements EntityBlock, ISecurableSeat, IWrenchable {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);
    public static final EnumProperty<SeatType> SEAT_TYPE = EnumProperty.create("type", SeatType.class);
    public static final EnumProperty<SeatStyle> SEAT_STYLE = EnumProperty.create("style", SeatStyle.class);

    public SecurableSeatBlock(Properties properties) {
        super(properties, DyeColor.BLACK);

        registerDefaultState(defaultBlockState()
                .setValue(POWERED, false)
                .setValue(FACING, net.minecraft.core.Direction.NORTH)
                .setValue(COLOR, DyeColor.BLACK)
                .setValue(SEAT_TYPE, SeatType.PLASTIC)
                .setValue(SEAT_STYLE, SeatStyle.BASE)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(POWERED, FACING, COLOR, SEAT_TYPE, SEAT_STYLE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        ItemStack stack = context.getItemInHand();
        DyeColor dyeColor = DyeColor.BLACK;
        SeatType seatType = SeatType.PLASTIC;
        if (stack.getItem() instanceof SecurableSeatBlockItem blockItem){
            dyeColor = blockItem.getColor();
            seatType = blockItem.getSeatType();
        }
        return state == null ? null : state.setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(COLOR, dyeColor).setValue(SEAT_TYPE, seatType);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                               Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.getItem() instanceof RestrictorItem || AllItems.WRENCH.isIn(stack)) {
            return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        }
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
        if (newColor != null) {
            if (!level.isClientSide && newColor != state.getValue(COLOR)) {
                level.setBlockAndUpdate(pos, state.setValue(COLOR, newColor));
            }
            return ItemInteractionResult.SUCCESS;
        }

        if (isSecured(state, pos, level)) {
            if (!level.isClientSide)
                SeatMountHandler.preventMountForPlayer(player);
            return ItemInteractionResult.SUCCESS;
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (!level.isClientSide && context.getPlayer() != null
                && level.getBlockEntity(pos) instanceof SecurableSeatBlockEntity blockEntity
                && blockEntity.hasRestrictor()) {
            ItemStack restrictor = blockEntity.getRestrictorItem();
            blockEntity.updateRestrictor(null);
            if (!context.getPlayer().isCreative()) {
                context.getPlayer().getInventory().placeItemBackInInventory(restrictor);
            }
            IWrenchable.playRemoveSound(level, pos);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        List<ItemStack> drops = new ArrayList<>();
        DyeColor color = state.getValue(COLOR);
        drops.add(state.getValue(SEAT_TYPE) == SeatType.WOOL
                ? new ItemStack(ModItems.WOOL_SEATS.get(color).get())
                : new ItemStack(ModItems.COLORED_BLOCKS.get(color).get()));

        if (params.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof SecurableSeatBlockEntity blockEntity) {
            ItemStack restrictor = blockEntity.getRestrictorItem();
            if (!restrictor.isEmpty()) {
                drops.add(restrictor);
            }
        }
        return drops;
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
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
        if (level.isClientSide())
            return;

        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof SecurableSeatBlockEntity securableSeatBlockEntity) {
            int redstonePower = level.getBestNeighborSignal(pos);
            securableSeatBlockEntity.setRedstonePowered(redstonePower > 0);
        }
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof SecurableSeatBlockEntity securableSeatBlockEntity && securableSeatBlockEntity.getActivationMode() != SecurableSeatBlockEntity.ActivationMode.LINK_ONLY) {
            return true;
        }
        return super.canConnectRedstone(state, level, pos, direction);
    }

    @Override
    public boolean isSecured(BlockState state, BlockPos pos, Level level) {
        return state.getValue(POWERED)
                && level.getBlockEntity(pos) instanceof SecurableSeatBlockEntity blockEntity
                && blockEntity.hasRestrictor();
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
