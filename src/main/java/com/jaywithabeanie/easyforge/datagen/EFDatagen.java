package com.jaywithabeanie.easyforge.datagen;

import com.jaywithabeanie.easyforge.EasyForge;
import com.jaywithabeanie.easyforge.api.annotation.EasyForgeItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class EFDatagen {

    private final EasyForge easyForge;

    public EFDatagen(EasyForge easyForge) {
        this.easyForge = easyForge;
    }

    public void gatherData(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();

        generator.addProvider(true, new EFLanguageProvider(packOutput, this.easyForge));
//        generator.addProvider(true, new EFTagsProvider(packOutput, event.getWorldLookupProvider() ,this.easyForge));
    }

}
