package com.jaywithabeanie.easyforge.datagen;

import com.jaywithabeanie.easyforge.EasyForge;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.intellij.lang.annotations.Identifier;

public class EFLanguageProvider extends LanguageProvider {

    private final EasyForge easyForge;

    public EFLanguageProvider(PackOutput output, EasyForge easyForge) {
        super(output, easyForge.modId(), "en_us");
        this.easyForge = easyForge;
    }

    @Override
    protected void addTranslations() {
        this.easyForge.translations().forEach(this::add);
        TagKey<Item> MY_ITEMS = TagKey.create(Registries.ITEM, Registries.ITEM.identifier());
    }

}
