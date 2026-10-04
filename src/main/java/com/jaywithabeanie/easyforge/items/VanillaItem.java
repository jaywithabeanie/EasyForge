package com.jaywithabeanie.easyforge.items;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public record VanillaItem<T extends Item>(
    ResourceKey<Item> id,
    Function<Item.Properties, T> itemFactory,
    Item.Properties properties
) {}
