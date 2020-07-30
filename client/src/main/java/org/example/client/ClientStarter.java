package org.example.client;

import org.apache.commons.cli.*;

import java.util.HashMap;

/**
 * @author ：lihan
 * @description：
 * @date ：2020/7/30 16:28
 */
public class ClientStarter {
    public static void main(String[] args) throws Exception {
        // args
        Options options = new Options();
        options.addOption("h", false, "Help");
        options.addOption("si", true, "Hans server address");
        options.addOption("sp", true, "Hans server port");
        options.addOption("p", true, "Hans server password");
        options.addOption("pi", true, "Proxy server address");
        options.addOption("pp", true, "Hans server port");
        options.addOption("rp", true, "Hans server remote port");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        HashMap<String, Object> map = new HashMap<>();

        if (cmd.hasOption("h")) {
            // print help
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("options", options);
        } else {
            String serverAddress = cmd.getOptionValue("si");
            if (serverAddress == null) {
                System.out.println("serverIp cannot be null");
                return;
            }
            String serverPort = cmd.getOptionValue("sp");
            if (serverPort == null) {
                System.out.println("serverPort cannot be null");
                return;
            }
            String password = cmd.getOptionValue("p");
            String proxyAddress = cmd.getOptionValue("pi");
            if (proxyAddress == null) {
                System.out.println("proxyIp cannot be null");
                return;
            }
            String proxyPort = cmd.getOptionValue("pp");
            if (proxyPort == null) {
                System.out.println("proxyPort cannot be null");
                return;
            }
            String remotePort = cmd.getOptionValue("rp");
            if (remotePort == null) {
                System.out.println("remotePort cannot be null");
                return;
            }
            map.put("serverIp", serverAddress);
            map.put("serverPort", Integer.valueOf(serverPort));
            map.put("password", password);
            map.put("proxyIp", proxyAddress);
            map.put("proxyPort", Integer.valueOf(proxyPort));
            map.put("remotePort", Integer.valueOf(remotePort));
        }

        HansCoreClient hansCoreClient = new HansCoreClient(map);
        hansCoreClient.start();
    }
}
