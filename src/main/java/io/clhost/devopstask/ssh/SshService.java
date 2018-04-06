package io.clhost.devopstask.ssh;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import io.clhost.devopstask.ssh.cpu.CPUInfo;
import io.clhost.devopstask.ssh.cpu.CpuInfoBuilder;
import io.clhost.devopstask.ssh.disk.DiskInfoBuilder;
import io.clhost.devopstask.ssh.disk.DisksInfo;
import io.clhost.devopstask.ssh.ram.RAMInfo;
import io.clhost.devopstask.ssh.ram.RamInfoBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SshService {
    private String user;
    private String passphrase;
    private String prvkey;
    private String host;
    private int port;

    private static int TIMEOUT = 15000;

    private static String GET_RAM = "free -m";
    private static String GET_DISK = "df -h";
    private static String GET_CPU = "mpstat";

    private static String PROP_USER = "ssh.user";
    private static String PROP_PASSPHRASE = "ssh.passphrase";
    private static String PROP_PRVKEY = "ssh.private_key";
    private static String PROP_HOST = "ssh.host";
    private static String PROP_PORT = "ssh.port";

    private boolean isConfigured = false;

    public void configure() {
        Properties prop = new Properties();

        try {
            prop.load(new InputStreamReader(getClass().getResourceAsStream("/app.properties")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        user = prop.getProperty(PROP_USER);
        passphrase = prop.getProperty(PROP_PASSPHRASE);
        prvkey = prop.getProperty(PROP_PRVKEY);
        host = prop.getProperty(PROP_HOST);

        try {
            port = Integer.parseInt(prop.getProperty(PROP_PORT));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        isConfigured = true;
    }

    public RAMInfo getRamInfo() {
        if (!isConfigured) {
            throw new IllegalStateException("Service is not configured.");
        }

        return new RamInfoBuilder(response(GET_RAM)).build();
    }

    public DisksInfo getDisksInfo() {
        if (!isConfigured) {
            throw new IllegalStateException("Service is not configured.");
        }

        return new DiskInfoBuilder(response(GET_DISK)).build();
    }

    public CPUInfo getCpuInfo() {
        if (!isConfigured) {
            throw new IllegalStateException("Service is not configured.");
        }

        return new CpuInfoBuilder(response(GET_CPU)).build();
    }

    private List<String> response(String command) {
        JSch jsch = new JSch();
        Session session = null;
        ChannelExec channel = null;
        try {
            if (passphrase != null) {
                jsch.addIdentity(prvkey, passphrase);
            } else {
                jsch.addIdentity(prvkey);
            }

            session = jsch.getSession(user, host, port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect(TIMEOUT);

            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            channel.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(channel.getInputStream()));
            String str;
            List<String> lines = new ArrayList<>();

            while ((str = reader.readLine()) != null) {
                lines.add(str);
            }

            return lines;
        } catch (JSchException | IOException e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.disconnect();
            }

            if (channel != null) {
                channel.disconnect();
            }
        }

        return null;
    }
}
