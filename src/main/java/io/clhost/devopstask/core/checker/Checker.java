package io.clhost.devopstask.core.checker;

import io.clhost.devopstask.ssh.cpu.CPUInfo;
import io.clhost.devopstask.ssh.disk.DISKInfo;
import io.clhost.devopstask.ssh.disk.DisksInfo;
import io.clhost.devopstask.ssh.ram.RAMInfo;

import java.util.ArrayList;
import java.util.List;

public class Checker {
    private static final double LOAD_CONSTANT = 90d;

    public static List<OverloadMessage> checkOverload(RAMInfo ramInfo, CPUInfo cpuInfo, DisksInfo disksInfo) {
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
}
