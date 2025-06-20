package deltatwoforce.mcemu;

import deltatwoforce.mcemu.model.EmuRenderer;
import deltatwoforce.mcemu.minecraft.TelevisionEntityRenderer;
import deltatwoforce.mcemu.model.MCEmu;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;

import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;

import deltatwoforce.mcemu.minecraft.CartridgeItem;


@Environment(EnvType.CLIENT)
public class MCEmuClientMod implements ClientModInitializer {

    public static MCEmu model;

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.register(MCEmuMod.televisionEntityType, TelevisionEntityRenderer::new);

        BlockRenderLayerMap.INSTANCE.putBlock(MCEmuMod.consoleBlock, RenderLayer.getCutout());

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            for (int i = 0; i < 2; i++)
                for (int j = 0; j < 8; j++)
                    EmuRenderer.inpi.buf[i * 8 + j] = MCEmuMod.keyDef[i][j].isPressed() ? 1 : 0;
        });

        model = new MCEmu();

	for (Item item : Registries.ITEM) {
    		if (item instanceof CartridgeItem) {
        		ModelPredicateProviderRegistry.register(
            		item,
            		new Identifier("always_one"),
            		(stack, world, entity, seed) -> 1.0f
        	);
    	}
}
    }
}