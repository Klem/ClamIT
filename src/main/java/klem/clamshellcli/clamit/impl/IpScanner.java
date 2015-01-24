package klem.clamshellcli.clamit.impl;

import klem.clamshellcli.clamit.cmd.ScanIPRangeCommand;
import klem.clamshellcli.clamit.utils.IPUtils;

import java.io.IOException;
import java.net.InetAddress;

/**
 ClamIT Actual implementation called by {@link klem.clamshellcli.clamit.tests.ScanIPRange}
 <p/>
 Author : Klem Date   : 24/01/2015 Time   : 13:31 */
public class IpScanner extends Thread {

    private final int i;
    private final int timeout;

    public IpScanner(int i, int timeout) {
        this.i = i;
        this.timeout = timeout;

    }

    @Override
    public void run() {
        InetAddress ip = null;
        String hostname = "unreachable";
        long ping = 0;
        try {
            String address = IPUtils.intToIp(i);
            ip = InetAddress.getByName(address);
            long currentTime = System.currentTimeMillis();
            ping = 0L;
            if (ip.isReachable(timeout)) {

                ping = System.currentTimeMillis() - currentTime;
                hostname = ip.getHostName();

                System.out.print(String.format("%n|	%s	|	%s	|	%s ms	|", ip, hostname, ping));

                ScanIPRangeCommand.REACHED ++;
            } else {
                ping = timeout;
                // TODO
               // System.out.print(String.format("%n|	%s	|	%s	|	%s ms	|", ip, hostname, ping));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
