package io.github.insomniakitten.chromaticfoliage.common.support.waila;

import com.google.common.base.Suppliers;
import io.github.insomniakitten.chromaticfoliage.common.ChromaticFoliage;
import io.github.insomniakitten.chromaticfoliage.common.block.ChromaticGrassBlock;
import io.github.insomniakitten.chromaticfoliage.common.block.ChromaticLeavesBlock;
import io.github.insomniakitten.chromaticfoliage.common.block.ChromaticVineBlock;
import io.github.insomniakitten.chromaticfoliage.common.block.EmissiveGrassBlock;
import io.github.insomniakitten.chromaticfoliage.common.block.EmissiveLeavesBlock;
import io.github.insomniakitten.chromaticfoliage.common.block.EmissiveVineBlock;
import io.github.insomniakitten.chromaticfoliage.common.support.waila.provider.ChromaticBlockDataProvider;
import io.github.insomniakitten.chromaticfoliage.common.support.waila.provider.EmissiveBlockDataProvider;
import io.github.insomniakitten.chromaticfoliage.common.support.waila.provider.EnlightenedBushDataProvider;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraftforge.fml.common.Loader;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

@WailaPlugin
public final class ChromaticWailaPlugin implements IWailaPlugin {
  private static final Logger LOGGER = ChromaticFoliage.getLogger("WailaPlugin");

  private static final Supplier<Class<?>> ENLIGHTENED_BUSH_BLOCK_CLASS = Suppliers.memoize(() -> {
    try {
      return Class.forName("knightminer.inspirations.building.block.BlockEnlightenedBush");
    } catch (final ClassNotFoundException e) {
      throw new IllegalStateException("BlockEnlightenedBush is missing or was moved", e);
    }
  });

  @Override
  public void register(final IWailaRegistrar registrar) {
    LOGGER.debug("Registering chromatic data provider to {}", registrar);
    final IWailaDataProvider chromaticDataProvider = new ChromaticBlockDataProvider();
    registrar.registerBodyProvider(chromaticDataProvider, ChromaticGrassBlock.class);
    registrar.registerBodyProvider(chromaticDataProvider, ChromaticLeavesBlock.class);
    registrar.registerBodyProvider(chromaticDataProvider, ChromaticVineBlock.class);

    LOGGER.debug("Registering emissive data provider to {}", registrar);
    final IWailaDataProvider emissiveDataProvider = new EmissiveBlockDataProvider();
    registrar.registerBodyProvider(emissiveDataProvider, EmissiveGrassBlock.class);
    registrar.registerBodyProvider(emissiveDataProvider, EmissiveLeavesBlock.class);
    registrar.registerBodyProvider(emissiveDataProvider, EmissiveVineBlock.class);

    if (Loader.isModLoaded("inspirations")) {
      LOGGER.debug("Registering Inspirations support provider to {}", registrar);
      registrar.registerBodyProvider(new EnlightenedBushDataProvider(), ENLIGHTENED_BUSH_BLOCK_CLASS.get());
    }
  }

  @Override
  public String toString() {
    return "ChromaticWailaPlugin";
  }
}
