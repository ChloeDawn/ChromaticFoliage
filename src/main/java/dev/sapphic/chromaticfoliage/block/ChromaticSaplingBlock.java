package dev.sapphic.chromaticfoliage.block;

import dev.sapphic.chromaticfoliage.ChromaticColor;
import dev.sapphic.chromaticfoliage.ChromaticConfig;
import dev.sapphic.chromaticfoliage.ChromaticFoliage;
import dev.sapphic.chromaticfoliage.block.entity.ChromaticBlockEntity;
import dev.sapphic.chromaticfoliage.init.ChromaticBlocks;
import dev.sapphic.chromaticfoliage.init.ChromaticSounds;
import dev.sapphic.chromaticfoliage.tree.BigChromaticBigOakGenerator;
import dev.sapphic.chromaticfoliage.tree.ChromaticAcaciaTreeGenerator;
import dev.sapphic.chromaticfoliage.tree.ChromaticBigSpruceTreeGenerator;
import dev.sapphic.chromaticfoliage.tree.ChromaticBirchTreeGenerator;
import dev.sapphic.chromaticfoliage.tree.ChromaticDarkOakTreeGenerator;
import dev.sapphic.chromaticfoliage.tree.ChromaticSpruceTreeGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.TerrainGen;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;
import java.util.Random;

public class ChromaticSaplingBlock extends BlockSapling {
  public ChromaticSaplingBlock() {
    this.setHardness(0.0F);
    this.setSoundType(SoundType.PLANT);
  }

  @Override
  @Deprecated
  public IBlockState getActualState(final IBlockState state, final IBlockAccess access, final BlockPos pos) {
    final ChromaticBlockEntity be = (ChromaticBlockEntity) Objects.requireNonNull(access.getTileEntity(pos));
    return super.getActualState(state, access, pos).withProperty(ChromaticFoliage.COLOR, be.getColor());
  }

  @Override
  public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state, final EntityPlayer player, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
    final ItemStack stack = player.getHeldItem(hand);
    if (!player.canPlayerEdit(pos, facing, stack) || stack.isEmpty()) {
      return false;
    }
    if (ChromaticConfig.General.recolorRecipes) {
      final IBlockState actualState = state.getActualState(world, pos);
      final Block block = actualState.getBlock();

      if ((stack.getItem() == Items.DYE) && (stack.getMetadata() == EnumDyeColor.WHITE.getDyeDamage())) {
        return false;
      }

      final @Nullable ChromaticColor color = ChromaticColor.of(stack);
      if ((color == null) || (color == actualState.getValue(ChromaticFoliage.COLOR))) {
        return false;
      }
      if (world.isRemote) {
        player.swingArm(hand);
        return true;
      }
      world.setBlockState(pos, actualState.withProperty(ChromaticFoliage.COLOR, color), 3);
      world.playSound(null, pos, ChromaticSounds.BLOCK_DYED, SoundCategory.BLOCKS, 1.0F, 0.8F);
      if (!player.capabilities.isCreativeMode) {
        stack.shrink(1);
      }
      return true;
    }
    return false;
  }

  @Override
  @Deprecated
  public IBlockState getStateForPlacement(final World world, final BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
    return super.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, meta, placer).withProperty(ChromaticFoliage.COLOR, ChromaticColor.of(meta >> 3));
  }

  @Override
  public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
    ((ChromaticBlockEntity) Objects.requireNonNull(world.getTileEntity(pos))).setColor(ChromaticColor.of(stack.getMetadata() >> 3));
  }

  @Override
  public boolean hasTileEntity(final IBlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(final World world, final IBlockState state) {
    return new ChromaticBlockEntity(state.getValue(ChromaticFoliage.COLOR));
  }

  @Override
  public void generateTree(final World world, final BlockPos pos, final IBlockState state, final Random rand) {
    if (!TerrainGen.saplingGrowTree(world, rand, pos)) {
      return;
    }
    final ChromaticColor color = state.getActualState(world, pos).getValue(ChromaticFoliage.COLOR);
    WorldGenerator generator;
    if (rand.nextInt(10) == 0) {
      generator = new BigChromaticBigOakGenerator(color, true);
    } else {
      final IBlockState log =
        Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, EnumType.OAK);
      final IBlockState leaves = ChromaticBlocks.CHROMATIC_OAK_LEAVES.getDefaultState()
        .withProperty(BlockLeaves.CHECK_DECAY, false).withProperty(ChromaticFoliage.COLOR, color);
      generator = new WorldGenTrees(true, 4, log, leaves, false);
    }
    int x = 0;
    int z = 0;
    boolean mega = false;

    switch (state.getValue(TYPE)) {
      case SPRUCE:
        megaCheck:
        for (x = 0; x >= -1; --x) {
          for (z = 0; z >= -1; --z) {
            if (this.isMega(world, pos, x, z, EnumType.SPRUCE)) {
              generator = new ChromaticBigSpruceTreeGenerator(color, rand.nextBoolean());
              mega = true;
              break megaCheck;
            }
          }
        }
        if (!mega) {
          x = 0;
          z = 0;
          generator = new ChromaticSpruceTreeGenerator(color);
        }

        break;
      case BIRCH:
        generator = new ChromaticBirchTreeGenerator(color);
        break;
      case JUNGLE:
        final IBlockState log =
          Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, EnumType.JUNGLE);
        final IBlockState leaves = ChromaticBlocks.CHROMATIC_JUNGLE_LEAVES.getDefaultState()
          .withProperty(BlockLeaves.CHECK_DECAY, false).withProperty(ChromaticFoliage.COLOR, color);
        megaCheck:
        for (x = 0; x >= -1; --x) {
          for (z = 0; z >= -1; --z) {
            if (this.isMega(world, pos, x, z, EnumType.JUNGLE)) {
              generator = new WorldGenMegaJungle(true, 10, 20, log, leaves);
              mega = true;
              break megaCheck;
            }
          }
        }
        if (!mega) {
          x = 0;
          z = 0;
          generator = new WorldGenTrees(true, 4 + rand.nextInt(7), log, leaves, false);
        }

        break;
      case ACACIA:
        generator = new ChromaticAcaciaTreeGenerator(color);
        break;
      case DARK_OAK:
        megaCheck:
        for (x = 0; x >= -1; --x) {
          for (z = 0; z >= -1; --z) {
            if (this.isMega(world, pos, x, z, EnumType.DARK_OAK)) {
              generator = new ChromaticDarkOakTreeGenerator(color);
              mega = true;
              break megaCheck;
            }
          }
        }
        if (!mega) {
          return;
        }
      case OAK:
    }
    final IBlockState air = Blocks.AIR.getDefaultState();
    if (mega) {
      world.setBlockState(pos.add(x, 0, z), air, 4);
      world.setBlockState(pos.add(x + 1, 0, z), air, 4);
      world.setBlockState(pos.add(x, 0, z + 1), air, 4);
      world.setBlockState(pos.add(x + 1, 0, z + 1), air, 4);
    } else {
      world.setBlockState(pos, air, 4);
    }
    if (!generator.generate(world, rand, pos.add(x, 0, z))) {
      if (mega) {
        world.setBlockState(pos.add(x, 0, z), state, 4);
        world.setBlockState(pos.add(x + 1, 0, z), state, 4);
        world.setBlockState(pos.add(x, 0, z + 1), state, 4);
        world.setBlockState(pos.add(x + 1, 0, z + 1), state, 4);
      } else {
        world.setBlockState(pos, state, 4);
      }
    }
  }

  @Override
  public int damageDropped(final IBlockState state) {
    return state.getValue(TYPE).ordinal() + (state.getValue(ChromaticFoliage.COLOR).ordinal() << 3);
  }

  @Override
  public void getSubBlocks(final CreativeTabs group, final NonNullList<ItemStack> items) {
    for (final EnumType wood : EnumType.values()) {
      for (final ChromaticColor color : ChromaticColor.COLORS) {
        items.add(new ItemStack(this, 1, wood.ordinal() + (color.ordinal() << 3)));
      }
    }
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, ChromaticFoliage.COLOR, TYPE, STAGE);
  }

  private boolean isMega(final World world, final BlockPos pos, final int x, final int z, final EnumType type) {
    return this.isTypeAt(world, pos.add(x, 0, z), type) && this.isTypeAt(world, pos.add(x + 1, 0, z), type)
      && this.isTypeAt(world, pos.add(x, 0, z + 1), type) && this.isTypeAt(world, pos.add(x + 1, 0, z + 1), type);
  }
}
