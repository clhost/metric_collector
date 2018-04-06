package io.clhost.devopstask.ssh.cpu;

public class CPUInfo {
    private String cpu;
    private String usr;
    private String sys;

    public CPUInfo(String cpu, String usr, String sys) {
        this.cpu = cpu;
        this.usr = usr;
        this.sys = sys;
    }

    @Override
    public String toString() {
        return "CPUInfo{" +
                "cpu='" + cpu + '\'' +
                ", usr='" + usr + '\'' +
                ", sys='" + sys + '\'' +
                '}';
    }
}
