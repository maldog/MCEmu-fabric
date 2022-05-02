package deltatwoforce.mcemu.minecraft;

import java.nio.file.Path;
import java.util.List;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;


public class CartridgeItem extends Item {
	private String name;
	public Path rom;

	public CartridgeItem(Settings settings, Path rom) {
		super(settings);
		name = rom.getFileName().toString();
		int i = name.lastIndexOf('.');
		if (i > 0) {
			name = name.replace(name.substring(i), "");
		}

		this.rom = rom;
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(Text.of("A ROM File for the NES. " + name));
		super.appendTooltip(stack, world, tooltip, context);
	}
}
