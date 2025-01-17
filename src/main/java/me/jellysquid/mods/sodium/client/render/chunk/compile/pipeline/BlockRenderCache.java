package me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline;

import me.jellysquid.mods.sodium.client.model.light.LightPipelineProvider;
import me.jellysquid.mods.sodium.client.model.light.cache.ArrayLightDataCache;
import me.jellysquid.mods.sodium.client.model.quad.blender.BiomeColorBlender;
import me.jellysquid.mods.sodium.client.world.WorldSlice;
import me.jellysquid.mods.sodium.client.world.cloned.ChunkRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.world.World;

public class BlockRenderCache {
    private final ArrayLightDataCache lightDataCache;

    private final BlockRenderer blockRenderer;
    private final FluidRenderer fluidRenderer;

    private final BlockModels blockModels;
    private final WorldSlice worldSlice;

    public BlockRenderCache(MinecraftClient client, World world) {
        this.worldSlice = new WorldSlice(world);
        this.lightDataCache = new ArrayLightDataCache(this.worldSlice);

        LightPipelineProvider lightPipelineProvider = new LightPipelineProvider(this.lightDataCache);
        BiomeColorBlender biomeColorBlender = createBiomeColorBlender();

        this.blockRenderer = new BlockRenderer(client, lightPipelineProvider, biomeColorBlender);
        this.fluidRenderer = new FluidRenderer(lightPipelineProvider, biomeColorBlender);

        this.blockModels = client.getBakedModelManager().getBlockModels();
    }

    public BlockModels getBlockModels() {
        return this.blockModels;
    }

    public BlockRenderer getBlockRenderer() {
        return this.blockRenderer;
    }

    public FluidRenderer getFluidRenderer() {
        return this.fluidRenderer;
    }

    public void init(ChunkRenderContext context) {
        this.lightDataCache.reset(context.getOrigin());
        this.worldSlice.copyData(context);
    }

    public WorldSlice getWorldSlice() {
        return this.worldSlice;
    }

    private static BiomeColorBlender createBiomeColorBlender() {
        return new BiomeColorBlender(MinecraftClient.getInstance().options.getBiomeBlendRadius().getValue());
    }
}
