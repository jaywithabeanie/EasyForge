package com.jaywithabeanie.easyforge;

import com.jaywithabeanie.easyforge.datagen.EFDatagen;
import com.jaywithabeanie.easyforge.internal.EFScanner;
import com.jaywithabeanie.easyforge.items.EFItemRegistry;
import com.jaywithabeanie.easyforge.items.VanillaItem;
import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.IEventBus;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EasyForge {

    private final String modId;
    private final String modPackage;
    private IEventBus modEventBus;

    private final EFItemRegistry items;
    private final Map<String, Component> translations = new HashMap<>();
    private final EFDatagen datagen;

    public static final Logger LOGGER =
        LogUtils.getLogger();

    public EasyForge(String modId, Class<?> modClass) {
        this.modId = modId;
        this.modPackage = modClass.getPackageName();

        this.items = new EFItemRegistry(this);
        this.datagen = new EFDatagen(this);

        EasyForge.LOGGER.info("EFLOG: EasyForge constructor loaded");
    }

    public String modId() {
        return this.modId;
    }

    public IEventBus modEventBus() {
        return this.modEventBus;
    }

    public void setModEventBus(IEventBus modEventBus) {
        this.modEventBus = modEventBus;

        this.items().assignModEventBus(modEventBus);

        this.registerEvents(this.modEventBus);

        EFScanner.scan(modPackage);
    }

    public EFItemRegistry items() {
        return this.items;
    }

    public Map<String, Component> translations() {
        return this.translations;
    }

    private void registerEvents(IEventBus modEventBus) {
        modEventBus.addListener(datagen::gatherData);
    }

}
