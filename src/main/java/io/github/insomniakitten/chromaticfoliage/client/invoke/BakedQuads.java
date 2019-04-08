package io.github.insomniakitten.chromaticfoliage.client.invoke;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

public final class BakedQuads {
  private static final MethodHandle BAKEDQUAD_TINTINDEX_SET;

  static {
    final MethodHandles.Lookup lookup = MethodHandles.lookup();
    try {
      BAKEDQUAD_TINTINDEX_SET = lookup.unreflectSetter(findField(BakedQuad.class, "field_178213_b"));
    } catch (final NoSuchFieldException | IllegalAccessException e) {
      throw new IllegalStateException("Unable to unreflect field 'tintIndex' in 'BakedQuad'", e);
    }
  }

  private BakedQuads() {
    throw new UnsupportedOperationException();
  }

  public static void setTintIndex(final BakedQuad bakedQuad, final int tintIndex) {
    try {
      BAKEDQUAD_TINTINDEX_SET.invokeExact(bakedQuad, tintIndex);
    } catch (final Throwable t) {
      throw new IllegalStateException("Unable to set int 'tintIndex' in 'BakedQuad' " + bakedQuad, t);
    }
  }

  private static Field findField(final Class<?> ownerType, final String name) throws NoSuchFieldException {
    final FMLDeobfuscatingRemapper remapper = FMLDeobfuscatingRemapper.INSTANCE;
    final String owner = remapper.unmap(ownerType.getName().replace(".", "/"));
    final String field = remapper.mapFieldName(owner, "field_178213_b", null);
    final Field declaredField = BakedQuad.class.getDeclaredField(field);
    declaredField.setAccessible(true);
    return declaredField;
  }
}
