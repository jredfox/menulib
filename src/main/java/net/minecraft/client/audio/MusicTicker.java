package net.minecraft.client.audio;

import java.util.Random;

import com.evilnotch.menulib.menu.MenuRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MusicTicker implements ITickable
{
    private final Random rand = new Random();
    private final Minecraft mc;
    private ISound currentMusic;
    private int timeUntilNextMusic = 100;

    public MusicTicker(Minecraft mcIn)
    {
        this.mc = mcIn;
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    public void update()
    {
        MusicTicker.MusicType type = this.mc.getAmbientMusicType();
        if(type == MusicType.MENU && !MenuRegistry.canPlayMusic(this.mc.currentScreen, MusicTicker.class))
        {
        	return;
        }

        if (this.currentMusic != null)
        {
            if (!type.getMusicLocation().getSoundName().equals(this.currentMusic.getSoundLocation()))
            {
                this.mc.getSoundHandler().stopSound(this.currentMusic);
                this.timeUntilNextMusic = MathHelper.getInt(this.rand, 0, type.getMinDelay() / 2);
            }

            if (!this.mc.getSoundHandler().isSoundPlaying(this.currentMusic))
            {
                this.currentMusic = null;
                this.timeUntilNextMusic = Math.min(MathHelper.getInt(this.rand, type.getMinDelay(), type.getMaxDelay()), this.timeUntilNextMusic);
            }
        }

        this.timeUntilNextMusic = Math.min(this.timeUntilNextMusic, type.getMaxDelay());

        if (this.currentMusic == null && this.timeUntilNextMusic-- <= 0)
        {
            this.playMusic(type);
        }
    }

	/**
     * Plays a music track for the maximum allowable period of time
     */
    public void playMusic(MusicTicker.MusicType requestedMusicType)
    {
        this.currentMusic = PositionedSoundRecord.getMusicRecord(requestedMusicType.getMusicLocation());
        this.mc.getSoundHandler().playSound(this.currentMusic);
        this.timeUntilNextMusic = Integer.MAX_VALUE;
    }

    @SideOnly(Side.CLIENT)
    public static enum MusicType
    {
        MENU(SoundEvents.MUSIC_MENU, 20, 600),
        GAME(SoundEvents.MUSIC_GAME, 12000, 24000),
        CREATIVE(SoundEvents.MUSIC_CREATIVE, 1200, 3600),
        CREDITS(SoundEvents.MUSIC_CREDITS, 0, 0),
        NETHER(SoundEvents.MUSIC_NETHER, 1200, 3600),
        END_BOSS(SoundEvents.MUSIC_DRAGON, 0, 0),
        END(SoundEvents.MUSIC_END, 6000, 24000);

        private final SoundEvent musicLocation;
        private final int minDelay;
        private final int maxDelay;

        private MusicType(SoundEvent musicLocationIn, int minDelayIn, int maxDelayIn)
        {
            this.musicLocation = musicLocationIn;
            this.minDelay = minDelayIn;
            this.maxDelay = maxDelayIn;
        }

        /**
         * Gets the {@link SoundEvent} containing the current music track's location
         */
        public SoundEvent getMusicLocation()
        {
            return this.musicLocation;
        }

        /**
         * Returns the minimum delay between playing music of this type.
         */
        public int getMinDelay()
        {
            return this.minDelay;
        }

        /**
         * Returns the maximum delay between playing music of this type.
         */
        public int getMaxDelay()
        {
            return this.maxDelay;
        }
    }
}