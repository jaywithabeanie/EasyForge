package com.jaywithabeanie.easyforge.mixins;

import com.jaywithabeanie.easyforge.EasyForge;
import com.jaywithabeanie.easyforge.items.EFItemRegistry;
import net.minecraft.core.component.DataComponentInitializers;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(Items.class)
public class ItemsMixin {

    @Inject(
        method = "registerItem(Lnet/minecraft/resources/ResourceKey;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;",
        at = @At("RETURN")
    )
    private static void easyForge$registerItem(
        ResourceKey<Item> id,
        Function<Item.Properties, Item> itemFactory,
        Item.Properties properties,
        CallbackInfoReturnable<Item> cir
    ) {
        EFItemRegistry.registerVanilla(id, itemFactory, properties);
    }

}
