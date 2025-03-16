package com.petrolpark.petrolsparts.content.double_cardan_shaft;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.petrolpark.petrolsparts.PetrolsPartsPartials;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;

import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

public class DoubleCardanShaftRenderer extends KineticBlockEntityRenderer<DoubleCardanShaftBlockEntity> {

    public DoubleCardanShaftRenderer(Context context) {
        super(context);
    };

    @Override
    protected void renderSafe(DoubleCardanShaftBlockEntity doubleCardanShaftBlockEntity, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        //if (Backend.canUseInstancing(doubleCardanShaftBlockEntity.getLevel())) return; //TODO fix

        float time = AnimationTickHolder.getRenderTime(doubleCardanShaftBlockEntity.getLevel());
        BlockState state = doubleCardanShaftBlockEntity.getBlockState();
        VertexConsumer vbSolid = buffer.getBuffer(RenderType.solid());

        Direction[] directions = DoubleCardanShaftBlock.getDirectionsConnectedByState(state);
        Direction shaft1Direction = directions[0];
        Direction shaft2Direction = directions[1];
        boolean facesHaveSameSign = shaft1Direction.getAxisDirection() == shaft2Direction.getAxisDirection();
        boolean secondaryPositive = state.getValue(DoubleCardanShaftBlock.AXIS_ALONG_FIRST_COORDINATE);

        Axis axis = getAxis(shaft1Direction, shaft2Direction);

        float gimbal1Angle = Mth.PI * ((time * getSpeed(doubleCardanShaftBlockEntity, shaft1Direction) * 3f / 10) % 360) / 180f;
        float gimbal2Angle = Mth.PI * ((time * getSpeed(doubleCardanShaftBlockEntity, shaft2Direction) * 3f / 10) % 360) / 180f;
        float fluctuatingAngle1 = (float)Math.atan2(Mth.sin(gimbal1Angle), Mth.cos(gimbal1Angle) * Mth.sqrt(2) / 2) + (axis == Axis.Z ? Mth.PI / 4 * (facesHaveSameSign ? 1f : -1f) : 0);
        float fluctuatingAngle2 = (float)Math.atan2(Mth.sin(gimbal2Angle), Mth.cos(gimbal2Angle) * Mth.sqrt(2) / 2);
        float fluctuatingAngle3 = (float)Math.atan2(Mth.sin(gimbal1Angle + Mth.PI / 2), Mth.cos(gimbal1Angle + Mth.PI / 2) * Mth.sqrt(2) / 2) + Mth.PI / 2;
        float gimbal1FluctuatingAngle = Mth.sin(fluctuatingAngle1 + (axis == Axis.Z ? -Mth.PI / 4 : 0) + (facesHaveSameSign ^ (axis == Axis.X && shaft1Direction.getAxis() == Axis.Z) ? Mth.PI : 0) + (axis == Axis.X ? Mth.PI / 2 : 0)) * Mth.PI / 4;
        float gimbal2FluctuatingAngle = Mth.sin(fluctuatingAngle2 + (facesHaveSameSign ^ (axis == Axis.X && shaft2Direction.getAxis() == Axis.Z) ? Mth.PI : 0) + (axis == Axis.Z && !facesHaveSameSign ? Mth.PI / 2 : 0) + (axis == Axis.X ? Mth.PI / 2 : 0)) * Mth.PI / 4;
        float offset1 = Mth.PI * getRotationOffsetForPosition(doubleCardanShaftBlockEntity, doubleCardanShaftBlockEntity.getBlockPos(), shaft1Direction.getAxis()) / 180f;
        float offset2 = Mth.PI * getRotationOffsetForPosition(doubleCardanShaftBlockEntity, doubleCardanShaftBlockEntity.getBlockPos(), shaft2Direction.getAxis()) / 180f;

        SuperByteBuffer shaft1 = CachedBuffers.partialFacing(PetrolsPartsPartials.DCS_SIDE_SHAFT, state, shaft1Direction);
        kineticRotationTransform(shaft1, doubleCardanShaftBlockEntity, shaft1Direction.getAxis(), gimbal1Angle + offset1, light);
        shaft1.renderInto(ms, vbSolid);

        SuperByteBuffer shaft2 = CachedBuffers.partialFacing(PetrolsPartsPartials.DCS_SIDE_SHAFT, state, shaft2Direction);
        kineticRotationTransform(shaft2, doubleCardanShaftBlockEntity, shaft2Direction.getAxis(), gimbal2Angle + offset2, light);
        shaft2.renderInto(ms, vbSolid);

        SuperByteBuffer grip1 = CachedBuffers.partialFacing(PetrolsPartsPartials.DCS_SIDE_GRIP, state, shaft1Direction);
        kineticRotationTransform(grip1, doubleCardanShaftBlockEntity, shaft1Direction.getAxis(), gimbal1Angle + (axis == Axis.Z ? Mth.PI / 2 : 0f), light);
        grip1.renderInto(ms, vbSolid);

        SuperByteBuffer grip2 = CachedBuffers.partialFacing(PetrolsPartsPartials.DCS_SIDE_GRIP, state, shaft2Direction);
        kineticRotationTransform(grip2, doubleCardanShaftBlockEntity, shaft2Direction.getAxis(), gimbal2Angle, light);
        grip2.renderInto(ms, vbSolid);

        CachedBuffers.partial(PetrolsPartsPartials.DCS_CENTER_SHAFT, state).light(light)
            .translate(shaft1Direction.step().mul(2.5f / 16f))
            .translate(shaft2Direction.step().mul(2.5f / 16f))
            .center()
            .rotateY(axis == Axis.Z ? 90f : 0f)
            .rotateX(axis == Axis.Z ? (facesHaveSameSign ? 45f : 135f) : 0f)
            .rotate(facesHaveSameSign ^ axis != Axis.Y ? 135f : 45f, axis)
            .uncenter()
            .center()
                //TODO: This used a different function called rotateZRadians before?
            .rotateZ((axis == Axis.X ? fluctuatingAngle3 : fluctuatingAngle1) * (axis == Axis.X || (axis == Axis.Y ^ facesHaveSameSign) ? 1f : -1f) * (axis == Axis.X ? -1f : 1f))
            .uncenter()
            .renderInto(ms, vbSolid);


        CachedBuffers.partialFacing(PetrolsPartsPartials.DCS_GIMBAL, state, shaft1Direction).light(light)
            
            .center()
            .rotate(gimbal1Angle, Direction.get(AxisDirection.POSITIVE, shaft1Direction.getAxis()))
            .center()

            .translateBack(DoubleCardanShaftInstance.gimbalTranslation(shaft1Direction))
            .rotate(gimbal1FluctuatingAngle, DoubleCardanShaftInstance.gimbalRotation(shaft1Direction, axis == Axis.Z))
            .rotateY(axis == Axis.Z && !secondaryPositive ? 90 : 0)
            .rotateX(axis == Axis.Z ? 90 : 0)
            .translate(DoubleCardanShaftInstance.gimbalTranslation(shaft1Direction))
    
            .uncenter()
            .uncenter()
            .renderInto(ms, vbSolid);

        CachedBuffers.partialFacing(PetrolsPartsPartials.DCS_GIMBAL, state, shaft2Direction).light(light)
            
            .center()
            .rotate(gimbal2Angle, Direction.get(AxisDirection.POSITIVE, shaft2Direction.getAxis()))
            .center()

            .translateBack(DoubleCardanShaftInstance.gimbalTranslation(shaft2Direction))
            .rotate(gimbal2FluctuatingAngle, DoubleCardanShaftInstance.gimbalRotation(shaft2Direction, false))
            .translate(DoubleCardanShaftInstance.gimbalTranslation(shaft2Direction))

            .uncenter()
            .uncenter()
            .renderInto(ms, vbSolid);
    };
    
    @SuppressWarnings("null")
    private float getSpeed(DoubleCardanShaftBlockEntity blockEntity, Direction face) {
        Direction sourceFacing = null;
        if (blockEntity.hasSource()) {
            BlockPos source = blockEntity.source.subtract(blockEntity.getBlockPos()); // It thinks source can be null (it can't)
            sourceFacing = Direction.getNearest(source.getX(), source.getY(), source.getZ());
        };
        float speed = blockEntity.getSpeed();
        if (speed != 0f && sourceFacing != null) {
            if (sourceFacing.getAxisDirection() == face.getAxisDirection() && face != sourceFacing) speed *= -1;
        };
        return speed;
    };

    @SuppressWarnings("null")
    protected Direction getSourceFacing(DoubleCardanShaftBlockEntity blockEntity) {
        if (blockEntity.hasSource()) {
            BlockPos source = blockEntity.source.subtract(blockEntity.getBlockPos()); // It thinks source can be null (it can't)
            return Direction.getNearest(source.getX(), source.getY(), source.getZ());
        } else {
            return null;
        }
    };

    private Axis getAxis(Direction shaft1Direction, Direction shaft2Direction) {
        List<Axis> axes = new ArrayList<>();
        axes.addAll(List.of(Axis.values()));
        axes.remove(shaft1Direction.getAxis());
        axes.remove(shaft2Direction.getAxis());
        return axes.get(0);
    };
    
};
