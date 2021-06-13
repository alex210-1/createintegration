package com.grimmauld.createintegration.misc;

import net.minecraft.util.math.BlockPos;

public interface IChunkLoaderList {
    void removeblock(BlockPos pos);
    void addblock(BlockPos pos);

    void add(BlockPos pos);
    void remove(BlockPos pos);

    void addchunk(Vector2i chunk);
    void removechunk(Vector2i chunk);

    void start();
}
