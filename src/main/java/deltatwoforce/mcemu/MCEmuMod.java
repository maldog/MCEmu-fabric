package deltatwoforce.mcemu;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
import net.fabricmc.fabric.impl.itemgroup.FabricItemGroupBuilderImpl;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
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

	public static final KeyBinding P1NES_LEFT = new KeyBinding("NES: P1 Left", GLFW.GLFW_KEY_LEFT, "NES");
    public static final KeyBinding P1NES_RIGHT = new KeyBinding("NES: P1 Right", GLFW.GLFW_KEY_RIGHT, "NES");
    public static final KeyBinding P1NES_UP = new KeyBinding("NES: P1 Up", GLFW.GLFW_KEY_UP, "NES");
    public static final KeyBinding P1NES_DOWN = new KeyBinding("NES: P1 Down", GLFW.GLFW_KEY_DOWN, "NES");
    public static final KeyBinding P1NES_A = new KeyBinding("NES: P1 A", GLFW.GLFW_KEY_RIGHT_SHIFT, "NES");
    public static final KeyBinding P1NES_B = new KeyBinding("NES: P1 B", GLFW.GLFW_KEY_RIGHT_CONTROL, "NES");
    public static final KeyBinding P1NES_START = new KeyBinding("NES: P1 Start", GLFW.GLFW_KEY_ENTER, "NES");
    public static final KeyBinding P1NES_SELECT = new KeyBinding("NES: P1 Select", GLFW.GLFW_KEY_BACKSPACE, "NES");
    
    public static final KeyBinding P2NES_LEFT = new KeyBinding("NES: P2 Left", GLFW.GLFW_KEY_1, "NES");
    public static final KeyBinding P2NES_RIGHT = new KeyBinding("NES: P2 Right", GLFW.GLFW_KEY_3, "NES");
    public static final KeyBinding P2NES_UP = new KeyBinding("NES: P2 Up", GLFW.GLFW_KEY_5, "NES");
    public static final KeyBinding P2NES_DOWN = new KeyBinding("NES: P2 Down", GLFW.GLFW_KEY_2, "NES");
    public static final KeyBinding P2NES_A = new KeyBinding("NES: P2 A", GLFW.GLFW_KEY_ENTER, "NES");
    public static final KeyBinding P2NES_B = new KeyBinding("NES: P2 B", GLFW.GLFW_KEY_0, "NES");
    public static final KeyBinding P2NES_START = new KeyBinding("NES: P2 Start", GLFW.GLFW_KEY_7, "NES");
    public static final KeyBinding P2NES_SELECT = new KeyBinding("NES: P2 Select", GLFW.GLFW_KEY_9, "NES");

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

	public static final ItemGroup tabNES = new FabricItemGroupBuilderImpl(new Identifier(MODID, "nes"))
			.icon(() -> new ItemStack(Items.WHITE_STAINED_GLASS))
			.entries((context, entries) -> {
				entries.add(consoleItem);
				entries.add(televisionItem);
			})
			.build();

	private static final String ENTITY_TYPE_ID = MODID + ":television_entity";

	public static BlockEntityType<TelevisionEntity> televisionEntityType =
			FabricBlockEntityTypeBuilder.create(TelevisionEntity::new, televisionBlock).build(null);

	@Override
    public void onInitialize() {
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
			Files.list(nesroms).forEach(f -> {
				CartridgeItem item = new CartridgeItem(new FabricItemSettings(), f);
				ItemGroupEvents.modifyEntriesEvent(tabNES).register(entries -> entries.add(item));
				Registry.register(Registries.ITEM, new Identifier(MODID, "cartridge"), item);
logger.info("loaded " + item.rom);
			});
		} catch (IOException e) {
			logger.error(e);
		}
	}
}
