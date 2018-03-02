package WayofTime.bloodmagic.ritual;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ritual.data.*;
import WayofTime.bloodmagic.tile.TileSpectralBlock;
import WayofTime.bloodmagic.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class RitualSuppression extends Ritual {
    public static final String SUPPRESSION_RANGE = "suppressionRange";

    public RitualSuppression() {
        super("ritualSuppression", 0, 10000, "ritual." + BloodMagic.MODID + ".suppressionRitual");
        addBlockRange(SUPPRESSION_RANGE, new AreaDescriptor.HemiSphere(new BlockPos(0, 0, 0), 10));
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone) {
        World world = masterRitualStone.getWorldObj();
        int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

        if (currentEssence < getRefreshCost()) {
            masterRitualStone.getOwnerNetwork().causeNausea();
            return;
        }

        final int refresh = 100;
        AreaDescriptor suppressionRange = getBlockRange(SUPPRESSION_RANGE);

        for (BlockPos blockPos : suppressionRange.getContainedPositions(masterRitualStone.getBlockPos())) {
            IBlockState state = world.getBlockState(blockPos);

            if (Utils.isBlockLiquid(state) && world.getTileEntity(blockPos) == null)
                TileSpectralBlock.createSpectralBlock(world, blockPos, refresh);
            else {
                TileEntity tile = world.getTileEntity(blockPos);
                if (tile instanceof TileSpectralBlock)
                    ((TileSpectralBlock) tile).resetDuration(refresh);
            }
        }
    }

    @Override
    public int getRefreshTime() {
        return 1;
    }

    @Override
    public int getRefreshCost() {
        return 2;
    }

    @Override
    public ArrayList<RitualComponent> getComponents() {
        ArrayList<RitualComponent> components = new ArrayList<>();

        this.addCornerRunes(components, 2, 0, EnumRuneType.WATER);
        this.addRune(components, -2, 0, -1, EnumRuneType.AIR);
        this.addRune(components, -1, 0, -2, EnumRuneType.AIR);
        this.addRune(components, -2, 0, 1, EnumRuneType.AIR);
        this.addRune(components, 1, 0, -2, EnumRuneType.AIR);
        this.addRune(components, 2, 0, 1, EnumRuneType.AIR);
        this.addRune(components, 1, 0, 2, EnumRuneType.AIR);
        this.addRune(components, 2, 0, -1, EnumRuneType.AIR);
        this.addRune(components, -1, 0, 2, EnumRuneType.AIR);

        return components;
    }

    @Override
    public Ritual getNewCopy() {
        return new RitualSuppression();
    }
}
