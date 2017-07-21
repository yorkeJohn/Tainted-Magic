package taintedmagic.common.blocks;

import net.minecraft.block.Block;

public class CustomStepSound extends Block.SoundType
{
	public CustomStepSound (String soundPath, float volume, float pitch)
	{
		super(soundPath, volume, pitch);
	}

	public String getBreakSound ()
	{
		return this.soundName;
	}

	public String getStepResourcePath ()
	{
		return this.soundName;
	}
}
