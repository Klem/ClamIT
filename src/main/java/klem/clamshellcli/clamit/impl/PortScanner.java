package klem.clamshellcli.clamit.impl;

import klem.clamshellcli.clamit.cmd.ScanPortsCommand;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 ClamIT Actual implementation called by {@link klem.clamshellcli.clamit.cmd.ScanPortsCommand}
 <p/>
 Author : Klem Date   : 24/01/2015 Time   : 15:32 */
public class PortScanner extends Thread {

    private final InetAddress address;
    private final int port;

    public PortScanner(InetAddress address, int port) {
        this.address = address;
        this.port = port;

    }

    @Override
    public void run() {

            try {
                Socket s = new Socket(address,port);
                System.out.print(String.format("%n%s	::	OPEN", port));
                ScanPortsCommand.OPEN++;
                s.close();
            }
            catch (IOException ex) {

            }

    }
}
