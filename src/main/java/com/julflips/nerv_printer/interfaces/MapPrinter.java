package com.julflips.nerv_printer.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Tuple;

public interface MapPrinter {

    void setInterval(Tuple<Integer, Integer> interval);

    void mineLine(int minedLines);

    void addError(BlockPos relativeBlockPos);

    void pause();

    void start();

    boolean isActive();

    void toggle();

    boolean getActivationReset();

    void skipBuilding();

    void slaveFinished(String slave);
}
