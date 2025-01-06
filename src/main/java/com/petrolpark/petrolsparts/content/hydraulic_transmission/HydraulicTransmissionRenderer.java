package com.petrolpark.petrolsparts.content.hydraulic_transmission;

import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.petrolpark.petrolsparts.PetrolsPartsPartials;
import com.petrolpark.tube.ITubeRenderer;
import com.petrolpark.util.KineticsHelper;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

public class HydraulicTransmissionRenderer extends KineticBlockEntityRenderer<HydraulicTransmissionBlockEntity> implements ITubeRenderer<HydraulicTransmissionBlockEntity> {

    public HydraulicTransmissionRenderer(Context context) {
        super(context);
    };

    @Override
    protected void renderSafe(HydraulicTransmissionBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
        Direction facing = be.getBlockState().getValue(HydraulicTransmissionBlock.FACING);
        VertexConsumer vc = buffer.getBuffer(RenderType.solid());
        float time = AnimationTickHolder.getRenderTime();

        renderTube(be, ms, buffer, light);

        ms.pushPose();
        TransformStack.cast(ms)
            .centre()
            .rotateToFace(facing.getOpposite())
            .multiply(com.mojang.math.Axis.XN.rotationDegrees(-90))
            .unCentre();

        CachedBufferer.partial(PetrolsPartsPartials.HYDRAULIC_TRANSMISSION_PISTON, be.getBlockState())
            .translateZ(Mth.sin(((time * be.getSpeed() * 3f / 5) % 360) * Mth.PI / 180f) * 3 / 32d)
            .light(light)
            .renderInto(ms, vc);
        CachedBufferer.partial(PetrolsPartsPartials.HYDRAULIC_TRANSMISSION_PISTON, be.getBlockState())
            .centre()
            .rotateY(90d)
            .unCentre()
            .translateZ(Mth.cos(((time * be.getSpeed() * 3f / 5) % 360) * Mth.PI / 180f) * 3 / 32d)
            .light(light)
            .renderInto(ms, vc);
        
        ms.popPose();
    };

    @Override
    public PartialModel getTubeSegmentModel(HydraulicTransmissionBlockEntity be) {
        return PetrolsPartsPartials.HYDRAULIC_TRANSMISSION_SEGMENT;
    };

    @Override
    protected SuperByteBuffer getRotatedModel(HydraulicTransmissionBlockEntity be, BlockState state) {
        Direction face = state.getValue(HydraulicTransmissionBlock.FACING);
        return CachedBufferer.partialDirectional(PetrolsPartsPartials.HYDRAULIC_TRANSMISSION_INNER, state, face, () -> KineticsHelper.rotateToFace(face.getOpposite()));
    };

    @Override
    public boolean shouldRenderOffScreen(HydraulicTransmissionBlockEntity pBlockEntity) {
        return true;
    };
    
};
