package com.petrolpark.petrolsparts;

import com.petrolpark.petrolsparts.ponder.PetrolsPartsPonderPlugin;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class PetrolsPartsClient {

    public static void clientInit(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> { // Work which must be done on main thread
            
        });
        PonderIndex.addPlugin(new PetrolsPartsPonderPlugin());
    };

    public static final void clientCtor(IEventBus forgeEventBus, IEventBus modEventBus) {
        PetrolsPartsPartials.init();
    };
};
