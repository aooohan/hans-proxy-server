package org.example.server;

import org.apache.commons.cli.*;

/**
 * @author ：lihan
 * @description：
 * @date ：2020/7/30 16:44
 */
public class ServerStarter {
    public static void main(String[] args) throws Exception {

        // args
        Options options = new Options();
        options.addOption("h", false, "Help");
        options.addOption("p", true, "Hans server port");
        options.addOption("pw", true, "Hans server password");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption("h")) {
            // print help
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("options", options);
        } else {

            int port = Integer.parseInt(cmd.getOptionValue("p", "8869"));
            String password = cmd.getOptionValue("pw");
            HansCoreServer server = new HansCoreServer();
            server.start(port, password);

            System.out.println("Hans server started on port " + port);
        }
    }
}
