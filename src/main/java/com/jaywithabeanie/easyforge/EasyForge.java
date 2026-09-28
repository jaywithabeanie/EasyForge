package com.jaywithabeanie.easyforge;

import com.jaywithabeanie.easyforge.datagen.EFDatagen;
import com.jaywithabeanie.easyforge.items.EFItemRegistry;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.IEventBus;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class EasyForge {

    private final String modId;
    private IEventBus modEventBus;

    private final EFItemRegistry items;
    private final Map<String, Component> translations = new HashMap<>();
    private final EFDatagen datagen;

    public EasyForge(String modId) {
        this.modId = modId;

        this.items = new EFItemRegistry(this);
        this.datagen = new EFDatagen(this);
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
