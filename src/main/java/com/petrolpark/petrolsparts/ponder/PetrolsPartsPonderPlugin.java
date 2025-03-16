package com.petrolpark.petrolsparts.ponder;

import com.petrolpark.petrolsparts.PetrolsParts;
import net.createmod.ponder.api.level.PonderLevel;
import net.createmod.ponder.api.registration.*;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class PetrolsPartsPonderPlugin implements PonderPlugin {

    public PetrolsPartsPonderPlugin() {
    }

    public @NotNull String getModId() {
        return PetrolsParts.MOD_ID;
    }

    public void registerScenes(@NotNull PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PetrolsPartsPonderIndex.register(helper);
    }

    public void registerTags(@NotNull PonderTagRegistrationHelper<ResourceLocation> helper) {
        PetrolsPartsPonderIndex.registerTags(helper);
    }

    public void registerSharedText(@NotNull SharedTextRegistrationHelper helper) {

    }

    public void onPonderLevelRestore(@NotNull PonderLevel ponderLevel) {

    }

    public void indexExclusions(@NotNull IndexExclusionHelper helper) {

    }
}
