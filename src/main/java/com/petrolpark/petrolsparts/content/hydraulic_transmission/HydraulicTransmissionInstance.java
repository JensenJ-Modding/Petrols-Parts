package com.petrolpark.petrolsparts.content.hydraulic_transmission;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.petrolpark.petrolsparts.PetrolsPartsPartials;
import com.petrolpark.util.KineticsHelper;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;

import net.minecraft.core.Direction;

public class HydraulicTransmissionInstance extends SingleRotatingInstance<HydraulicTransmissionBlockEntity> {
    
    public HydraulicTransmissionInstance(MaterialManager materialManager, HydraulicTransmissionBlockEntity blockEntity) {
        super(materialManager, blockEntity);
    };

    @Override
    protected Instancer<RotatingData> getModel() {
        Direction facing = blockEntity.getBlockState().getValue(HydraulicTransmissionBlock.FACING);
        return getRotatingMaterial().getModel(PetrolsPartsPartials.HYDRAULIC_TRANSMISSION_INNER, blockEntity.getBlockState(), facing, () -> KineticsHelper.rotateToFace(facing.getOpposite()));
    };
    
};
