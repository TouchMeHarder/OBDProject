/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import com.github.pires.obd.commands.engine.RPMCommand;
import com.github.pires.obd.commands.protocol.EchoOffCommand;
import com.github.pires.obd.commands.protocol.LineFeedOffCommand;
import com.github.pires.obd.commands.protocol.SelectProtocolCommand;
import com.github.pires.obd.commands.protocol.TimeoutCommand;
import com.github.pires.obd.enums.ObdProtocols;
import java.io.*;
import java.util.Vector;
import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

public class elmbtooth implements DiscoveryListener {

    private static Object lock = new Object();

    private static Vector remdevices = new Vector();

    private static String connectionURL = null;

    public static void main(String args[]) throws IOException {
        BufferedReader b = new BufferedReader(new InputStreamReader(System.in));

        elmbtooth obj = new elmbtooth();
        LocalDevice locdevice = LocalDevice.getLocalDevice();
        String add = locdevice.getBluetoothAddress();
        String friendly_name = locdevice.getFriendlyName();

        System.out.println("Local Bluetooth Address : " + add);
        System.out.println(""
                + ""
                + "Local Friendly name : " + friendly_name);

        DiscoveryAgent dis_agent = locdevice.getDiscoveryAgent();
        System.out.println("********Locating Devices******");
        dis_agent.startInquiry(DiscoveryAgent.GIAC, obj);
        try {

            synchronized (lock) {
                lock.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (remdevices.size() <= 0) {
            System.out.println("No devices found");

        } else {

            for (int i = 0; i < remdevices.size(); i++) {
                RemoteDevice remote_device = (RemoteDevice) remdevices.elementAt(i);
                System.out.println((i + 1) + ".)" + remote_device.getFriendlyName(true) + " " + remote_device.getBluetoothAddress());
            }
            System.out.println("Choose Device to establish SPP");
            int index = Integer.parseInt(b.readLine());

            RemoteDevice des_device = (RemoteDevice) remdevices.elementAt(index - 1);
            UUID[] uuidset = new UUID[1];
            uuidset[0] = new UUID("1101", true);

            dis_agent.searchServices(null, uuidset, des_device, obj);
            try {
                synchronized (lock) {
                    lock.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (connectionURL == null) {
                System.out.println("Device does not support SPP.");
            } else {
                System.out.println("Device supports SPP.");
                //connect to the server and send a line of text

                StreamConnection streamConnection = (StreamConnection) Connector.open(connectionURL);

                //send string
                OutputStream outStream = streamConnection.openOutputStream();
                InputStream inStream = streamConnection.openInputStream();
                try {

                    new SelectProtocolCommand(ObdProtocols.AUTO).run(inStream, outStream);

                    //TroubleCodesCommand trouble = new TroubleCodesCommand();
                    //trouble.run(inStream, outStream);
                    //System.out.println(trouble.getFormattedResult());

                    new EchoOffCommand().run(inStream, outStream);

                    new LineFeedOffCommand().run(inStream, outStream);

                    new TimeoutCommand(60).run(inStream, outStream);

                    new SelectProtocolCommand(ObdProtocols.AUTO).run(inStream, outStream);
                    
                    RPMCommand rpm = new RPMCommand();
                    rpm.run(inStream, outStream);
                    System.out.println(rpm.getRPM());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }

    }

    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
        if (!remdevices.contains(btDevice)) {
            remdevices.addElement(btDevice);
        }
    }

    @Override
    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
        if (!(servRecord == null) && servRecord.length > 0) {
            connectionURL = servRecord[0].getConnectionURL(0, false);
        }

    }

    @Override
    public void serviceSearchCompleted(int transID, int respCode) {
        synchronized (lock) {
            lock.notify();
        }

    }

    @Override
    public void inquiryCompleted(int discType) {
        synchronized (lock) {
            lock.notify();
        }
        switch (discType) {
            case DiscoveryListener.INQUIRY_COMPLETED:
                System.out.println("Inquiry Completed");
                break;

            case DiscoveryListener.INQUIRY_TERMINATED:
                System.out.println("Inquiry Terminated");
                break;

            case DiscoveryListener.INQUIRY_ERROR:
                System.out.println("Inquiry Error");
                break;

            default:
                System.out.println("Unknown Response Code");
        }
    }

}
