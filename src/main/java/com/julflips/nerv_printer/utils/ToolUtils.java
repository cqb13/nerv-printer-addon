package com.julflips.nerv_printer.utils;

import java.util.Set;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.block.state.BlockState;

public final class ToolUtils {

    public static ItemStack getBestTool(Set<ItemStack> tools, BlockState targetBlock) {
        // 1 is the default mining multiplier
        float bestScore = 1;
        ItemStack bestStack = null;
        for (ItemStack tool : tools) {
            if (tool.getDestroySpeed(targetBlock) > bestScore) {
                bestScore = tool.getDestroySpeed(targetBlock);
                bestStack = tool;
            }
        }
        // Default to Pickaxe if no tool increases the mining speed
        if (bestStack == null) {
            for (ItemStack tool : tools) {
                if (tool.is(ItemTags.PICKAXES)) {
                    return tool;
                }
            }
        }
        return bestStack;
    }

    public static boolean isTool(ItemStack itemStack) {
        if (itemStack.is(ItemTags.PICKAXES)
            || itemStack.is(ItemTags.AXES)
            || itemStack.is(ItemTags.SHOVELS)
            || itemStack.is(ItemTags.HOES)
            || itemStack.getItem() instanceof ShearsItem) {
            return true;
        }
        return false;
    }
}
