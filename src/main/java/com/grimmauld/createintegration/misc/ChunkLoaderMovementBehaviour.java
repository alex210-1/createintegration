package com.grimmauld.createintegration.misc;

import com.grimmauld.createintegration.CreateIntegration;
import com.simibubi.create.content.contraptions.components.structureMovement.MovementBehaviour;
import com.simibubi.create.content.contraptions.components.structureMovement.MovementContext;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.Level;

//import java.util.HashMap;

public class ChunkLoaderMovementBehaviour extends MovementBehaviour {

    @Override
    public void visitNewPosition(MovementContext context, BlockPos pos) {
        if (context.world.isRemote)return;

        Vector2i chunknew = new Vector2i(pos);
        chunknew.x >>= 4; //converts position to chunk
        chunknew.y >>= 4;

        if (!(context.temporaryData instanceof Vector2i)){
            if(context.data.contains("chunknew")) {
                context.temporaryData = new Vector2i(context.data.getLong("chunknew"));
            }
            else{
                context.temporaryData=chunknew;
                CreateIntegration.logger.debug("wtf just happend, visitNewPosition wurde vor startMoving aufgerufen");
                writeExtraData(context);
                forceChunkLoadState(context,chunknew,true);
            }
        }
        Vector2i chunkOld = (Vector2i) context.temporaryData;

        if(!chunknew.equals(chunkOld)){
            forceChunkLoadState(context,chunkOld,false);
            forceChunkLoadState(context,chunknew,true);
            context.temporaryData=chunknew;
        }
    }

    @Override
    public void startMoving(MovementContext context){
        CreateIntegration.logger.debug("start moving");
        //CreateIntegration.logger.debug(context.position); probably null
        /*if(context.position!=null){
            Vector2i chunkstart= new Vector2i((int)(context.position.x),(int)(context.position.z));
            chunkstart.x>>=4;chunkstart.y>>=4;
            context.temporaryData=chunkstart;
            forceload(context,chunkstart,true);
        }*/

        //CreateIntegration.logger.debug("start");
        //chunk.put(context,null);
    }

    @Override
    public void stopMoving(MovementContext context){
        Vector2i chunkOld= (Vector2i) context.temporaryData;
        forceChunkLoadState(context, chunkOld, false);
        CreateIntegration.logger.debug("stop moving");
    }

    @Override
    public void writeExtraData(MovementContext context) {
        super.writeExtraData(context);
        if(context.temporaryData instanceof Vector2i) {
            Vector2i chunkOld = (Vector2i) context.temporaryData;
            context.data.putLong("chunknew", chunkOld.toLong());
            CreateIntegration.logger.debug("minecatr saved");
        }else{
            CreateIntegration.logger.debug("i don't want to write null");
        }
    }

    private void forceChunkLoadState(MovementContext context, Vector2i chunk, boolean loaded){
        context.world.getCapability(CreateIntegration.CHUNK_LOADING_CAPABILITY, null).ifPresent(chunkLoaderList -> {
            if(loaded) {
                chunkLoaderList.addchunk(chunk);
                CreateIntegration.logger.log(Level.INFO, "Loaded Chunk " + chunk);
            } else {
                chunkLoaderList.removechunk(chunk);
                CreateIntegration.logger.log(Level.INFO, "Unloaded Chunk " + chunk);
            }
        });
    }
}
