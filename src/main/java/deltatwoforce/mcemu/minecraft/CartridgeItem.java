package deltatwoforce.mcemu.minecraft;

import java.nio.file.Path;
import java.util.List;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

import net.fabricmc.loader.api.FabricLoader;


public class CartridgeItem extends Item {
	private String name;
	public Path rom;

	private final String slotId; // e.g., "01", "02", etc.

	public CartridgeItem(Settings settings, Path rom, String slotId) {
    		super(settings);
		this.rom = rom; // <- This is missing and causing the crash!
    		this.slotId = slotId;
    		String filename = rom.getFileName().toString();
    		int i = filename.lastIndexOf('.');
    		if (i > 0) {
        		filename = filename.substring(0, i);
    		}
    		this.name = filename;
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(Text.of("NES Game: " + name));
		super.appendTooltip(stack, world, tooltip, context);
	}

	@Override
	public String getTranslationKey() {
 	   return "item.mcemu.cart" + slotId;
	}

	@Override
	public ItemStack getDefaultStack() {
    		ItemStack stack = new ItemStack(this);
    		if (rom != null) {
        		stack.getOrCreateNbt().putString("rom_filename", rom.getFileName().toString());
    		}
    		return stack;
	}

	public static Path getRomPath(ItemStack stack) {
    		if (stack.getItem() instanceof CartridgeItem) {
        		String filename = stack.getOrCreateNbt().getString("rom_filename");
        		if (filename != null && !filename.isEmpty()) {
            		return FabricLoader.getInstance().getConfigDir()
                		.resolve("mcemu/roms/nes")
                		.resolve(filename);
        		}
    		}
    		return null;
	}


}
