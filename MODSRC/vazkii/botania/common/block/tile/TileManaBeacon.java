/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Mar 3, 2014, 5:09:30 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.entity.passive.EntitySheep;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.common.Botania;

public class TileManaBeacon extends TileMod {

	@Override
	public void updateEntity() {
		boolean redstone = false;
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			int redstoneSide = worldObj.getIndirectPowerLevelTo(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ, dir.ordinal());
			if(redstoneSide > 0)
				redstone = true;
		}

		if(!redstone) {
			float[] color = EntitySheep.fleeceColorTable[worldObj.getBlockMetadata(xCoord, yCoord, zCoord)];

			Botania.proxy.setWispFXDistanceLimit(false);
			for(int i = 0; i < 3; i++)
				Botania.proxy.wispFX(worldObj, xCoord + 0.5, yCoord, zCoord + 0.5, color[0], color[1], color[2], (float) Math.random() * 5 + 1F, (float) (Math.random() - 0.5F), 10F * (float) Math.sqrt(256F / (256F - yCoord)), (float) (Math.random() - 0.5F));

			for(int i = 0; i < 4; i++)
				Botania.proxy.wispFX(worldObj, xCoord + 0.5, 256, zCoord + 0.5, color[0], color[1], color[2], (float) Math.random() * 15 + 8F, (float) (Math.random() - 0.5F) * 8F, 0F, (float) (Math.random() - 0.5F) * 8F);
			Botania.proxy.setWispFXDistanceLimit(true);
		}
	}
}
