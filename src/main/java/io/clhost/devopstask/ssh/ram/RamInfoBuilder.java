package io.clhost.devopstask.ssh.ram;

import io.clhost.devopstask.ssh.Builder;

import java.util.List;

public class RamInfoBuilder implements Builder<RAMInfo> {
    private static final String MEM = "Mem:";
    private static final String TOTAL = "total";
    private static final String USED = "used";
    private static final String FREE = "free";
    private static final String AVAILABLE = "available";

    private List<String> lines;
    private StringBuilder builder;

    private String total;
    private String used;
    private String free;
    private String available;

    private int totalPos = -1;
    private int usedPos = -1;
    private int freePos = -1;
    private int availablePos = -1;

    public RamInfoBuilder(List<String> lines) {
        this.lines = lines;
        builder = new StringBuilder();
    }

    @Override
    public RAMInfo build() {
        for (String line : lines) {
            String[] tokens = line.trim().replaceAll("\\s+", " ").split(" ");

            if (!tokens[0].equals(MEM)) {
                for (int i = 0; i < tokens.length; i++) {
                    switch (tokens[i]) {
                        case TOTAL:
                            totalPos = i + 1;
                            break;
                        case USED:
                            usedPos = i + 1;
                            break;
                        case FREE:
                            freePos = i + 1;
                            break;
                        case AVAILABLE:
                            availablePos = i + 1;
                            break;
                    }
                }
            }

            if (tokens[0].equals(MEM)) {
                if (checkPositions()) {
                    total = tokens[totalPos];
                    used = tokens[usedPos];
                    free = tokens[freePos];
                    available = tokens[availablePos];
                } else {
                    throw new IllegalStateException("Couldn't find [" + notFoundTokens() + "]");
                }
            }
        }

        return new RAMInfo(total, used, free, available);
    }

    private boolean checkPositions() {
        return totalPos != -1 && usedPos != -1 && freePos != -1 && availablePos != -1;
    }

    private String notFoundTokens() {
        builder.setLength(0);

        if (totalPos == -1) {
            builder.append(TOTAL).append(",");
        }

        if (usedPos == -1) {
            builder.append(USED).append(",");
        }

        if (freePos == -1) {
            builder.append(FREE).append(",");
        }

        if (availablePos == -1) {
            builder.append(TOTAL).append(".");
        }

        String res = builder.toString();
        return res.charAt(res.length() - 1) == ',' ? res.substring(0, res.length() - 2) : res;
    }
}
