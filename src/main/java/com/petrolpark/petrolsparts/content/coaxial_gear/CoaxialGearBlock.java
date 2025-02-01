package com.petrolpark.petrolsparts.content.coaxial_gear;

import com.petrolpark.compat.create.block.entity.behaviour.AbstractRememberPlacerBehaviour;
import com.petrolpark.petrolsparts.PetrolsPartsBlockEntityTypes;
import com.petrolpark.petrolsparts.PetrolsPartsBlocks;
import com.petrolpark.petrolsparts.PetrolsPartsShapes;
import com.petrolpark.petrolsparts.core.block.DirectionalRotatedPillarKineticBlock;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CoaxialGearBlock extends CogWheelBlock {

    public static final BooleanProperty HAS_SHAFT = BooleanProperty.create("has_shaft");

    public static boolean isCoaxialGear(BlockState state) {
        return state.getBlock() instanceof CoaxialGearBlock;
    };

    public static boolean isCoaxialGear(Block block) {
        return block instanceof CoaxialGearBlock;
    };

    public CoaxialGearBlock(Properties properties, boolean large) {
        super(large, properties);
        registerDefaultState(defaultBlockState().setValue(HAS_SHAFT, false));
    };

    public static CoaxialGearBlock large(Properties properties) {
        return new CoaxialGearBlock(properties, true);
    };

    public static CoaxialGearBlock small(Properties properties) {
        return new CoaxialGearBlock(properties, false);
    };

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(HAS_SHAFT);
        super.createBlockStateDefinition(builder);
    };

    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return (state.getValue(HAS_SHAFT) ? AllShapes.SMALL_GEAR : PetrolsPartsShapes.COAXIAL_GEAR).get(state.getValue(RotatedPillarKineticBlock.AXIS));
    };

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (state.getValue(HAS_SHAFT)) updatePropagationOfLongShaft(oldState, worldIn, pos);
        super.onPlace(state, worldIn, pos, oldState, isMoving);
    };

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        AbstractRememberPlacerBehaviour.setPlacedBy(worldIn, pos, placer);
    };

    @Override
	public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        if (state.getValue(HAS_SHAFT)) {
            if (tryRemoveBracket(context)) {
                return InteractionResult.SUCCESS;
            } else if (tryRemoveLongShaft(state, context.getLevel(), context.getClickedPos(), false)) {
                Player player = context.getPlayer();
                if (player != null && player.isCreative()) player.getInventory().placeItemBackInInventory(PetrolsPartsBlocks.COAXIAL_GEAR.asStack());
                return InteractionResult.SUCCESS;
            };
        };
		return super.onWrenched(state, context);
	};

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (facing.getAxis() == state.getValue(AXIS)) updatePropagationOfLongShaft(state, level, currentPos);
        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    };

    public static void updatePropagationOfLongShaft(BlockState state, LevelReader level, BlockPos pos) {
        if (isCoaxialGear(state) && state.getValue(HAS_SHAFT) && !level.isClientSide()) {
            Axis axis = state.getValue(AXIS);
            for (AxisDirection axisDirection : AxisDirection.values()) {
                BlockPos longShaftPos = pos.relative(Direction.get(axisDirection, axis));
                BlockState longShaftState = level.getBlockState(pos.relative(Direction.get(axisDirection, axis)));
                if (PetrolsPartsBlocks.LONG_SHAFT.has(longShaftState) && longShaftState.getValue(AXIS) == axis && longShaftState.getValue(DirectionalRotatedPillarKineticBlock.POSITIVE_AXIS_DIRECTION) == (axisDirection != AxisDirection.POSITIVE)) {
                    level.getBlockEntity(longShaftPos, PetrolsPartsBlockEntityTypes.LONG_SHAFT.get()).ifPresent(be -> {
                        be.updateSpeed = true;
                    });
                    return;
                };
            };
        };
    };

    @Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state != newState && newState.isAir() && !isMoving) {
            if (tryRemoveLongShaft(state, world, pos, true)) {
                Block.popResource(world, pos, AllBlocks.SHAFT.asStack());
            };
        };
		super.onRemove(state, world, pos, newState, isMoving);
	};

    public static boolean tryMakeLongShaft(BlockState state, Block coaxialGearBlock, Level level, BlockPos pos, Player player, Direction preferredDirection) {
        Axis axis = state.getValue(AXIS);
        if (preferredDirection.getAxis() != axis) return false;
        for (Direction direction : new Direction[]{preferredDirection, preferredDirection.getOpposite()}) {
            BlockPos shaftPos = pos.relative(direction);
            BlockState shaftState = level.getBlockState(shaftPos);
            if (!ShaftBlock.isShaft(shaftState)) continue;
            if (shaftState.getValue(AXIS) != axis) continue;
            // Creation was successful
            if (!level.isClientSide()) {
                level.setBlockAndUpdate(shaftPos, PetrolsPartsBlocks.LONG_SHAFT.getDefaultState().setValue(AXIS, axis).setValue(DirectionalRotatedPillarKineticBlock.POSITIVE_AXIS_DIRECTION, direction.getAxisDirection() != AxisDirection.POSITIVE));
                level.setBlockAndUpdate(pos, coaxialGearBlock.defaultBlockState().setValue(AXIS, axis).setValue(HAS_SHAFT, true));
                AbstractRememberPlacerBehaviour.setPlacedBy(level, pos, player);
            };
            return true;
        };
        return false;
    };

    /**
     * @param removing True if the Coaxial Gear is being mined, false if it is just being wrenched
     */
    protected boolean tryRemoveLongShaft(BlockState state, Level level, BlockPos pos, boolean removing) {
        Axis thisAxis = state.getValue(AXIS);
        for (AxisDirection axisDirection : AxisDirection.values()) {
            BlockPos longShaftPos = pos.relative(Direction.get(axisDirection, thisAxis));
            BlockState longShaftState = level.getBlockState(longShaftPos);
            if (PetrolsPartsBlocks.LONG_SHAFT.has(longShaftState)) {
                if (longShaftState.getValue(AXIS) == thisAxis && (longShaftState.getValue(DirectionalRotatedPillarKineticBlock.POSITIVE_AXIS_DIRECTION) != (axisDirection == AxisDirection.POSITIVE))) {
                    if (!level.isClientSide()) {
                        if (!removing) level.setBlockAndUpdate(pos, AllBlocks.SHAFT.getDefaultState().setValue(AXIS, thisAxis));
                        level.setBlockAndUpdate(longShaftPos, AllBlocks.SHAFT.getDefaultState().setValue(AXIS, thisAxis));
                    };
                    return true;
                };
            };
        };
        return false;
    };

    @Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
		if (player.isShiftKeyDown() || !player.mayBuild()) return InteractionResult.PASS;
        ItemStack stack = player.getItemInHand(hand);
        if (AllBlocks.SHAFT.isIn(stack) && (!state.getValue(HAS_SHAFT))) {
            if (tryMakeLongShaft(state, state.getBlock(), world, pos, player, Direction.getFacingAxis(player, state.getValue(AXIS)))) {
                if (!player.isCreative() && !world.isClientSide()) stack.shrink(1);
                return InteractionResult.sidedSuccess(world.isClientSide());
            } else {
                player.displayClientMessage(Component.translatable("petrolsparts.tooltip.coaxial_gear.shaft_too_short").withStyle(ChatFormatting.RED), true);
                return InteractionResult.SUCCESS;
            }
        };
		return InteractionResult.PASS;
	};

    @Override
	public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
		return false;
	};

    @Override
    public BlockEntityType<? extends KineticBlockEntity> getBlockEntityType() {
        return PetrolsPartsBlockEntityTypes.COAXIAL_GEAR.get();
    };
};
