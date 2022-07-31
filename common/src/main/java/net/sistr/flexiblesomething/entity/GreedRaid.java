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
    private final ServerBossBar bossBar = (ServerBossBar) new ServerBossBar(Text.of("?????"),
            BossBar.Color.BLUE, BossBar.Style.NOTCHED_6).setDarkenSky(true);
    private final World world;
    private final PlayerEntity player;
    private boolean isRaidedByGreed;
    private int maxRaidKills = 200;
    private int raidSize = 100;
    private float raidScale = 0.1f;
    private int raidKills;

    public GreedRaid(World world, PlayerEntity player) {
        this.world = world;
        this.player = player;
    }

    private void start() {
        this.isRaidedByGreed = true;
        bossBar.addPlayer((ServerPlayerEntity) player);
        bossBar.setVisible(true);
        this.world.setRainGradient(1f);
    }

    private void end() {
        this.isRaidedByGreed = false;
        bossBar.setVisible(false);
        bossBar.clearPlayers();
        bossBar.setPercent(0);
        raidKills = 0;
        calcRaidScale();
        raidEntities.forEach(Entity::discard);
        raidEntities.clear();
    }

    public void tick() {
        if (player.getOffHandStack().getItem() == Items.BLAZE_ROD) {
            this.raidSize = 400;
            this.maxRaidKills = 200;
        } else if (player.getOffHandStack().getItem() == Items.SNOWBALL) {
            this.raidSize = 200;
            this.maxRaidKills = 100;
        } else if (!this.isRaidedByGreed && player.getOffHandStack().getItem() == Items.CLOCK) {
            start();
        } else if (this.isRaidedByGreed && player.getOffHandStack().getItem() == Items.BOOK) {
            end();
        }
        if (isRaidedByGreed) {
            raidEntities.removeIf(Entity::isRemoved);
            bossBar.setPercent(MathHelper.clamp(1 - (float) raidKills / maxRaidKills, 0, 1));

            if (player.age % 2 == 0
                    && this.raidEntities.size() < raidSize * raidScale) {
                var rand = world.getRandom();
                int x = (int) (player.getX() + (rand.nextFloat() * 2 - 1) * 64);
                int y = (int) (this.player.getY() + 128);
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
            calcRaidScale();
        }
    }

    private void calcRaidScale() {
        int index = MathHelper.floor(raidKills / (maxRaidKills / 6.0f));
        switch (index) {
            case 0, 1, 2 -> raidScale = 0.25f;
            case 3, 4 -> raidScale = 0.5f;
            default -> raidScale = 1.0f;
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
