package deltatwoforce.mcemu;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.List;
import java.util.stream.Collectors;

import deltatwoforce.mcemu.minecraft.CartridgeItem;
import deltatwoforce.mcemu.minecraft.ConsoleBlock;
import deltatwoforce.mcemu.minecraft.TelevisionBlock;
import deltatwoforce.mcemu.minecraft.TelevisionEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

public class MCEmuMod implements ModInitializer {

    public static final String MODID = "mcemu";

	public static Logger logger = LogManager.getLogger(MODID);

	// Custom keybinding category
	public static final String KEY_CATEGORY = "key.categories.mcemu";

	// Player 1 NES Key Bindings
	public static final KeyBinding P1NES_LEFT    = new KeyBinding("key.mcemu.p1_left",    GLFW.GLFW_KEY_UNKNOWN, KEY_CATEGORY);
	public static final KeyBinding P1NES_RIGHT   = new KeyBinding("key.mcemu.p1_right",   GLFW.GLFW_KEY_UNKNOWN, KEY_CATEGORY);
	public static final KeyBinding P1NES_UP      = new KeyBinding("key.mcemu.p1_up",      GLFW.GLFW_KEY_UNKNOWN, KEY_CATEGORY);
	public static final KeyBinding P1NES_DOWN    = new KeyBinding("key.mcemu.p1_down",    GLFW.GLFW_KEY_UNKNOWN, KEY_CATEGORY);
	public static final KeyBinding P1NES_A       = new KeyBinding("key.mcemu.p1_a",       GLFW.GLFW_KEY_UNKNOWN, KEY_CATEGORY);
	public static final KeyBinding P1NES_B       = new KeyBinding("key.mcemu.p1_b",       GLFW.GLFW_KEY_UNKNOWN, KEY_CATEGORY);
	public static final KeyBinding P1NES_START   = new KeyBinding("key.mcemu.p1_start",   GLFW.GLFW_KEY_UNKNOWN, KEY_CATEGORY);
	public static final KeyBinding P1NES_SELECT  = new KeyBinding("key.mcemu.p1_select",  GLFW.GLFW_KEY_UNKNOWN, KEY_CATEGORY);

	// Player 2 NES Key Bindings
	public static final KeyBinding P2NES_LEFT    = new KeyBinding("key.mcemu.p2_left",    GLFW.GLFW_KEY_UNKNOWN, KEY_CATEGORY);
	public static final KeyBinding P2NES_RIGHT   = new KeyBinding("key.mcemu.p2_right",   GLFW.GLFW_KEY_UNKNOWN, KEY_CATEGORY);
	public static final KeyBinding P2NES_UP      = new KeyBinding("key.mcemu.p2_up",      GLFW.GLFW_KEY_UNKNOWN, KEY_CATEGORY);
	public static final KeyBinding P2NES_DOWN    = new KeyBinding("key.mcemu.p2_down",    GLFW.GLFW_KEY_UNKNOWN, KEY_CATEGORY);
	public static final KeyBinding P2NES_A       = new KeyBinding("key.mcemu.p2_a",       GLFW.GLFW_KEY_UNKNOWN, KEY_CATEGORY);
	public static final KeyBinding P2NES_B       = new KeyBinding("key.mcemu.p2_b",       GLFW.GLFW_KEY_UNKNOWN, KEY_CATEGORY);
	public static final KeyBinding P2NES_START   = new KeyBinding("key.mcemu.p2_start",   GLFW.GLFW_KEY_UNKNOWN, KEY_CATEGORY);
	public static final KeyBinding P2NES_SELECT  = new KeyBinding("key.mcemu.p2_select",  GLFW.GLFW_KEY_UNKNOWN, KEY_CATEGORY);

	public static final KeyBinding[][] keyDef = {
			{ MCEmuMod.P1NES_A, MCEmuMod.P1NES_B, MCEmuMod.P1NES_SELECT,
					MCEmuMod.P1NES_START, MCEmuMod.P1NES_UP, MCEmuMod.P1NES_DOWN,
					MCEmuMod.P1NES_LEFT, MCEmuMod.P1NES_RIGHT, },
			{ MCEmuMod.P2NES_A, MCEmuMod.P2NES_B, MCEmuMod.P2NES_SELECT,
					MCEmuMod.P2NES_START, MCEmuMod.P2NES_UP, MCEmuMod.P2NES_DOWN,
					MCEmuMod.P2NES_LEFT, MCEmuMod.P2NES_RIGHT, } };
    
    public static final ConsoleBlock consoleBlock = new ConsoleBlock(FabricBlockSettings.of(Material.METAL).nonOpaque());
	// LESSON: w/o #nonOpaque() makes block super dark
    public static final TelevisionBlock televisionBlock = new TelevisionBlock(FabricBlockSettings.of(Material.WOOD).nonOpaque());

	public static final BlockItem consoleItem = new BlockItem(consoleBlock, new FabricItemSettings());
	public static final BlockItem televisionItem = new BlockItem(televisionBlock, new FabricItemSettings());

	public static ItemGroup tabNES;

	private static final String ENTITY_TYPE_ID = MODID + ":television_entity";

	public static BlockEntityType<TelevisionEntity> televisionEntityType =
			FabricBlockEntityTypeBuilder.create(TelevisionEntity::new, televisionBlock).build(null);

	@Override
    public void onInitialize() {

		tabNES = FabricItemGroup
    			.builder(new Identifier(MODID, "nes"))
    			.icon(() -> new ItemStack(consoleItem))
    			.displayName(Text.translatable("itemGroup.mcemu.nes"))
    			.entries((context, entries) -> {
        			entries.add(consoleItem);
        		entries.add(televisionItem);
    			})
    		.build();

		for (KeyBinding[] kbs : keyDef) {
			for (KeyBinding kb : kbs) {
				KeyBindingHelper.registerKeyBinding(kb);
			}
		}

		Registry.register(Registries.BLOCK, new Identifier(MODID, "console"), consoleBlock);
		Registry.register(Registries.ITEM, new Identifier(MODID, "console"), consoleItem);
		Registry.register(Registries.BLOCK, new Identifier(MODID, "television"), televisionBlock);
		Registry.register(Registries.ITEM, new Identifier(MODID, "television"), televisionItem);

		Registry.register(Registries.BLOCK_ENTITY_TYPE, ENTITY_TYPE_ID, televisionEntityType);

		try {
			Path nesroms = FabricLoader.getInstance().getConfigDir().resolve(MODID).resolve("roms/nes");
			Files.createDirectories(nesroms);
			logger.info("loading NES roms from " + nesroms + " ...");
			List<Path> romFiles = Files.list(nesroms)
    				.filter(Files::isRegularFile)
    				.limit(64)
    				.collect(Collectors.toList());

			for (int i = 0; i < romFiles.size(); i++) {
    				Path f = romFiles.get(i);
    				String slotId = String.format("%02d", i + 1); // "01", "02", ..., "32"
    				CartridgeItem item = new CartridgeItem(new FabricItemSettings(), f, slotId);
    				ItemGroupEvents.modifyEntriesEvent(tabNES).register(entries -> entries.add(item.getDefaultStack()));
    				Registry.register(Registries.ITEM, new Identifier(MODID, "cart" + slotId), item);
			}
		} catch (IOException e) {
			logger.error(e);
		}


	}
}
