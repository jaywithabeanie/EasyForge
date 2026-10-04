package com.jaywithabeanie.easyforge.items;

import com.jaywithabeanie.easyforge.EasyForge;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.*;
import java.util.function.Function;

public class EFItemRegistry {

    private final EasyForge easyForge;

    private final DeferredRegister.Items register;

    private final Map<ResourceKey<Item>, EFItemBuilder<?>> itemBuilders = new HashMap<>();
    private static final Map<ResourceKey<Item>, VanillaItem<?>> vanillaItems = new HashMap<>();

    public EFItemRegistry(EasyForge easyForge) {
        this.easyForge = easyForge;

        this.register = DeferredRegister.createItems(this.easyForge.modId());

        vanillaItems.values().forEach(item -> this.storeItemBuilder(
            item.id(),
            this.create(item.id())
                .behavior(item.itemFactory())
                .properties(_ -> item.properties())
        ));
    }

    public EasyForge easyForge() {
        return this.easyForge;
    }

    protected <T extends Item> EFItemBuilder<T> getItemBuilder(DeferredItem<T> item) {
        return getItemBuilder(item.getKey());
    }

    protected <T extends Item> EFItemBuilder<T> getItemBuilder(T item) {
        EasyForge.LOGGER.info("EFLOG: getItemBuilder(T item) ran");
        return getItemBuilder(BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow());
    }

    // It is safe to assume that the cast here is valid, as the DeferredItem and ItemBuilder share the same value of ?
    @SuppressWarnings("unchecked")
    private <T extends Item> EFItemBuilder<T> getItemBuilder(ResourceKey<Item> resourceKey) {
        return (EFItemBuilder<T>) itemBuilders.get(resourceKey);
    }

    private <T extends Item> void storeItemBuilder(ResourceKey<Item> resourceKey, EFItemBuilder<T> itemBuilder) {
        this.itemBuilders.put(resourceKey, itemBuilder);
    }

    public void assignModEventBus(IEventBus modEventBus) {
        this.register.register(modEventBus);
    }

    public EFItemBuilder<Item> create(ResourceKey<Item> resourceKey) {
        return new EFItemBuilder<>(this, register, resourceKey, Item::new);
    }

    public EFItemBuilder<Item> create(String id) {
        ResourceKey<Item> resourceKey = ResourceKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(easyForge.modId(), id)
        );

        return create(resourceKey);
    }

    public <T extends Item> void register(DeferredItem<T> item, EFItemBuilder<T> itemBuilder) {
        this.storeItemBuilder(item.getKey(), itemBuilder);
    }

    public static <T extends Item> void registerVanilla(ResourceKey<Item> resourceKey, Function<Item.Properties, T> itemFactory, Item.Properties properties) {
        vanillaItems.put(resourceKey, new VanillaItem<>(resourceKey, itemFactory, properties));
    }

}
