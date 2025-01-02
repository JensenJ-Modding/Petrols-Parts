package com.petrolpark.petrolsparts.content.hydraulic_transmission;

import java.util.List;

import com.petrolpark.tube.TubeBehaviour;
import com.petrolpark.util.MathsHelper;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class HydraulicTransmissionBlockEntity extends KineticBlockEntity {

    public TubeBehaviour tube;

    public HydraulicTransmissionBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    };

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        tube = new TubeBehaviour(this, this::invalidateRenderBoundingBox);
        behaviours.add(tube);
    };

    @Override
    public void destroy() {
        super.destroy();
        tube.disconnect();
    };

    @Override
    protected AABB createRenderBoundingBox() {
        if (tube == null || !tube.isController() || tube.getSpline() == null) return super.createRenderBoundingBox();
        return MathsHelper.expandToInclude(tube.getSpline().getOccupiedVolume(), getBlockPos());
    };

    @Override
    public List<BlockPos> addPropagationLocations(IRotate block, BlockState state, List<BlockPos> neighbours) {
        if (tube.getOtherEndPos() != null) neighbours.add(tube.getOtherEndPos());
        return neighbours;
    };

    @Override
    public float propagateRotationTo(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs) {
        if (target.getBlockPos() == tube.getOtherEndPos()) {
            return 1f;
        };
        return 0f;
    };
    
};
