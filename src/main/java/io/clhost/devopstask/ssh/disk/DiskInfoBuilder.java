package io.clhost.devopstask.ssh.disk;

import io.clhost.devopstask.ssh.Builder;

import java.util.List;

public class DiskInfoBuilder implements Builder<DisksInfo> {
    private static final String DEV = "dev";
    private static final String UDEV = "udev";
    private static final String FILESYSTEM = "Filesystem";
    private static final String SIZE = "Size";
    private static final String USED = "Used";
    private static final String USE_PERCENTS = "Use%";
    private static final String AVAILABLE = "Avail";

    private List<String> lines;
    private StringBuilder builder;

    private int fsPos = -1;
    private int sizePos = -1;
    private int usedPos = -1;
    private int availablePos = -1;
    private int usePercentPos = -1;

    private String fs;
    private String size;
    private String used;
    private String available;
    private String usePercent;

    public DiskInfoBuilder(List<String> lines) {
        this.lines = lines;
        builder = new StringBuilder();
    }

    @Override
    public DisksInfo build() {
        DisksInfo info = new DisksInfo();
        for (String line : lines) {
            String[] tokens = line.trim().replaceAll("\\s+", " ").split(" ");

            if (tokens[0].contains(FILESYSTEM)) {
                for (int i = 0; i < tokens.length; i++) {
                    switch (tokens[i]) {
                        case FILESYSTEM:
                            fsPos = i;
                            break;
                        case SIZE:
                            sizePos = i;
                            break;
                        case USED:
                            usedPos = i;
                            break;
                        case USE_PERCENTS:
                            usePercentPos = i;
                            break;
                        case AVAILABLE:
                            availablePos = i;
                            break;
                    }
                }
            }

            if (tokens[0].contains(DEV) && !tokens[0].contains(UDEV)) {
                if (checkPositions()) {
                    fs = tokens[fsPos];
                    size = tokens[sizePos];
                    used = tokens[usedPos];
                    available = tokens[availablePos];
                    usePercent = tokens[usePercentPos];
                } else {
                    throw new IllegalStateException("Couldn't find [" + notFoundTokens() + "]");
                }

                info.addDiskInfo(new DISKInfo(fs, size, used, available, usePercent));
            }
        }

        return info;
    }

    private boolean checkPositions() {
        return fsPos != -1 && sizePos != -1 && usedPos != -1 && availablePos != -1 && usePercentPos != -1;
    }

    private String notFoundTokens() {
        builder.setLength(0);

        if (fsPos == -1) {
            builder.append(FILESYSTEM).append(",");
        }

        if (sizePos == -1) {
            builder.append(SIZE).append(",");
        }

        if (usedPos == -1) {
            builder.append(USED).append(",");
        }

        if (availablePos == -1) {
            builder.append(AVAILABLE).append(",");
        }

        if (usePercentPos == -1) {
            builder.append(USE_PERCENTS).append(".");
        }

        String res = builder.toString();
        return res.charAt(res.length() - 1) == ',' ? res.substring(0, res.length() - 2) : res;
    }
}
