package com.jaywithabeanie.easyforge.datagen;

import com.jaywithabeanie.easyforge.EasyForge;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class EFLanguageProvider extends LanguageProvider {

    private final EasyForge easyForge;

    public EFLanguageProvider(PackOutput output, EasyForge easyForge) {
        super(output, easyForge.modId(), "en_us");
        this.easyForge = easyForge;
    }

    @Override
    protected void addTranslations() {
        this.easyForge.translations().forEach(this::add);
    }

}
