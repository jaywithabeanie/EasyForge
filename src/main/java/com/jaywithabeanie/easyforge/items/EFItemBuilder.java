package com.jaywithabeanie.easyforge.items;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public class EFItemBuilder<T extends Item> {

    private final EFItemRegistry owner;
    private final DeferredRegister.Items register;
    private final ResourceKey<Item> resourceKey;

    private UnaryOperator<Item.Properties> properties = props -> props;
    private Function<Item.Properties, T> itemFactory;

    public EFItemBuilder(EFItemRegistry owner, DeferredRegister.Items register, ResourceKey<Item> resourceKey) {
        this.owner = owner;
        this.register = register;
        this.resourceKey = resourceKey;
    }

    public EFItemBuilder(EFItemRegistry owner, DeferredRegister.Items register, ResourceKey<Item> resourceKey, Function<Item.Properties, T> itemFactory) {
        this(owner, register, resourceKey);
        this.itemFactory = itemFactory;
    }

    public ResourceKey<Item> getResourceKey() {
        return this.resourceKey;
    }

    private EFItemBuilder<T> components(DataComponentMap dataComponents) {
        dataComponents.forEach(this::addComponent);
        return this;
    }

    private <U> void addComponent(TypedDataComponent<U> dataComponent) {
        this.properties(properties ->
            properties.component(dataComponent.type(), dataComponent.value())
        );
    }

    /**
     * Registers the item
     * @return The registered item
     */
    public DeferredItem<T> register() {
        DeferredItem<T> deferredItem = this.register.registerItem(this.resourceKey.identifier().getPath(), this.itemFactory, this.properties);
        this.owner.register(deferredItem, this);
        return deferredItem;
    }

    /**
     * Sets the name of the item
     * @param name The name of the item as a {@link Component}
     * @return The {@link EFItemBuilder} object
     * @see #name(String)
     */
    public EFItemBuilder<T> name(Component name) {
        this.owner.easyForge().translations().put(String.format("item.%s.%s", this.resourceKey.identifier().getNamespace(), this.resourceKey.identifier().getPath()), name);
        return this;
    }

    /**
     * Sets the name of the item
     * @param name The name of the item
     * @return The {@link EFItemBuilder} object
     * @see #name(Component)
     */
    public EFItemBuilder<T> name(String name) {
        return this.name(Component.literal(name));
    }

    /**
     * Sets the behavior used to create the item
     * @param itemFactory The {@link Item} factory used to create the item
     * @return The {@link EFItemBuilder} object
     */
    public <U extends Item> EFItemBuilder<U> behavior(Function<Item.Properties, U> itemFactory) {
        EFItemBuilder<U> builder = new EFItemBuilder<>(this.owner, this.register, this.resourceKey);

        builder.itemFactory = itemFactory;

        return builder;
    }

    /**
     * Applies custom properties to the {@link Item}
     * @param properties A function used to configure the item's properties
     * @return The {@link EFItemBuilder} object
     */
    public EFItemBuilder<T> properties(UnaryOperator<Item.Properties> properties) {
        UnaryOperator<Item.Properties> previousProperties = this.properties;
        this.properties = props -> properties.apply(previousProperties.apply(props));
        return this;
    }

    /**
     * Copies the behavior and properties from the {@link EFItemBuilder} that the given item is based on
     * @param item The item whose properties should be copied
     * @return The {@link EFItemBuilder} object
     */
    public <U extends Item> EFItemBuilder<U> copy(DeferredItem<U> item) {
        return copy(this.owner.getItemBuilder(item));
    }

    public <U extends Item> EFItemBuilder<U> copy(U item) {
        return copy(this.owner.getItemBuilder(item));
    }

    private <U extends Item> EFItemBuilder<U> copy(EFItemBuilder<U> otherItemBuilder) {
        EFItemBuilder<U> builder = new EFItemBuilder<>(this.owner, this.register, this.resourceKey);

        builder.itemFactory = otherItemBuilder.itemFactory;
        builder.properties(otherItemBuilder.properties);

        return builder;
    }

}
