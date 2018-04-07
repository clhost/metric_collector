package io.clhost.devopstask.database;

import javax.persistence.*;

@Entity
@Table(name = "report_table")
public class Report {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "time", nullable = false)
    private long time;

    @Column(name = "ram", nullable = false)
    private String ram;

    @Column(name = "disk", nullable = false)
    private String disk;

    @Column(name = "cpu", nullable = false)
    private String cpu;

    public Report() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getDisk() {
        return disk;
    }

    public void setDisk(String disk) {
        this.disk = disk;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    @Override
    public String toString() {
        return  "\t" + ram + "\n" +
                "\t" + cpu + "\n" +
                "\t" + disk + "\n";
    }
}
