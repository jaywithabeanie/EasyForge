package com.jaywithabeanie.easyforge.items;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class EFItemBuilder<T extends Item> {

    private final EFItemRegistry owner;
    private final DeferredRegister.Items register;
    private final String id;

    private UnaryOperator<Item.Properties> properties = props -> props;
    private Function<Item.Properties, T> itemFactory;

    public EFItemBuilder(EFItemRegistry owner, DeferredRegister.Items register, String id) {
        this.owner = owner;
        this.register = register;
        this.id = id;
    }

    public EFItemBuilder(EFItemRegistry owner, DeferredRegister.Items register, String id, Function<Item.Properties, T> itemFactory) {
        this(owner, register, id);
        this.itemFactory = itemFactory;
    }

    public String getId() {
        return this.id;
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
        DeferredItem<T> deferredItem = this.register.registerItem(this.id, this.itemFactory, this.properties);
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
        this.owner.easyForge().translations().put(String.format("item.%s.%s", this.owner.easyForge().modId(), this.id), name);
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
        EFItemBuilder<U> builder = new EFItemBuilder<>(this.owner, this.register, this.id);

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
        EFItemBuilder<U> otherItemBuilder = this.owner.getItemBuilder(item);

        EFItemBuilder<U> builder = new EFItemBuilder<>(this.owner, this.register, this.id);

        builder.itemFactory = otherItemBuilder.itemFactory;
        builder.properties(otherItemBuilder.properties);

        return builder;
    }

    public <U extends Item> EFItemBuilder<U> copy(U item) {
        EFItemBuilder<U> builder = new EFItemBuilder<>(this.owner, this.register, this.id);

        builder.components(item.components());

        return builder;
    }

}
