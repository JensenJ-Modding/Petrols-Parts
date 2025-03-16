package com.petrolpark.petrolsparts.content.planetary_gearset;

import java.util.EnumMap;

import org.joml.Vector3f;

import com.petrolpark.petrolsparts.PetrolsPartsPartials;
import com.petrolpark.util.KineticsHelper;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntityRenderer;

import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;

//public class PlanetaryGearsetInstance extends KineticBlockEntityInstance<PlanetaryGearsetBlockEntity> {
//
//    protected final RotatingData ringGear;
//    protected final RotatingData sunGear;
//    protected final EnumMap<Direction, RotatingData> keys;
//
//    public PlanetaryGearsetInstance(MaterialManager materialManager, PlanetaryGearsetBlockEntity blockEntity) {
//        super(materialManager, blockEntity);
//        BlockState blockState = blockEntity.getBlockState();
//        Axis axis = KineticBlockEntityRenderer.getRotationAxisOf(blockEntity);
//        int blockLight = world.getBrightness(LightLayer.BLOCK, pos);
//        int skyLight = world.getBrightness(LightLayer.SKY, pos);
//
//        ringGear = getRotatingMaterial().getModel(PetrolsPartsPartials.PG_RING_GEAR, blockState, Direction.get(AxisDirection.POSITIVE, axis), () -> KineticsHelper.rotateToAxis(axis))
//            .createInstance();
//        ringGear
//            .setRotationAxis(axis)
//            .setRotationOffset(getRotationOffset(axis)).setColor(blockEntity)
//            .setRotationalSpeed(getBlockEntitySpeed())
//            .setPosition(getInstancePosition())
//			.setBlockLight(blockLight)
//			.setSkyLight(skyLight);
//
//        sunGear = getRotatingMaterial().getModel(PetrolsPartsPartials.PG_SUN_GEAR, blockState, Direction.get(AxisDirection.POSITIVE, axis), () -> KineticsHelper.rotateToAxis(axis))
//            .createInstance();
//        sunGear
//            .setRotationAxis(axis)
//            .setRotationOffset(BracketedKineticBlockEntityRenderer.getShaftAngleOffset(axis, pos)).setColor(blockEntity)
//            .setRotationalSpeed(-2 * getBlockEntitySpeed())
//            .setPosition(getInstancePosition())
//			.setBlockLight(blockLight)
//			.setSkyLight(skyLight);
//
//        keys = new EnumMap<>(Direction.class);
//
//        for (Direction direction : Direction.values()) {
//            if (direction.getAxis() == axis) continue;
//
//            Instancer<RotatingData> planetGear = getRotatingMaterial().getModel(PetrolsPartsPartials.PG_PLANET_GEAR, blockState, Direction.get(AxisDirection.POSITIVE, axis), () -> KineticsHelper.rotateToAxis(axis));
//
//			RotatingData key = planetGear.createInstance();
//
//            Vector3f position = new Vector3f(getInstancePosition().getX(), getInstancePosition().getY(), getInstancePosition().getZ());
//            position.add(direction.step().mul(6.25f / 16f));
//
//			key
//                .setRotationAxis(axis)
//                .setRotationalSpeed(2 * getBlockEntitySpeed())
//                .setRotationOffset(BracketedKineticBlockEntityRenderer.getShaftAngleOffset(axis, pos)).setColor(blockEntity)
//                .setPosition(position)
//                .setBlockLight(blockLight)
//                .setSkyLight(skyLight);
//
//            keys.put(direction, key);
//        };
//    };
//
//    @Override
//    public void update() {
//        Axis axis = KineticBlockEntityRenderer.getRotationAxisOf(blockEntity);
//        updateRotation(ringGear, axis, getBlockEntitySpeed());
//        updateRotation(sunGear, axis, -2 * getBlockEntitySpeed());
//        sunGear.setRotationOffset(BracketedKineticBlockEntityRenderer.getShaftAngleOffset(axis, pos));
//        keys.values().forEach(gear -> {
//            updateRotation(gear, axis, 2 * getBlockEntitySpeed());
//            gear.setRotationOffset(BracketedKineticBlockEntityRenderer.getShaftAngleOffset(axis, pos));
//        });
//    };
//
//    @Override
//    public void updateLight() {
//        relight(pos, ringGear, sunGear);
//        relight(pos, keys.values().stream());
//    };
//
//    @Override
//    protected void remove() {
//        ringGear.delete();
//        sunGear.delete();
//        keys.values().forEach(InstanceData::delete);
//        keys.clear();
//    };
//
//};
