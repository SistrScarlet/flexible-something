package net.sistr.flexiblesomething.util;

import net.minecraft.entity.Entity;

public interface Raidable {
    void addRaidEntity(Entity entity);
    void removeRaidEntity(Entity entity);
    boolean isRaiding();
    int getRaidKills();
}
