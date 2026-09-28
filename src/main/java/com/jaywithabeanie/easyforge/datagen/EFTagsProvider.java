package com.jaywithabeanie.easyforge.datagen;

import com.jaywithabeanie.easyforge.EasyForge;
import com.jaywithabeanie.easyforge.api.annotation.EasyForgeItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class EFTagsProvider extends TagsProvider<Item> {

    private final EasyForge easyForge;

    public EFTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, EasyForge easyForge) {
        super(output, Registries.ITEM, lookupProvider, easyForge.modId());
        this.easyForge = easyForge;
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
//        tag(ItemTags.ANVIL).add
//        tag(ItemTags.ACACIA_LOGS).add(Items.TRIAL_KEY.getDescriptionId().key());
    }

}
