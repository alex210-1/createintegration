package com.grimmauld.createintegration;


import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.nio.file.Path;

@Mod.EventBusSubscriber
public class Config {

    public static final String CATEGORY_GENERAL = "general";

    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    public static ForgeConfigSpec.BooleanValue CHUNK_CHAT;
    public static ForgeConfigSpec.BooleanValue PART_SNAPPING;

    static {

        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

        COMMON_BUILDER.comment("General settings").push(CATEGORY_GENERAL);
        setupGeneralConfig(COMMON_BUILDER);
        COMMON_BUILDER.pop();


        COMMON_CONFIG = COMMON_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    private static void setupGeneralConfig(ForgeConfigSpec.Builder COMMON_BUILDER) {
        CHUNK_CHAT = COMMON_BUILDER.comment("Do chat output of chunk loaders?").define("feedback", false);
        PART_SNAPPING = COMMON_BUILDER.comment("Kinetic blocks should snap to connect to networks").define("part_snapping", true);
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
        configData.load();
        spec.setConfig(configData);
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public static void onLoad(final ModConfig.Loading configEvent) {

    }
}