package net.sleeplessdev.chromaticfoliage.event;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.sleeplessdev.chromaticfoliage.ChromaticFoliage;
import net.sleeplessdev.chromaticfoliage.config.ChromaGeneralConfig;
import net.sleeplessdev.chromaticfoliage.data.ChromaBlocks;
import net.sleeplessdev.chromaticfoliage.data.ChromaColors;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = ChromaticFoliage.ID)
public final class ChromaDyeEvents {

    private ChromaDyeEvents() {}

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!ChromaGeneralConfig.inWorldInteraction) return;

        Optional<ChromaColors> color = ChromaColors.getColorFor(event.getItemStack());

        if (!color.isPresent()) return;

        EntityPlayer player = event.getEntityPlayer();
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        IBlockState state = world.getBlockState(pos);

        if (state.getBlock() == Blocks.GRASS) {
            IBlockState grass = ChromaBlocks.CHROMATIC_GRASS.getDefaultState()
                    .withProperty(ChromaColors.PROPERTY, color.get());
            if (world.setBlockState(pos, grass)) {
                world.playSound(null, pos, SoundEvents.BLOCK_SAND_PLACE, SoundCategory.BLOCKS, 1.0F, 0.8F);
                player.swingArm(event.getHand());
                if (!player.isCreative()) {
                    event.getItemStack().shrink(1);
                }
            }
        }

        if (state.getBlock() == Blocks.LEAVES || state.getBlock() == Blocks.LEAVES2) {
            BlockLeaves block = (BlockLeaves) state.getBlock();
            int meta = block.getMetaFromState(state);
            IBlockState leaves = getLeavesFor(block.getWoodType(meta));
            if (leaves != Blocks.AIR.getDefaultState()) {
                leaves = leaves.withProperty(ChromaColors.PROPERTY, color.get());
                if (world.setBlockState(pos, leaves)) {
                    world.playSound(null, pos, SoundEvents.BLOCK_SAND_PLACE, SoundCategory.BLOCKS, 1.0F, 0.8F);
                    player.swingArm(event.getHand());
                    if (!player.isCreative()) {
                        event.getItemStack().shrink(1);
                    }
                }
            }
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
        return Blocks.AIR.getDefaultState();
    }

}
