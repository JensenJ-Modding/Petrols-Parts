package com.petrolpark.petrolsparts.ponder;

import com.petrolpark.petrolsparts.PetrolsPartsBlocks;
import com.simibubi.create.infrastructure.ponder.AllCreatePonderTags;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class PetrolsPartsPonderIndex {

    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        // Coaxial Gear
        //CREATE_HELPER.forComponents(PetrolsPartsBlocks.COAXIAL_GEAR)
        //    .addStoryBoard("cog/small", KineticsScenes::cogAsRelay);
        HELPER.forComponents(PetrolsPartsBlocks.COAXIAL_GEAR)
            .addStoryBoard("coaxial_gear/shaftless", PetrolsPartsScenes::coaxialGearShaftless)
            .addStoryBoard("coaxial_gear/through", PetrolsPartsScenes::coaxialGearThrough);

        // Colossal Cogwheel
        HELPER.forComponents(PetrolsPartsBlocks.COLOSSAL_COGWHEEL)
            .addStoryBoard("colossal_cogwheel", PetrolsPartsScenes::colossalCogwheel);

        // Differential
        HELPER.forComponents(PetrolsPartsBlocks.DIFFERENTIAL)
            .addStoryBoard("differential", PetrolsPartsScenes::differential);

        // Double Cardan Shaft
        HELPER.forComponents(PetrolsPartsBlocks.DOUBLE_CARDAN_SHAFT)
            .addStoryBoard("double_cardan_shaft", PetrolsPartsScenes::doubleCardanShaft);

        // Hydraulic Transmission
        HELPER.forComponents(PetrolsPartsBlocks.HYDRAULIC_TRANSMISSION)
            .addStoryBoard("hydraulic_transmission", PetrolsPartsScenes::hydraulicTransmission);

        // Large Coaxial Cogwheel
        //CREATE_HELPER.forComponents(PetrolsPartsBlocks.LARGE_COAXIAL_GEAR)
        //    .addStoryBoard("cog/speedup", com.simibubi.create.infrastructure.ponder.scenes.KineticsScenes::cogsSpeedUp)
        //    .addStoryBoard("cog/large", com.simibubi.create.infrastructure.ponder.scenes.KineticsScenes::largeCogAsRelay, AllPonderTags.KINETIC_RELAYS);
        HELPER.forComponents(PetrolsPartsBlocks.LARGE_COAXIAL_GEAR)
            .addStoryBoard("coaxial_gear/shaftless", PetrolsPartsScenes::coaxialGearShaftless)
            .addStoryBoard("coaxial_gear/through", PetrolsPartsScenes::coaxialGearThrough);

        // Planetary Gearset
        //CREATE_HELPER.forComponents(PetrolsPartsBlocks.PLANETARY_GEARSET)
        //    .addStoryBoard("cog/speedup", KineticsScenes::cogsSpeedUp)
        //    .addStoryBoard("cog/large", KineticsScenes::largeCogAsRelay);
        HELPER.forComponents(PetrolsPartsBlocks.PLANETARY_GEARSET)
            .addStoryBoard("planetary_gearset", PetrolsPartsScenes::planetaryGearset);
    };

    public static void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<RegistryEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);
        HELPER.addToTag(AllCreatePonderTags.KINETIC_RELAYS)
            .add(PetrolsPartsBlocks.COAXIAL_GEAR)
            .add(PetrolsPartsBlocks.COLOSSAL_COGWHEEL)
            .add(PetrolsPartsBlocks.DIFFERENTIAL)
            .add(PetrolsPartsBlocks.DOUBLE_CARDAN_SHAFT)
            .add(PetrolsPartsBlocks.LARGE_COAXIAL_GEAR)
            .add(PetrolsPartsBlocks.HYDRAULIC_TRANSMISSION)
            .add(PetrolsPartsBlocks.PLANETARY_GEARSET)
        ;
    };
};
