package io.clhost.devopstask.ssh.cpu;

public class CPUInfo {
    private String cpu;
    private String usr;
    private String sys;

    private double load;

    public CPUInfo(String cpu, String usr, String sys) {
        this.cpu = cpu;
        this.usr = usr;
        this.sys = sys;

        load = Double.parseDouble(usr.replace(',', '.')) +
                Double.parseDouble(sys.replace(',', '.'));
    }

    public String getCpu() {
        return cpu;
    }

    public String getUsr() {
        return usr;
    }

    public String getSys() {
        return sys;
    }

    public double getLoad() {
        return load;
    }

    @Override
    public String toString() {
        return "cpu: [" +
                "cpu='" + cpu + '\'' +
                ", usr='" + usr + '\'' +
                ", sys='" + sys + '\'' +
                ", load='" + load + '\'' +
                ']';
    }
}
