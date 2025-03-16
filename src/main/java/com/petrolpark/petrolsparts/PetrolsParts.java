package com.petrolpark.petrolsparts;

import net.createmod.catnip.lang.FontHelper;
import net.createmod.catnip.placement.PlacementHelpers;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.petrolpark.petrolsparts.content.coaxial_gear.CoaxialGearBlockItem.GearOnShaftPlacementHelper;
import com.petrolpark.petrolsparts.content.coaxial_gear.CoaxialGearBlockItem.ShaftOnGearPlacementHelper;
import com.petrolpark.petrolsparts.core.advancement.PetrolsPartsAdvancementTrigger;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(PetrolsParts.MOD_ID)
public class PetrolsParts {

    public static final String MOD_ID = "petrolsparts";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);

    static {
		REGISTRATE.setTooltipModifierFactory(item -> {
			return new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE).andThen(TooltipModifier.mapNull(KineticStats.create(item)));
		});
	};

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    };

    static {
        // Placement Helpers which need to come before Create's
        PlacementHelpers.register(new GearOnShaftPlacementHelper());
        PlacementHelpers.register(new ShaftOnGearPlacementHelper());
    };

    public PetrolsParts() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        REGISTRATE.registerEventListeners(modEventBus);

        PetrolsPartCreativeModeTab.register(modEventBus);
        PetrolsPartsBlocks.register();
        PetrolsPartsBlockEntityTypes.register();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    
        // Register the commonSetup method for modloading
        modEventBus.addListener(PetrolsPartsClient::clientInit);
        modEventBus.addListener(this::init);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> PetrolsPartsClient.clientCtor(forgeEventBus, modEventBus));
    };

    private void init(final FMLCommonSetupEvent event) {
        event.enqueueWork(PetrolsPartsAdvancementTrigger::register);
    };

};
