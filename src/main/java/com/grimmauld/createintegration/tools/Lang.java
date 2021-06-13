package com.grimmauld.createintegration.tools;

import com.grimmauld.createintegration.CreateIntegration;
import net.minecraft.util.text.TranslationTextComponent;

public class Lang extends com.simibubi.create.foundation.utility.Lang {
    public static TranslationTextComponent translate(String key, Object... args) {
        return createTranslationTextComponent(key, args);
    }

    public static TranslationTextComponent createTranslationTextComponent(String key, Object... args) {
        return new TranslationTextComponent(CreateIntegration.modid + "." + key, args);
    }
}
