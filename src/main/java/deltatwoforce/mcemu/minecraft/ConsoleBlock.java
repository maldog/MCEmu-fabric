package deltatwoforce.mcemu.minecraft;

import java.nio.file.Path;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import static deltatwoforce.mcemu.MCEmuClientMod.model;
import static deltatwoforce.mcemu.MCEmuMod.logger;

// LESSON: assets/NS/blockstates/*.json should be simple for new version
public class ConsoleBlock extends Block {

	public ConsoleBlock(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
logger.info("client: " + world.isClient());

		if (world.isClient()) {
			ItemStack is = player.getMainHandStack();

			if (is.getItem() instanceof CartridgeItem) {
    				java.nio.file.Path rom = CartridgeItem.getRomPath(is);
    				logger.info("item: " + rom);
    				if (rom != null) {
        				if (!model.isRunning()) {
            				model.start(rom);
            				logger.info("nes: started");
        			} else {
            				model.reset(rom);
            				logger.info("nes: restarted");
        			}
    				} else {
        			logger.warn("ROM path not found for cartridge item: " + is);
    				}
			}
		}

		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Override
	public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
		model.stop();
logger.info("nes stopped");
		super.onBroken(world, pos, state);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
		return createCuboidShape(1, 0,  3, 15,  5,  13);
	}
}
