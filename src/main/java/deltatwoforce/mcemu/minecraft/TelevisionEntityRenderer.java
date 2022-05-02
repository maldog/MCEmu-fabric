package deltatwoforce.mcemu.minecraft;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;

import static deltatwoforce.mcemu.MCEmuClientMod.model;


public class TelevisionEntityRenderer implements BlockEntityRenderer<TelevisionEntity> {

    private final DynamicNativeImage dni;

    public TelevisionEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        dni = new DynamicNativeImage(model.getImage());
    }

    @Override
    public void render(TelevisionEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

        // TODO screen rendering: not works
        matrices.push();
        matrices.translate(0.5, 0.5, -(0.0625 * -9));
        matrices.scale(0.5f - (0.0625f), 0.5f - (0.0625f), 1);
        matrices.multiply(new Quaternion(180f, 1f, 0f, 0f));
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getText(dni.update()));
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        int sw = 1, sh = 1;
        vertexConsumer.vertex(matrix4f,  sw,  sh, 0.505f).color(255, 255, 255, 255).texture(0.f, 1.f).light(light).next();
        vertexConsumer.vertex(matrix4f, -sw,  sh, 0.505f).color(255, 255, 255, 255).texture(1.f, 1.f).light(light).next();
        vertexConsumer.vertex(matrix4f, -sw, -sh, 0.505f).color(255, 255, 255, 255).texture(1.f, 0.f).light(light).next();
        vertexConsumer.vertex(matrix4f,  sw, -sh, 0.505f).color(255, 255, 255, 255).texture(0.f, 0.f).light(light).next();
        matrices.pop();
	}
}
