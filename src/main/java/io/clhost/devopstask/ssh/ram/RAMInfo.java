package io.clhost.devopstask.ssh.ram;

public class RAMInfo {
    private String total;
    private String used;
    private String free;
    private String available;

    public RAMInfo(String total, String used, String free, String available) {
        this.total = total;
        this.used = used;
        this.free = free;
        this.available = available;
    }

    @Override
    public String toString() {
        return "RAMInfo{" +
                "total='" + total + '\'' +
                ", used='" + used + '\'' +
                ", free='" + free + '\'' +
                ", available='" + available + '\'' +
                '}';
    }
}
