package io.clhost.devopstask.ssh.ram;

public class RAMInfo {
    private String total;
    private String used;
    private String free;
    private String available;
    private double load;

    public RAMInfo(String total, String used, String free, String available) {
        this.total = total;
        this.used = used;
        this.free = free;
        this.available = available;

        load = ((Double.parseDouble(total) - Double.parseDouble(available)) * 100) / Double.parseDouble(total);
    }

    public String getTotal() {
        return total;
    }

    public String getUsed() {
        return used;
    }

    public String getFree() {
        return free;
    }

    public String getAvailable() {
        return available;
    }

    public double getLoad() {
        return load;
    }

    @Override
    public String toString() {
        return "ram: [" +
                "total='" + total + '\'' +
                ", used='" + used + '\'' +
                ", free='" + free + '\'' +
                ", available='" + available + '\'' +
                ", load='" + load + '\'' +
                ']';
    }
}
