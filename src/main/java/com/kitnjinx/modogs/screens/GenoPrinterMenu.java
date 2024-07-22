package com.kitnjinx.modogs.screens;

import com.kitnjinx.modogs.block.ModBlocks;
import com.kitnjinx.modogs.item.GenoReaderItem;
import com.kitnjinx.modogs.item.ModItems;
import com.kitnjinx.modogs.util.ModTags;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class GenoPrinterMenu extends ItemCombinerMenu {
    public GenoPrinterMenu(int pContainerId, Inventory pPlayerInventory) {
        this(pContainerId, pPlayerInventory, ContainerLevelAccess.NULL);
    }

    public GenoPrinterMenu(int pContainerId, Inventory pPlayerInventory, ContainerLevelAccess pAccess) {
        super(ModMenuTypes.GENO_PRINTER_MENU.get(), pContainerId, pPlayerInventory, pAccess);
    }

    public GenoPrinterMenu(int i, Inventory inventory, FriendlyByteBuf friendlyByteBuf) {
        this(i, inventory);
    }

    protected @NotNull ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create().withSlot(0, 27, 47, (itemStack) ->
                        this.isAllowed(0, itemStack))
                .withSlot(1, 76, 47, (itemStack) -> this.isAllowed(1, itemStack))
                .withResultSlot(2, 134, 47).build();
    }

    private boolean isAllowed(int slotIndex, ItemStack itemStack) {
        if (slotIndex == 0) {
            return itemStack.getItem() instanceof GenoReaderItem;
        } else {
            return itemStack.is(ModTags.Items.PAPER_STRIPS);
        }
    }

    protected boolean isValidBlock(BlockState pState) {
        return pState.is(ModBlocks.GENO_PRINTER.get());
    }

    protected boolean mayPickup(Player pPlayer, boolean pHasStack) {
        return pHasStack;
    }

    protected void onTake(Player pPlayer, ItemStack pStack) {
        // use 1 paper strip
        ItemStack itemstack = this.inputSlots.getItem(1);
        if (!itemstack.isEmpty() && itemstack.getCount() > 1) {
            itemstack.shrink(1);
            this.inputSlots.setItem(1, itemstack);
        } else {
            this.inputSlots.setItem(1, ItemStack.EMPTY);
        }
    }

    /**
     * Called when the Printer's Input Slot changes, calculates the new result and puts it in the output slot.
     */
    public void createResult() {
        ItemStack itemstack = this.inputSlots.getItem(0);
        if (itemstack.isEmpty() && !(itemstack.getItem() instanceof GenoReaderItem)) {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
        } else if (((GenoReaderItem) itemstack.getItem()).getLastGeno() == null ||
                ((GenoReaderItem) itemstack.getItem()).getLastGeno().isEmpty()) {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
        } else {
            ItemStack itemstack2 = this.inputSlots.getItem(1);

            if (!itemstack2.isEmpty()) {
                ItemStack filledStrip = new ItemStack(ModItems.USED_PAPER_STRIP.get());
                filledStrip.set(DataComponents.CUSTOM_NAME,
                        Component.literal(((GenoReaderItem) itemstack.getItem()).getLastGeno()));
                this.resultSlots.setItem(0, filledStrip);
            } else {
                this.resultSlots.setItem(0, ItemStack.EMPTY);
            }
        }
        this.broadcastChanges();
    }
}
