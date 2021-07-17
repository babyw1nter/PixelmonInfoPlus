package io.github.hhui64.pixelmoninfoplus.gui.statspanel;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class StatsPanelContainer extends Container {
    public StatsPanelContainer() {
        super();
    }

    /**
     * Determines whether supplied player can use this container
     *
     * @param playerIn
     */
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }
}
