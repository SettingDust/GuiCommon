package com.lalameow.guicommon.client.gui;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Author: SettingDust.
 * Date: 2018/5/13.
 */
public class ISlot extends Slot {
    private boolean enabled = true;

    public ISlot(IInventory inventoryIn, int index, int xPosition, int yPosition, boolean enabled) {
        super(inventoryIn, index, xPosition, yPosition);
        this.enabled = enabled;
    }

    public ISlot(Slot slot, boolean enabled) {
        super(slot.inventory, slot.getSlotIndex(), slot.xPos, slot.yPos);
        this.enabled = enabled;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isEnabled() {
        return enabled;
    }
}
