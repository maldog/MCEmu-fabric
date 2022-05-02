package deltatwoforce.mcemu.minecraft;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import static deltatwoforce.mcemu.MCEmuMod.logger;


/**
 * DynamicNativeImage.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-04-26 nsano initial version <br>
 */
public class DynamicNativeImage {
    private final int w;
    private final int h;

    private final ColorModel colorModel;
    private final WritableRaster raster;

    private final NativeImage ni;
    private final NativeImageBackedTexture texture;

    private final Identifier identifier;

    public DynamicNativeImage(BufferedImage image) {
        w = image.getWidth();
        h = image.getHeight();
        colorModel = image.getColorModel();
        raster = image.getRaster();
        ni = new NativeImage(NativeImage.Format.RGBA, w, h, true);
        texture = new NativeImageBackedTexture(ni);
        MinecraftClient client = MinecraftClient.getInstance();
        identifier = client.getTextureManager().registerDynamicTexture("offscreen", texture);
        logger.info("DynamicNativeImage::<init>: " + identifier);
    }

    public int getWidth() {
        return w;
    }

    public int getHeight() {
        return h;
    }

    public Identifier update() {
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Object elements = raster.getDataElements(x, y, (Object) null);

                int alpha = colorModel.getAlpha(elements);
                int red = colorModel.getRed(elements);
                int green = colorModel.getGreen(elements);
                int blue = colorModel.getBlue(elements);
                int out = (alpha << 24) | (blue << 16) | (green << 8) | red;

                ni.setColor(x, y, out);
            }
        }

        this.texture.upload();
        return identifier;
    }
}
