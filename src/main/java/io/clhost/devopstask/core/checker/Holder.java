package io.clhost.devopstask.core.checker;

import java.util.ArrayList;
import java.util.List;

public class Holder {
    private Data cpu = new Data();
    private Data ram = new Data();
    private List<Data> disks = new ArrayList<>();

    void resetRam() {
        ram.load = 0;
        ram.minutes = 0;
    }

    void resetDisk(int i) {
        disks.set(i, new Data(0, 0));
    }

    void resetCpu() {
        cpu.load = 0;
        cpu.minutes = 0;
    }

    public List<Data> getDisks() {
        return disks;
    }

    public void addDisk() {
        disks.add(new Data(0, 0));
    }

    public double getCpuLoad() {
        return cpu.load;
    }

    public double getRamLoad() {
        return ram.load;
    }

    public double getDiskLoad(int i) {
        return disks.get(i).load;
    }

    public int getCpuTime() {
        return cpu.minutes;
    }

    public int getRamTime() {
        return ram.minutes;
    }

    public int getDiskTime(int i) {
        return disks.get(i).minutes;
    }

    void increaseRam(double load) {
        ram.minutes++;
        ram.load = load;
    }

    void increaseDisk(int i, double load) {
        int m = disks.get(i).minutes;
        disks.set(i, new Data(load, ++m));
    }

    void increaseCpu(double load) {
        cpu.minutes++;
        cpu.load = load;
    }

    private class Data {
        private double load;
        private int minutes;

        Data() {}

        public Data(double load, int minutes) {
            this.load = load;
            this.minutes = minutes;
        }
    }
}
