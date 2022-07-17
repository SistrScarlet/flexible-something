package net.sistr.flexiblesomething.item;

import net.minecraft.item.Item;

public class SimpleItem extends Item {

    public SimpleItem(Settings settings) {
        super(settings);
    }

    public SimpleItem(FlexibleArguments arg) {
        this(arg.orElseThrow(Item.Settings.class));
    }
}
