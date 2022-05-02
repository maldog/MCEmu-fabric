package deltatwoforce.mcemu.minecraft;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

import static deltatwoforce.mcemu.MCEmuClientMod.model;


public class TelevisionEntityRenderer implements BlockEntityRenderer<TelevisionEntity> {

    private final DynamicNativeImage dni;

    public TelevisionEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        dni = new DynamicNativeImage(model.getImage());
    }

    @Override
    public void render(TelevisionEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay) {

        matrixStack.push();
        matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0F));

        Direction direction = entity.getHorizontalFacing();
        if (direction == Direction.EAST) {
            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-90 * entity.getHorizontalFacing().getHorizontal()));
            matrixStack.translate(-1 + 0.0625, -1 + 0.0625, -1 + 0.0625);
        } else if (direction == Direction.WEST) {
            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-90 * entity.getHorizontalFacing().getHorizontal()));
            matrixStack.translate(0.0625, -1 + 0.0625, 0.0625);
        } else if (direction == Direction.SOUTH) {
            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-180));
            matrixStack.translate( 0.0625, -1 + 0.0625, -1 + 0.0625);
        } else if (direction == Direction.NORTH) {
            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180 * entity.getHorizontalFacing().getHorizontal()));
            matrixStack.translate(-1 + 0.0625, -1 + 0.0625, 0.0625);
        }

        matrixStack.scale(0.007f, 0.007f, 0.007f);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getText(dni.update()));
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        vertexConsumer.vertex(matrix4f, 0.0F, 128.0F, -0.01F).color(255, 255, 255, 255).texture(0.0F, 1.0F).light(15728640).next();
        vertexConsumer.vertex(matrix4f, 128.0F, 128.0F, -0.01F).color(255, 255, 255, 255).texture(1.0F, 1.0F).light(15728640).next();
        vertexConsumer.vertex(matrix4f, 128.0F, 0.0F, -0.01F).color(255, 255, 255, 255).texture(1.0F, 0.0F).light(15728640).next();
        vertexConsumer.vertex(matrix4f, 0.0F, 0.0F, -0.01F).color(255, 255, 255, 255).texture(0.0F, 0.0F).light(15728640).next();
        matrixStack.pop();
	}
}
