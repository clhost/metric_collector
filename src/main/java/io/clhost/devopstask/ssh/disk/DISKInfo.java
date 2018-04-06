package io.clhost.devopstask.ssh.disk;

public class DISKInfo {
    private String fs;
    private String size;
    private String used;
    private String available;

    private double load;

    public DISKInfo(String fs, String size, String used, String available, String usePercent) {
        this.fs = fs;
        this.size = size;
        this.used = used;
        this.available = available;

        load = Double.parseDouble(usePercent.substring(0, usePercent.length() - 1));
    }

    public String getFs() {
        return fs;
    }

    public String getSize() {
        return size;
    }

    public String getUsed() {
        return used;
    }

    public String getAvailable() {
        return available;
    }

    public double getLoad() {
        return load;
    }

    @Override
    public String toString() {
        return "disk: [" +
                "fs='" + fs + '\'' +
                ", size='" + size + '\'' +
                ", used='" + used + '\'' +
                ", available='" + available + '\'' +
                ", load='" + load + '\'' +
                ']';
    }
}
