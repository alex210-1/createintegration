package com.grimmauld.createintegration.blocks;


import com.grimmauld.createintegration.CreateIntegration;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

public class ChunkLoaderTile extends TileEntity {
    public ChunkLoaderTile() {
        super(ModBlocks.CHUNK_LOADER_TILE);
        // TODO block does not get added on initial load, apparently
        super.getCapability(CreateIntegration.CHUNK_LOADING_CAPABILITY, null)
                .ifPresent(cap -> cap.addblock(pos));
    }

    @Override
    public void remove() {
        //TODO(remove tile entity???)
        super.remove();
        super.getCapability(CreateIntegration.CHUNK_LOADING_CAPABILITY, null)
                .ifPresent(cap -> cap.removeblock(pos));
    }
}
