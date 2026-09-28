package com.jaywithabeanie.easyforge.items;

import com.jaywithabeanie.easyforge.EasyForge;
import net.minecraft.world.item.Item;
import net.neoforged.bus.EventBus;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.sql.Array;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class EFItemRegistry {

    private final EasyForge easyForge;

    private final DeferredRegister.Items register;

    private final Map<DeferredItem<?>, EFItemBuilder<?>> itemBuilders = new IdentityHashMap<>();

    public EFItemRegistry(EasyForge easyForge) {
        this.easyForge = easyForge;

        this.register = DeferredRegister.createItems(this.easyForge.modId());
    }

    public EasyForge easyForge() {
        return this.easyForge;
    }

    public Map<DeferredItem<?>, EFItemBuilder<?>> getItemMap() {
        return this.itemBuilders;
    }

    // It is safe to assume that the cast here is valid, as the DeferredItem and ItemBuilder share the same value of ?
    @SuppressWarnings("unchecked")
    public <T extends Item> EFItemBuilder<T> getItemBuilder(DeferredItem<T> item) {
        return (EFItemBuilder<T>) this.itemBuilders.get(item);
    }

    private <T extends Item> void storeItemBuilder(DeferredItem<T> item, EFItemBuilder<T> itemBuilder) {
        this.itemBuilders.put(item, itemBuilder);
    }

    public void assignModEventBus(IEventBus modEventBus) {
        this.register.register(this.easyForge.modEventBus());
    }

    public EFItemBuilder<Item> create(String id) {
        return new EFItemBuilder<>(this, register, id, Item::new);
    }

    public <T extends Item> void register(DeferredItem<T> item, EFItemBuilder<T> itemBuilder) {
        this.storeItemBuilder(item, itemBuilder);
    }

}
