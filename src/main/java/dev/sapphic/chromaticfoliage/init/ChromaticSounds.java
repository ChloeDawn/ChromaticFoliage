package dev.sapphic.chromaticfoliage.init;

import dev.sapphic.chromaticfoliage.ChromaticFoliage;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(ChromaticFoliage.ID)
@EventBusSubscriber(modid = ChromaticFoliage.ID)
public final class ChromaticSounds {
  public static final SoundEvent BLOCK_DYED = make("block.dyed");
  public static final SoundEvent BLOCK_ILLUMINATED = make("block.illuminated");
  public static final SoundEvent BLOCK_DARKENED = make("block.darkened");

  private ChromaticSounds() {
  }

  @SubscribeEvent
  static void registerAll(final RegistryEvent.Register<SoundEvent> event) {
    final IForgeRegistry<SoundEvent> registry = event.getRegistry();

    register(registry, "block.dyed", BLOCK_DYED);
    register(registry, "block.illuminated", BLOCK_ILLUMINATED);
    register(registry, "block.darkened", BLOCK_DARKENED);
  }

  private static SoundEvent make(final String name) {
    return new SoundEvent(new ResourceLocation(ChromaticFoliage.ID, name));
  }

  private static void register(final IForgeRegistry<SoundEvent> registry, final String name, final SoundEvent sound) {
    registry.register(sound.setRegistryName(new ResourceLocation(ChromaticFoliage.ID, name)));
  }
}
