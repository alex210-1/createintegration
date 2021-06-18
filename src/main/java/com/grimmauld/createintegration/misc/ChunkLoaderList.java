package com.grimmauld.createintegration.misc;

import com.grimmauld.createintegration.Config;
import com.grimmauld.createintegration.CreateIntegration;
import com.simibubi.create.content.contraptions.components.structureMovement.MovementContext;
import net.minecraft.command.CommandSource;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
//import net.minecraft.nbt.LongArrayNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ChunkLoaderList implements IChunkLoaderList {
    @Nullable
    private final ServerWorld world;


    public HashMap<Vector2i, Integer> loadedchunks;
    public ArrayList<BlockPos> chunkloaderblocks;

    private boolean enabled = false;

    public ChunkLoaderList(@Nullable ServerWorld world) {
        this.world = world;
        loadedchunks = new HashMap<>();
        chunkloaderblocks=new ArrayList<>();//could be replaced with hashset
    }

    /** loads chunk containing the blockpos */
    private void force(BlockPos pos) {
        forceload(pos, "add");
    }
    /** unloads chunk containing the blockpos */
    private void unforce(BlockPos pos) {
        forceload(pos, "remove");
    }
    /** loads/unloads chunk containing the blockpos */
    private void forceload(BlockPos pos, String action) {
        if (this.world == null) return;

        CommandSource source = (this.world.getServer().getCommandSource().withWorld(this.world));
        if (!Config.CHUNK_CHAT.get()) {
            source = source.withFeedbackDisabled();
        }

        int ret = this.world.getServer().getCommandManager().handleCommand(source, "forceload " + action + " " + pos.getX() + " " + pos.getZ());
    }
    /** loads/unloads chunk containing the blockpos */
    private void setforceload(BlockPos pos, Boolean x) {forceload(pos,x?"add":"remove");}
    private void setforceload(Vector2i pos, Boolean x) {forceload(new BlockPos(pos.x,0,pos.y),x?"add":"remove");}


    /**addblock adds chunk containing the blockpos to loaded chunks (and to chunkloaderblocks???)
     * use to add a normal chunkloader (not in minecart)*/
    @Override
    public void addblock(BlockPos pos) {
        if(pos==null){CreateIntegration.logger.debug("pos is null");return;}
        //chunkloaderblocks.add(pos);
        Vector2i chunk=new Vector2i(pos);
        chunk.x>>=4;chunk.y>>=4;
        addchunk(chunk);
    }

    /**removeblock removes chunk containing the blockpos from loaded chunks (and from chunkloaderblocks???)
     * use to remove a normal chunkloader (not in minecart)*/
    @Override
    public void removeblock(BlockPos pos) {
        if(pos==null){CreateIntegration.logger.debug("pos is null");return;}
        //chunkloaderblocks.remove(pos);
        Vector2i chunk=new Vector2i(pos);
        chunk.x>>=4;chunk.y>>=4;
        removechunk(chunk);
    }

    /**add adds chunk containing the blockpos to loaded chunks */
    @Override
    public void add(BlockPos pos) {
        if(pos==null){CreateIntegration.logger.debug("pos is null");return;}
        Vector2i chunk=new Vector2i(pos);
        chunk.x>>=4;chunk.y>>=4;
        addchunk(chunk);
    }

    /**remove removes chunk containing the blockpos from loaded chunks */
    @Override
    public void remove(BlockPos pos) {
        if(pos==null){CreateIntegration.logger.debug("pos is null");return;}
        Vector2i chunk=new Vector2i(pos);
        chunk.x>>=4;chunk.y>>=4;
        removechunk(chunk);
    }

    /**addchunk adds chunk to loadedchunks and loads chunk if it isn't loaded*/
    @Override
    public void addchunk(Vector2i chunk){
        if(chunk==null){CreateIntegration.logger.debug("chunk is null");return;}
        if(!loadedchunks.containsKey(chunk)){//if chunk is not in loaded chunks
            loadedchunks.put(chunk,1);
            setforceload(chunk.times(16),true);
        }else{
            loadedchunks.put(chunk, loadedchunks.get(chunk)+1);}
        CreateIntegration.logger.debug(loadedchunks);
        CreateIntegration.logger.info("loaded chunk: " + chunk);
        //CreateIntegration.logger.debug(new Vector2i(chunk.toLong()));
    }

    /**removechunk removes chunk containing the blockpos from loaded chunks*/
    @Override
    public void removechunk(Vector2i chunk){
        Integer i= loadedchunks.get(chunk);
        if(i==null){CreateIntegration.logger.debug("no chunk to remove");return;}
        if(chunk==null){CreateIntegration.logger.debug("chunk is null");return;}
        if(i==1){
            loadedchunks.remove(chunk);
            setforceload(chunk.times(16),false);
        }else loadedchunks.put(chunk,i-1);
        CreateIntegration.logger.debug(loadedchunks);
    }

    public void addSilent(BlockPos pos) {
    }

    @Override
    public void start() {
        //Todo(Does nothing)
        enabled = true;
    }

    /** loads all chunks which are supposed to be loaded */
    public void reload(){
        for(Vector2i k: loadedchunks.keySet()) {
            Integer i= loadedchunks.get(k);
            if(i==null||i==0){
                loadedchunks.remove(k);
                //setforceload(k,false);
            }else setforceload(k,true);
        }
    }


    public void readFromNBT(CompoundNBT nbt){
        long[] keys=nbt.getLongArray("loadedchunksKeys");
        int[] values=nbt.getIntArray("loadedchunksValues");
        //CreateIntegration.logger.debug("readnbt");
        CreateIntegration.logger.debug(keys);
        for(int i=0;i< keys.length;i++) {
            if(values[i]>0) {//"if" probably not nessesary but saver
                loadedchunks.put(new Vector2i(keys[i]), values[i]);
                CreateIntegration.logger.debug((new Vector2i(keys[i])).toString() + " = " + values[i]);
            }
        }
    }

    public CompoundNBT writeToNBT(){
        CompoundNBT nbt=new CompoundNBT();
        long[] keys=new long[loadedchunks.size()];
        int[] values=new int[loadedchunks.size()];
        int i=0;
        CreateIntegration.logger.debug("writenbt");
        CreateIntegration.logger.debug(loadedchunks.keySet());
        for(Vector2i v:loadedchunks.keySet()){
            CreateIntegration.logger.debug(v);
            keys[i]=v.toLong();
            values[i]=loadedchunks.get(v);
            i++;
        }

        nbt.putLongArray("loadedchunksKeys",keys);
        nbt.putIntArray("loadedchunksValues",values);
        return nbt;
    }

    public static class Storage implements IStorage<IChunkLoaderList> {
        @Override
        public INBT writeNBT(Capability<IChunkLoaderList> capability, IChunkLoaderList instance, Direction side) {
            if (!(instance instanceof ChunkLoaderList)) return null;
            return ((ChunkLoaderList) instance).writeToNBT();
        }

        @Override
        public void readNBT(Capability<IChunkLoaderList> capability, IChunkLoaderList instance, Direction side, INBT nbt) {
            if (!(instance instanceof ChunkLoaderList) || !(nbt instanceof CompoundNBT)) return;
            ChunkLoaderList list = (ChunkLoaderList) instance;
            try {
                list.readFromNBT((CompoundNBT) nbt);
                CreateIntegration.logger.debug("Loaded Chunk Loader positions. sucsessfull");
            } finally {
                CreateIntegration.logger.debug("Loaded Chunk Loader positions.");
            }
        }

        //TODO(not implemented)
        /*@Override
        public INBT writeNBT(Capability<IChunkLoaderList> capability, IChunkLoaderList instance, Direction side) {
            if (!(instance instanceof ChunkLoaderList)) return null;
            return new LongArrayNBT(((ChunkLoaderList) instance).getChunkNumbers());
        }

        @Override
        public void readNBT(Capability<IChunkLoaderList> capability, IChunkLoaderList instance, Direction side, INBT nbt) {
            if (!(instance instanceof ChunkLoaderList) || !(nbt instanceof LongArrayNBT)) return;
            ChunkLoaderList list = (ChunkLoaderList) instance;
            try {
                for (long l : ((LongArrayNBT) nbt).getAsLongArray()) {
                    list.addSilent(BlockPos.fromLong(l));
                }
            } finally {
                CreateIntegration.logger.debug("Loaded Chunk Loader positions.");
            }
        }*/
    }
}
