package io.clhost.devopstask.ssh.disk;

public class DISKInfo {
    private String fs;
    private String size;
    private String used;
    private String available;
    private String usePercent;

    public DISKInfo(String fs, String size, String used, String available, String usePercent) {
        this.fs = fs;
        this.size = size;
        this.used = used;
        this.available = available;
        this.usePercent = usePercent;
    }

    @Override
    public String toString() {
        return "DISKInfo{" +
                "fs='" + fs + '\'' +
                ", size='" + size + '\'' +
                ", used='" + used + '\'' +
                ", available='" + available + '\'' +
                ", usePercent='" + usePercent + '\'' +
                '}';
    }
}
