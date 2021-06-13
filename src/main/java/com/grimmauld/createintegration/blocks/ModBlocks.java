package com.grimmauld.createintegration.blocks;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class ModBlocks {

    @ObjectHolder("createintegration:chunk_loader")
    public static ChunkLoader CHUNK_LOADER;

    @ObjectHolder("createintegration:chunk_loader")
    public static TileEntityType<ChunkLoaderTile> CHUNK_LOADER_TILE;

    @ObjectHolder("createintegration:ender_crate")
    public static EnderCrate ENDER_CRATE;

    @ObjectHolder("createintegration:ender_crate")
    public static TileEntityType<EnderCrateTile> ENDER_CRATE_TILE;

    @ObjectHolder("createintegration:ender_crate")
    public static ContainerType<EnderContainer> ENDER_CONTAINER;
}
