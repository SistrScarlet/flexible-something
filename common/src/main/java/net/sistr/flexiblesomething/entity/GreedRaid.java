package net.sistr.flexiblesomething.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.sistr.flexiblesomething.util.Raidable;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public class GreedRaid implements Raidable {
    private final List<Entity> raidEntities = Lists.newArrayList();
    private boolean isRaidedByGreed;
    private int raidKills;
    private final ServerBossBar bossBar = (ServerBossBar) new ServerBossBar(Text.of("?????"),
            BossBar.Color.BLUE, BossBar.Style.NOTCHED_20).setDarkenSky(true);
    private final World world;
    private final PlayerEntity player;

    public GreedRaid(World world, PlayerEntity player) {
        this.world = world;
        this.player = player;
    }

    public void tick() {
        if (!this.isRaidedByGreed && player.getOffHandStack().getItem() == Items.CLOCK) {
            this.isRaidedByGreed = true;
            bossBar.addPlayer((ServerPlayerEntity) player);
            bossBar.setVisible(true);
            this.world.setRainGradient(1f);
        } else if (this.isRaidedByGreed && player.getOffHandStack().getItem() == Items.BOOK) {
            this.isRaidedByGreed = false;
            bossBar.setVisible(false);
            bossBar.clearPlayers();
            bossBar.setPercent(0);
            raidKills = 0;
            raidEntities.forEach(Entity::discard);
            raidEntities.clear();
            this.world.setRainGradient(0f);
        }
        if (isRaidedByGreed) {
            raidEntities.removeIf(Entity::isRemoved);
            bossBar.setPercent(MathHelper.clamp(1 - raidKills / 300f, 0, 1));
            if (player.age % 2 == 0
                    && this.raidEntities.size() < 100) {
                var rand = world.getRandom();
                int x = (int) (player.getX() + (rand.nextFloat() * 2 - 1) * 64);
                int y = this.world.getHeight();
                int z = (int) (player.getZ() + (rand.nextFloat() * 2 - 1) * 64);
                var greedChunk = new GreedChunkEntity(this.world);
                greedChunk.setTargetPlayer(player);
                greedChunk.setPosition(x, y, z);
                this.world.spawnEntity(greedChunk);
                this.raidEntities.add(greedChunk);
            }
        }
    }

    @Override
    public void addRaidEntity(Entity entity) {
        this.raidEntities.add(entity);
    }

    @Override
    public void removeRaidEntity(Entity entity) {
        this.raidEntities.remove(entity);
    }

    public void killRaidEntity(Entity entity) {
        if (raidEntities.contains(entity)) {
            raidKills++;
            raidEntities.remove(entity);
        }
    }

    @Override
    public boolean isRaiding() {
        return isRaidedByGreed;
    }

    @Override
    public int getRaidKills() {
        return raidKills;
    }
}
