package net.sistr.flexiblesomething;

import net.minecraft.item.Item;
import net.sistr.flexiblesomething.item.FlexibleJsonReader;
import net.sistr.flexiblesomething.setup.Registration;

public class FlexibleSomethingMod {
    public static final String MOD_ID = "flexiblesomething";
    public static final FlexibleJsonReader<Item> ITEM_READER = FlexibleJsonReader.create();

    public static void init() {
        Registration.init();
    }
}
