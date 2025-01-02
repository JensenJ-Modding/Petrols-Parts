package com.petrolpark.petrolsparts.content.hydraulic_transmission;

import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.petrolpark.petrolsparts.PetrolsPartsPartials;
import com.petrolpark.tube.ITubeRenderer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

public class HydraulicTransmissionRenderer extends KineticBlockEntityRenderer<HydraulicTransmissionBlockEntity> implements ITubeRenderer<HydraulicTransmissionBlockEntity> {

    public HydraulicTransmissionRenderer(Context context) {
        super(context);
    };

    @Override
    protected void renderSafe(HydraulicTransmissionBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
        renderTube(be, ms, buffer, light);
        Axis axis = be.getBlockState().getValue(HydraulicTransmissionBlock.FACING).getAxis();
        VertexConsumer vc = buffer.getBuffer(RenderType.solid());
        SuperByteBuffer buf = CachedBufferer.partialFacingVertical(PetrolsPartsPartials.HYDRAULIC_TRANSMISSION_PISTON, be.getBlockState(), be.getBlockState().getValue(HydraulicTransmissionBlock.FACING));
        buf.pushPose()
            .translateZ(Mth.sin(getAngleForTe(be, be.getBlockPos(), axis)))
            .popPose()
            .light(light)
            .renderInto(ms, vc);
        buf.pushPose()
            .rotateY(90d)
            .translateX(Mth.cos(getAngleForTe(be, be.getBlockPos(), axis)))
            .popPose()
            .light(light)
            .renderInto(ms, vc);
    };

    @Override
    public PartialModel getTubeSegmentModel(HydraulicTransmissionBlockEntity be) {
        return PetrolsPartsPartials.HYDRAULIC_TRANSMISSION_SEGMENT;
    };

    @Override
    protected SuperByteBuffer getRotatedModel(HydraulicTransmissionBlockEntity be, BlockState state) {
        return CachedBufferer.partial(PetrolsPartsPartials.HYDRAULIC_TRANSMISSION_INNER, state);
    };

    @Override
    public boolean shouldRenderOffScreen(HydraulicTransmissionBlockEntity pBlockEntity) {
        return true;
    };
    
};
