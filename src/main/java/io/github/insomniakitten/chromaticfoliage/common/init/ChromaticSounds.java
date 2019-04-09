package io.github.insomniakitten.chromaticfoliage.common.init;

import io.github.insomniakitten.chromaticfoliage.common.ChromaticFoliage;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

import static io.github.insomniakitten.chromaticfoliage.common.base.ObjectHolders.checkPresent;

@ObjectHolder(ChromaticFoliage.MOD_ID)
@EventBusSubscriber(modid = ChromaticFoliage.MOD_ID)
public final class ChromaticSounds {
  static final String BLOCK_DYED = "block.dyed";
  static final String BLOCK_ILLUMINATED = "block.illuminated";
  static final String BLOCK_DARKENED = "block.darkened";

  private static final Logger LOGGER = ChromaticFoliage.getLogger("Sounds");

  @Nullable
  @ObjectHolder(BLOCK_DYED)
  private static SoundEvent blockDyed;

  @Nullable
  @ObjectHolder(BLOCK_ILLUMINATED)
  private static SoundEvent blockIlluminated;

  @Nullable
  @ObjectHolder(BLOCK_DARKENED)
  private static SoundEvent blockDarkened;

  private ChromaticSounds() {
    throw new UnsupportedOperationException();
  }

  public static SoundEvent blockDyed() {
    return checkPresent(blockDyed, BLOCK_DYED);
  }

  public static SoundEvent blockIlluminated() {
    return checkPresent(blockIlluminated, BLOCK_ILLUMINATED);
  }

  public static SoundEvent blockDarkened() {
    return checkPresent(blockDarkened, BLOCK_DARKENED);
  }

  @SubscribeEvent
  static void registerAll(final RegistryEvent.Register<SoundEvent> event) {
    final ResourceLocation name = event.getName();
    LOGGER.debug("Beginning registration to {}", name);
    registerAll(event.getRegistry());
    LOGGER.debug("Completed registration to {}", name);
  }

  private static void registerAll(final IForgeRegistry<SoundEvent> registry) {
    register(registry, BLOCK_DYED);
    register(registry, BLOCK_ILLUMINATED);
    register(registry, BLOCK_DARKENED);
  }

  private static void register(final IForgeRegistry<SoundEvent> registry, final String name) {
    final ResourceLocation id = ChromaticFoliage.namespace(name);
    final SoundEvent sound = new SoundEvent(id).setRegistryName(id);
    LOGGER.debug("| Registering {} as '{}'", sound, name);
    registry.register(sound);
  }
}
