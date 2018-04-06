package io.clhost.devopstask.ssh.cpu;

import io.clhost.devopstask.ssh.Builder;

import java.util.Arrays;
import java.util.List;

public class CpuInfoBuilder implements Builder<CPUInfo> {
    private static final String ALL = "all";
    private static final String CPU = "CPU";
    private static final String USR = "%usr";
    private static final String SYS = "%sys";

    private List<String> lines;
    private StringBuilder builder;

    private int cpuPos = -1;
    private int usrPos = -1;
    private int sysPos = -1;

    private String cpu;
    private String usr;
    private String sys;

    public CpuInfoBuilder(List<String> lines) {
        this.lines = lines;
        builder = new StringBuilder();
    }

    @Override
    public CPUInfo build() {
        for (String line : lines) {
            String[] tokens = line.trim().replaceAll("\\s+", " ").split(" ");

            if (tokens.length > 2 && (tokens[1].equals(CPU) || tokens[2].equals(CPU))) {
                for (int i = 0; i < tokens.length; i++) {
                    switch (tokens[i]) {
                        case CPU:
                            cpuPos = i;
                            break;
                        case USR:
                            usrPos = i;
                            break;
                        case SYS:
                            sysPos = i;
                            break;
                    }
                }
            }

            if (tokens.length > 2 && (tokens[1].equals(ALL) || tokens[2].equals(ALL))) {
                if (checkPositions()) {
                    cpu = tokens[cpuPos];
                    usr = tokens[usrPos];
                    sys = tokens[sysPos];
                } else {
                    throw new IllegalStateException("Couldn't find [" + notFoundTokens() + "]");
                }
            }
        }

        return new CPUInfo(cpu, usr, sys);
    }

    private boolean checkPositions() {
        return cpuPos != -1 && usrPos != -1 && sysPos != -1;
    }

    private String notFoundTokens() {
        builder.setLength(0);

        if (cpuPos == -1) {
            builder.append(CPU).append(",");
        }

        if (usrPos == -1) {
            builder.append(USR).append(",");
        }

        if (sysPos == -1) {
            builder.append(SYS).append(",");
        }

        String res = builder.toString();
        return res.charAt(res.length() - 1) == ',' ? res.substring(0, res.length() - 2) : res;
    }
}
