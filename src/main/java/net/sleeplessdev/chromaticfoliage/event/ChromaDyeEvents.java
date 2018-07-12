package net.sleeplessdev.chromaticfoliage.event;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.sleeplessdev.chromaticfoliage.ChromaticFoliage;
import net.sleeplessdev.chromaticfoliage.block.entity.ChromaBlockEntity;
import net.sleeplessdev.chromaticfoliage.config.ChromaGeneralConfig;
import net.sleeplessdev.chromaticfoliage.data.ChromaBlocks;
import net.sleeplessdev.chromaticfoliage.data.ChromaColor;

import java.util.Map.Entry;

@EventBusSubscriber(modid = ChromaticFoliage.ID)
public final class ChromaDyeEvents {
    private ChromaDyeEvents() {}

    @SubscribeEvent
    static void onBlockRightClicked(PlayerInteractEvent.RightClickBlock event) {
        if (!ChromaGeneralConfig.inWorldInteraction) return;

        final EntityPlayer player = event.getEntityPlayer();
        final World world = event.getWorld();
        final BlockPos pos = event.getPos();
        final IBlockState state = world.getBlockState(pos);

        if (state.getBlock() == Blocks.GRASS) {
            if (world.isRemote && !player.isSneaking()) {
                player.swingArm(event.getHand());
                return;
            }

            ChromaColor.from(event.getItemStack()).ifPresent(color -> {
                final IBlockState grass = ChromaBlocks.CHROMATIC_GRASS.getDefaultState();
                if (world.setBlockState(pos, grass.withProperty(ChromaColor.PROPERTY, color), 3)) {
                    world.playSound(null, pos, SoundEvents.BLOCK_SAND_PLACE, SoundCategory.BLOCKS, 1.0F, 0.8F);
                    if (!player.isCreative()) event.getItemStack().shrink(1);
                }
            });
        }

        if (state.getBlock() == Blocks.LEAVES || state.getBlock() == Blocks.LEAVES2) {
            if (world.isRemote && !player.isSneaking()) {
                player.swingArm(event.getHand());
                return;
            }

            ChromaColor.from(event.getItemStack()).ifPresent(color -> {
                final BlockLeaves block = (BlockLeaves) state.getBlock();
                final IBlockState leaves = getLeavesFor(block.getWoodType(block.getMetaFromState(state)));
                if (world.setBlockState(pos, leaves.withProperty(ChromaColor.PROPERTY, color), 3)) {
                    world.playSound(null, pos, SoundEvents.BLOCK_SAND_PLACE, SoundCategory.BLOCKS, 1.0F, 0.8F);
                    if (!player.isCreative()) event.getItemStack().shrink(1);
                }
            });
        }

        if (state.getBlock() == Blocks.VINE) {
            if (world.isRemote && !player.isSneaking()) {
                player.swingArm(event.getHand());
                return;
            }

            ChromaColor.from(event.getItemStack()).ifPresent(color -> {
                final IBlockState actualState = state.getActualState(world, pos);
                IBlockState chroma = ChromaBlocks.CHROMATIC_VINE.getDefaultState();
                for (final Entry<IProperty<?>, Comparable<?>> entry : actualState.getProperties().entrySet()) {
                    //noinspection unchecked,RedundantCast
                    chroma = chroma.withProperty((IProperty) entry.getKey(), (Comparable) entry.getValue());
                }
                if (world.setBlockState(pos, chroma.withProperty(ChromaColor.PROPERTY, color), 3)) {
                    world.setTileEntity(pos, new ChromaBlockEntity().withColor(color));
                    world.playSound(null, pos, SoundEvents.BLOCK_SAND_PLACE, SoundCategory.BLOCKS, 1.0F, 0.8F);
                    if (!player.isCreative()) event.getItemStack().shrink(1);
                }
            });
        }
    }

    private static IBlockState getLeavesFor(EnumType type) {
        switch (type) {
            case OAK: return ChromaBlocks.CHROMATIC_OAK_LEAVES.getDefaultState();
            case SPRUCE: return ChromaBlocks.CHROMATIC_SPRUCE_LEAVES.getDefaultState();
            case BIRCH: return ChromaBlocks.CHROMATIC_BIRCH_LEAVES.getDefaultState();
            case JUNGLE: return ChromaBlocks.CHROMATIC_JUNGLE_LEAVES.getDefaultState();
            case ACACIA: return ChromaBlocks.CHROMATIC_ACACIA_LEAVES.getDefaultState();
            case DARK_OAK: return ChromaBlocks.CHROMATIC_DARK_OAK_LEAVES.getDefaultState();
        }
        throw new IllegalStateException("Unable to determine leaves for type \"" + type.getName() + "\"");
    }
}
