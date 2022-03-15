package net.caffeinemc.sodium.mixin.features.chunk_rendering;

import net.caffeinemc.sodium.interop.vanilla.mixin.PackedIntegerArrayExtended;
import net.caffeinemc.sodium.world.slice.cloned.palette.ClonedPalette;
import net.minecraft.util.collection.PackedIntegerArray;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PackedIntegerArray.class)
public class MixinPackedIntegerArray implements PackedIntegerArrayExtended {
    @Shadow
    @Final
    private long[] data;

    @Shadow
    @Final
    private int elementsPerLong;

    @Shadow
    @Final
    private long maxValue;

    @Shadow
    @Final
    private int elementBits;

    @Shadow
    @Final
    private int size;

    @Override
    public <T> void copyUsingPalette(T[] out, ClonedPalette<T> palette) {
        int idx = 0;

        for (long word : this.data) {
            long l = word;

            for (int j = 0; j < this.elementsPerLong; ++j) {
                out[idx] = palette.get((int) (l & this.maxValue));
                l >>= this.elementBits;

                if (++idx >= this.size) {
                    return;
                }
            }
        }
    }
}