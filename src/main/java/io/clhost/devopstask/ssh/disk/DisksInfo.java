package io.clhost.devopstask.ssh.disk;

import java.util.ArrayList;
import java.util.List;

public class DisksInfo {
    private List<DISKInfo> disks;
    private StringBuilder builder;

    public DisksInfo(List<DISKInfo> disks) {
        this.disks = disks;
        builder = new StringBuilder();
    }

    public DisksInfo() {
        this.disks = new ArrayList<>();
        builder = new StringBuilder();
    }

    public void addDiskInfo(DISKInfo diskInfo) {
        disks.add(diskInfo);
    }

    public List<DISKInfo> getDisks() {
        return disks;
    }

    @Override
    public String toString() {
        builder.append("disks: [");

        for (int i = 0; i < disks.size(); i++) {
            builder.append("(").append(i).append(")").append(disks.get(i).toString());
        }

        builder.append("]");
        return builder.toString();
    }
}
