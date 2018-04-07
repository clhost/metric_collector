package io.clhost.devopstask.core.checker;

import io.clhost.devopstask.ssh.cpu.CPUInfo;
import io.clhost.devopstask.ssh.disk.DISKInfo;
import io.clhost.devopstask.ssh.disk.DisksInfo;
import io.clhost.devopstask.ssh.ram.RAMInfo;

import java.util.ArrayList;
import java.util.List;

public class Checker {
    private static final double LOAD_CONSTANT = 90d;
    private static final double LOAD_INCREASE_CONSTANT = 70d;
    private static final int MAX_MINUTES = 15;
    private final Holder holder;

    public Checker() {
        holder = new Holder();
    }

    public List<OverloadMessage> checkOverload(RAMInfo ramInfo, CPUInfo cpuInfo, DisksInfo disksInfo) {
        List<OverloadMessage> messages = new ArrayList<>();
        double ramLoad = ramInfo.getLoad();
        double cpuLoad = cpuInfo.getLoad();

        if (ramLoad >= LOAD_CONSTANT) {
            messages.add(new OverloadMessage(
                    "RAM is overload: " + ramLoad + ".\n" +
                            "Report:\n" +
                            "\t Total space: " + ramInfo.getTotal() + "\n" +
                            "\t Free space: " + ramInfo.getFree() + "\n" +
                            "\t Used space: " + ramInfo.getUsed() + "\n" +
                            "\t Available space: " + ramInfo.getAvailable() + "\n",
                    OverloadMessage.OverloadType.RAM
            ));
        }

        if (cpuLoad >= LOAD_CONSTANT) {
            messages.add(new OverloadMessage(
                    "CPU is overload: " + cpuLoad + ".\n" +
                            "Report:\n" +
                            "\t Cpu: " + cpuInfo.getCpu() + "\n" +
                            "\t Usr load: " + cpuInfo.getUsr() + "\n" +
                            "\t Sys load: " + cpuInfo.getSys() + "\n",
                    OverloadMessage.OverloadType.CPU
            ));
        }

        for (DISKInfo diskInfo : disksInfo.getDisks()) {
            if (diskInfo.getLoad() >= LOAD_CONSTANT) {
                messages.add(new OverloadMessage(
                        "DISK \"" + diskInfo.getFs() + "\" is overload: " + diskInfo.getLoad() + ".\n" +
                                "Report:\n" +
                                "\t Size: " + diskInfo.getSize() + "\n" +
                                "\t Used: " + diskInfo.getUsed() + "\n" +
                                "\t Available: " + diskInfo.getAvailable() + "\n",
                        OverloadMessage.OverloadType.DISK
                ));
            }
        }

        return messages;
    }

    /**
     * Предполагается, что диски у holder'а и диски у disksInfo в одном порядке, т.е. на linux ноде никто
     * не вынимал никакой диск.
     */
    public List<OverloadMessage> checkIncrease(RAMInfo ramInfo, CPUInfo cpuInfo, DisksInfo disksInfo) {
        List<OverloadMessage> messages = new ArrayList<>();

        double currentRamLoad = holder.getRamLoad();
        double currentCpuLoad = holder.getCpuLoad();

        List<DISKInfo> disks = disksInfo.getDisks();

        if (holder.getDisks().isEmpty()) {
            for (int i = 0; i < disks.size(); i++) {
                holder.addDisk();
            }
        }

        for (int i = 0; i < holder.getDisks().size(); i++) {
            if (holder.getDiskLoad(i) <= disks.get(i).getLoad()) {
                holder.increaseDisk(i, disks.get(i).getLoad());
            } else {
                holder.resetDisk(i);
            }
        }

        if (currentRamLoad <= ramInfo.getLoad()) {
            holder.increaseRam(ramInfo.getLoad());
        } else {
            holder.resetRam();
        }

        if (currentCpuLoad <= cpuInfo.getLoad()) {
            holder.increaseCpu(cpuInfo.getLoad());
        } else {
            holder.resetCpu();
        }

        System.out.println("Holder info: ");
        System.out.println("\tram: " + holder.getRamLoad() + " " + holder.getRamTime());
        System.out.println("\tcpu: " + holder.getCpuLoad() + " " + holder.getCpuTime());
        for (int i = 0; i < holder.getDisks().size(); i++) {
            System.out.println("\tdisk(" + i + "): " + holder.getDiskLoad(i) + " " + holder.getDiskTime(i) + "\n");
        }
        System.out.println();

        if (holder.getRamTime() >= MAX_MINUTES) {
            if (holder.getRamLoad() >= LOAD_INCREASE_CONSTANT) {
                messages.add(new OverloadMessage(
                        "RAM is overload: " + currentRamLoad + ".\n" +
                                "The load increased and exceeded 70% during " + holder.getRamTime() + " minutes.\n" +
                                "Report (last dump):\n" +
                                "\t Total space: " + ramInfo.getTotal() + "\n" +
                                "\t Free space: " + ramInfo.getFree() + "\n" +
                                "\t Used space: " + ramInfo.getUsed() + "\n" +
                                "\t Available space: " + ramInfo.getAvailable() + "\n",
                        OverloadMessage.OverloadType.RAM
                ));
                holder.resetRam();
            }
        }

        if (holder.getCpuTime() >= MAX_MINUTES) {
            if (holder.getCpuLoad() >= LOAD_INCREASE_CONSTANT) {
                messages.add(new OverloadMessage(
                        "CPU is overload: " + currentCpuLoad + ".\n" +
                                "The load increased and exceeded 70% during " + holder.getCpuTime() + " minutes.\n" +
                                "Report(last dump):\n" +
                                "\t Cpu: " + cpuInfo.getCpu() + "\n" +
                                "\t Usr load: " + cpuInfo.getUsr() + "\n" +
                                "\t Sys load: " + cpuInfo.getSys() + "\n",
                        OverloadMessage.OverloadType.CPU
                ));
                holder.resetCpu();
            }
        }

        for (int i = 0; i < holder.getDisks().size(); i++) {
            if (holder.getDiskTime(i) >= MAX_MINUTES) {
                if (holder.getDiskLoad(i) >= LOAD_INCREASE_CONSTANT) {
                    messages.add(new OverloadMessage(
                            "DISK \"" + disks.get(i).getFs() + "\" is overload: " +
                                    disks.get(i).getLoad() + ".\n" +
                                    "The load increased and exceeded 70% during " + holder.getDiskTime(i) + " " +
                                    "minutes.\n" +
                                    "Report (last dump):\n" +
                                    "\t Size: " + disks.get(i).getSize() + "\n" +
                                    "\t Used: " + disks.get(i).getUsed() + "\n" +
                                    "\t Available: " + disks.get(i).getAvailable() + "\n",
                            OverloadMessage.OverloadType.DISK
                    ));
                    holder.resetDisk(i);
                }
            }
        }

        return messages;
    }
}
