package io.clhost.devopstask.core;

import io.clhost.devopstask.core.checker.Checker;
import io.clhost.devopstask.core.checker.OverloadMessage;
import io.clhost.devopstask.database.HibernateUtil;
import io.clhost.devopstask.database.Report;
import io.clhost.devopstask.database.service.ReportService;
import io.clhost.devopstask.mail.Mail;
import io.clhost.devopstask.mail.MailService;
import io.clhost.devopstask.ssh.SshService;
import io.clhost.devopstask.ssh.cpu.CPUInfo;
import io.clhost.devopstask.ssh.disk.DisksInfo;
import io.clhost.devopstask.ssh.ram.RAMInfo;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TaskManager {
    private static final int START_TIME = 5 * 1000;
    private static final int TIME = 20 * 1000;
    private final SshService sshService;
    private final MailService mailService;
    private final ReportService reportService;
    private final Timer timer;
    private final StringBuilder builder;

    public TaskManager() {
        this.timer = new Timer();
        this.sshService = new SshService();
        this.mailService = new MailService();
        this.reportService = new ReportService();
        this.builder = new StringBuilder();
    }

    public void run() throws IOException {
        sshService.configure();
        mailService.configure();
        mailService.addReceivers();
        HibernateUtil.start();

        System.out.println("Start");
        timer.schedule(new TimerTask() {
            private RAMInfo ramInfo;
            private CPUInfo cpuInfo;
            private DisksInfo diskInfo;

            @Override
            public void run() {
                builder.setLength(0);

                ramInfo = sshService.getRamInfo();
                cpuInfo = sshService.getCpuInfo();
                diskInfo = sshService.getDisksInfo();

                List<OverloadMessage> messages = Checker.checkOverload(ramInfo, cpuInfo, diskInfo);

                if (!messages.isEmpty()) {
                    for (OverloadMessage o : messages) {
                        builder.append(o.getText());
                    }
                    mailService.send(new Mail("PANIC!!! Overload report.", builder.toString()));
                }

                Report report = new Report();

                report.setTime(System.currentTimeMillis());
                report.setRam(ramInfo.toString());
                report.setCpu(cpuInfo.toString());
                report.setDisk(diskInfo.toString());

                try {
                    System.out.println("Saved: \n" + report.toString());
                    reportService.save(report);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, START_TIME, TIME);
    }

    public static void main(String[] args) throws IOException {
        TaskManager t = new TaskManager();
        t.run();
    }
}
