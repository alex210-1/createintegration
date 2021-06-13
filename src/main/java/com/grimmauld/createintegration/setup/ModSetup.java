package com.grimmauld.createintegration.setup;

import com.grimmauld.createintegration.blocks.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModSetup {
    public static ItemGroup itemGroup = new ItemGroup("createintegration") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModBlocks.CHUNK_LOADER);
        }
    };

    public void init() {

    }
}
