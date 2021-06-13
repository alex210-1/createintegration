package com.grimmauld.createintegration.blocks;

import com.grimmauld.createintegration.CreateIntegration;
import com.grimmauld.createintegration.misc.ChunkLoaderMovementBehaviour;
import com.simibubi.create.content.contraptions.base.KineticBlock;
import com.simibubi.create.content.contraptions.components.structureMovement.MovementBehaviour;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class ChunkLoader extends KineticBlock {
    public static MovementBehaviour MOVEMENT = new ChunkLoaderMovementBehaviour();


    public ChunkLoader() {
        super(Properties.from(Blocks.BEACON));
        setRegistryName("chunk_loader");
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        // TODO not sure if correct
        if (worldIn.isRemote()) return;
        worldIn.getCapability(CreateIntegration.CHUNK_LOADING_CAPABILITY, null).ifPresent(cap -> cap.addblock(pos));
    }

    /* @Override
    public void onReplaced(@Nonnull BlockState state, World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        if (world.isRemote) return;
        if (!isMoving) {
            world.getCapability(CreateIntegration.CHUNK_LOADING_CAPABILITY, null).ifPresent(cap -> cap.chunk(pos));
        }
    } */

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ChunkLoaderTile();
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState blockState) {
        return null;
    }

    @Override
    public SpeedLevel getMinimumRequiredSpeedLevel() {
        return null;
    }

    @Override
    public boolean hideStressImpact() {
        return false;
    }

    @Override
    public boolean showCapacityWithAnnotation() {
        return false;
    }
}
