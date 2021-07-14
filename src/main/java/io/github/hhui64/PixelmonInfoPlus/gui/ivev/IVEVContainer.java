package io.github.hhui64.PixelmonInfoPlus.gui.ivev;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class IVEVContainer extends Container {
    public IVEVContainer() {
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
