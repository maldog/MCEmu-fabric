package deltatwoforce.mcemu.minecraft;

import deltatwoforce.mcemu.MCEmuMod;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.state.property.Properties.HORIZONTAL_FACING;


public class TelevisionEntity extends BlockEntity {

    public Direction facing;

    public TelevisionEntity(BlockPos pos, BlockState state) {
        super(MCEmuMod.televisionEntityType, pos, state);
        this.facing = state.get(HORIZONTAL_FACING);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    public Direction getHorizontalFacing() {
        return facing;
    }
}
