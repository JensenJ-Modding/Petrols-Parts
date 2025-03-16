package com.petrolpark.petrolsparts.content.planetary_gearset;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.petrolpark.petrolsparts.PetrolsPartsPartials;
import com.petrolpark.util.KineticsHelper;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntityRenderer;

import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

public class PlanetaryGearsetRenderer extends KineticBlockEntityRenderer<PlanetaryGearsetBlockEntity> {

    public PlanetaryGearsetRenderer(Context context) {
        super(context);
    };

    @Override
    protected void renderSafe(PlanetaryGearsetBlockEntity planetaryGearsetBlockEntity, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if(VisualizationManager.supportsVisualization(planetaryGearsetBlockEntity.getLevel()))
            return;

		BlockState state = getRenderedBlockState(planetaryGearsetBlockEntity);
        Axis axis = state.getValue(RotatedPillarKineticBlock.AXIS);
		VertexConsumer vbSolid = buffer.getBuffer(RenderType.solid());

        float time = AnimationTickHolder.getRenderTime(planetaryGearsetBlockEntity.getLevel());
		float offset1 = Mth.PI * getRotationOffsetForPosition(planetaryGearsetBlockEntity, planetaryGearsetBlockEntity.getBlockPos(), axis) / 180f;
        float offset2 = Mth.PI * BracketedKineticBlockEntityRenderer.getShaftAngleOffset(axis, planetaryGearsetBlockEntity.getBlockPos()) / 180f;
		float angle = ((time * planetaryGearsetBlockEntity.getSpeed() * 3f / 10 + offset1) % 360) / 180 * Mth.PI;

        SuperByteBuffer ringGear = CachedBuffers.partialDirectional(PetrolsPartsPartials.PG_RING_GEAR, state, Direction.get(AxisDirection.POSITIVE, axis), () -> KineticsHelper.rotateToAxis(axis));
        kineticRotationTransform(ringGear, planetaryGearsetBlockEntity, axis, angle + offset1, light);
        ringGear.renderInto(ms, vbSolid);
        
        SuperByteBuffer sunGear = CachedBuffers.partialDirectional(PetrolsPartsPartials.PG_SUN_GEAR, state, Direction.get(AxisDirection.POSITIVE, axis), () -> KineticsHelper.rotateToAxis(axis));
        kineticRotationTransform(sunGear, planetaryGearsetBlockEntity, axis, (-2 * angle) + offset2, light);
        sunGear.renderInto(ms, vbSolid);
        
        for (Direction direction : Direction.values()) {
            if (direction.getAxis() == axis) continue;
            SuperByteBuffer planetGear = CachedBuffers.partialDirectional(PetrolsPartsPartials.PG_PLANET_GEAR, state, Direction.get(AxisDirection.POSITIVE, axis), () -> KineticsHelper.rotateToAxis(axis));
            planetGear.translate(direction.step().mul(6.25f / 16f));
            kineticRotationTransform(planetGear, planetaryGearsetBlockEntity, axis, (2 * angle) + offset2, light);
            planetGear.renderInto(ms, vbSolid);
        };
    };
    
};
