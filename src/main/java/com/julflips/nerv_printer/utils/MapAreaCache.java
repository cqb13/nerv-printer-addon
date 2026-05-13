package com.julflips.nerv_printer.utils;

import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundForgetLevelChunkPacket;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import java.util.HashMap;
import java.util.Map;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public final class MapAreaCache {
    private static BlockPos mapCorner = null;
    private static Map<ChunkPos, ChunkAccess> cachedChunks = new HashMap<>();

    public static boolean isWithingMap(BlockPos pos) {
        BlockPos relativePos = pos.subtract(mapCorner);
        return relativePos.getX() >= 0 && relativePos.getX() < 128 && relativePos.getZ() >= 0 && relativePos.getZ() < 128;
    }

    public static boolean isMapAreaClear() {
        for (int x = 0; x < 128; x++) {
            for (int z = 0; z < 128; z++) {
                BlockState blockState = mc.level.getBlockState(mapCorner.offset(x, 0, z));
                if (!blockState.isAir() || !blockState.getFluidState().isEmpty()) return false;
            }
        }
        return true;
    }

    public static void reset(BlockPos newCorner) {
        mapCorner = new BlockPos(newCorner);
        cachedChunks.clear();
    }

    public static BlockState getCachedBlockState(BlockPos blockPos) {
        int chunkX = blockPos.getX() >> 4;
        int chunkZ = blockPos.getZ() >> 4;
        if (mc.level.getChunkSource().hasChunk(chunkX, chunkZ)) {
            return mc.level.getBlockState(blockPos);
        }
        ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);
        if (cachedChunks.containsKey(chunkPos)) {
            ChunkAccess chunk = cachedChunks.get(chunkPos);
            return chunk.getBlockState(blockPos);
        }
        ChatUtils.warning("Could not fetch Block at " + blockPos.toShortString() + ". Try loading the entire Map Area first.");
        return mc.level.getBlockState(blockPos);
    }

    @EventHandler()
    private static void onReceivePacket(PacketEvent.Receive event) {
        if (mapCorner != null && event.packet instanceof ClientboundForgetLevelChunkPacket packet) {
            BlockPos chunkCorner = packet.pos().getWorldPosition();
            if (isWithingMap(chunkCorner)) {
                cachedChunks.put(packet.pos(), mc.level.getChunk(packet.pos().getWorldPosition()));
            }
        }
    }
}
