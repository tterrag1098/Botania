/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 28, 2014, 9:09:39 PM (GMT)]
 */
package vazkii.botania.common.block.subtile;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTilePureDaisy extends SubTileEntity {

	private static final String TAG_POSITION = "position";
	private static final String TAG_TICKS_REMAINING = "ticksRemaining";

	private static final int[][] POSITIONS = new int[][] {
		{ -1, 0, -1 },
		{ -1, 0, 0 },
		{ -1, 0, 1 },
		{ 0, 0, 1 },
		{ 1, 0, 1 },
		{ 1, 0, 0 },
		{ 1, 0, -1 },
		{ 0, 0, -1 },
	};

	int positionAt = 0;
	int[] ticksRemaining = new int[] { 200, 200, 200, 200, 200, 200, 200, 200 };

	@Override
	public boolean canUpdate() {
		return true;
	}

	@Override
	public void onUpdate() {
		positionAt++;
		if(positionAt == POSITIONS.length)
			positionAt = 0;

		int[] acoords = POSITIONS[positionAt];
		ChunkCoordinates coords = new ChunkCoordinates(supertile.xCoord + acoords[0], supertile.yCoord + acoords[1], supertile.zCoord + acoords[2]);
		Block block =supertile.getWorldObj().getBlock(coords.posX, coords.posY, coords.posZ);
		if(block != Blocks.air) {
			String oredict = OreDictionary.getOreName(OreDictionary.getOreID(new ItemStack(block, 1, supertile.getWorldObj().getBlockMetadata(coords.posX, coords.posY, coords.posZ))));
			Block output = oredict.equals("stone") ? ModBlocks.livingrock : oredict.endsWith("logWood") ? ModBlocks.livingwood : null;
			if(output != null) {
				ticksRemaining[positionAt] = ticksRemaining[positionAt] - 1;

				Botania.proxy.sparkleFX(supertile.getWorldObj(), coords.posX + Math.random(), coords.posY + Math.random(), coords.posZ + Math.random(), 1F, 1F, 1F, (float) Math.random(), 5);

				if(ticksRemaining[positionAt] <= 0) {
					supertile.getWorldObj().setBlock(coords.posX, coords.posY, coords.posZ, output);
					ticksRemaining[positionAt] = 200;

					for(int i = 0; i < 25; i++) {
						double x = coords.posX + Math.random();
						double y = coords.posY + Math.random() + 0.5;
						double z = coords.posZ + Math.random();

						Botania.proxy.wispFX(supertile.getWorldObj(), x, y, z, 1F, 1F, 1F, (float) Math.random() / 2F);
					}
				}
			} else ticksRemaining[positionAt] = 200;
		}
	}

	@Override
	public void readFromPacketNBT(NBTTagCompound cmp) {
		positionAt = cmp.getInteger(TAG_POSITION);

		if(supertile.getWorldObj() != null && !supertile.getWorldObj().isRemote)
			for(int i = 0; i < ticksRemaining.length; i++)
				ticksRemaining[i] = cmp.getInteger(TAG_TICKS_REMAINING + i);
	}

	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_POSITION, positionAt);
		for(int i = 0; i < ticksRemaining.length; i++)
			cmp.setInteger(TAG_TICKS_REMAINING + i, ticksRemaining[i]);
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.pureDaisy;
	}
}
