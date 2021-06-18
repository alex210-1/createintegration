package com.grimmauld.createintegration.blocks;

import com.grimmauld.createintegration.CreateIntegration;
import com.grimmauld.createintegration.tools.Lang;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import com.simibubi.create.foundation.tileEntity.behaviour.CenteredSideValueBoxTransform;
import com.simibubi.create.foundation.tileEntity.behaviour.scrollvalue.ScrollValueBehaviour;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;


public class EnderCrateTile extends SmartTileEntity implements INamedContainerProvider {
    ScrollValueBehaviour id;
    private LazyOptional<IItemHandler> handler = LazyOptional.of(this::getHandler);

    public EnderCrateTile() {
        super(ModBlocks.ENDER_CRATE_TILE);
        updateItemHandler();
    }

    public static int step(ScrollValueBehaviour.StepContext context) {
        if (context.shift)
            return 1;

        int current = context.currentValue;
        int magnitude = Math.abs(current) - (context.forward == current > 0 ? 0 : 1);
        int step = 1;

        if (magnitude >= 4)
            step *= 4;
        if (magnitude >= 32)
            step *= 4;
        if (magnitude >= 128)
            step *= 4;
        return step;
    }

    public void updateItemHandler() {
        if (world == null) return;
        world
                .getCapability(CreateIntegration.ENDER_CRATE_CAPABILITY,null)
                .ifPresent(enderList ->
                        setItemHandler(enderList.getOrCreate(id.getValue()))
                );
    }

    private void onChannelChange(Integer integer) {
        updateItemHandler();
    }

    private void setItemHandler(LazyOptional<IItemHandler> itemHandler) {
        this.handler = itemHandler;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            updateItemHandler();
            return handler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    protected void fromTag(BlockState state, CompoundNBT compound, boolean clientPacket) {
        super.fromTag(state, compound, clientPacket);

        int v = compound.getInt("ender_id");
        id.value = v;
        id.scrollableValue = v;
        id.setValue(v);
        updateItemHandler();
    }

    @Override
    public void write(CompoundNBT tag, boolean clientPacket) {
        tag.putInt("ender_id", id.getValue());
        super.write(tag, clientPacket);
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {
        CenteredSideValueBoxTransform slot =
                new CenteredSideValueBoxTransform((ender_crate, side) -> ender_crate.get(BlockStateProperties.FACING) == side);

        id = new ScrollValueBehaviour(Lang.translate("generic.ender_id"), this, slot)
            .between(0, 256)
            .withStepFunction(EnderCrateTile::step)
            .withCallback(this::onChannelChange);

        id.value = 0;
        id.scrollableValue = 0;
        behaviours.add(id);
    }

    @Nullable
    @Override
    public Container createMenu(int id, @Nonnull PlayerInventory playerInventory, @Nonnull PlayerEntity player) {
        assert world != null;
        return new EnderContainer(id, world, pos, playerInventory);
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(getType().getRegistryName() != null ? getType().getRegistryName().getPath() : "create_integration_redux:ender_crate");  // Lang File ?
    }

    public int getId() {
        return id.getValue();
    }

    private IItemHandler getHandler() {
        return new ItemStackHandler(9);
    }

    @SuppressWarnings("unused")
    @Nonnull
    @Override
    public TileEntityType<?> getType() {
        return super.getType();
    }
}
