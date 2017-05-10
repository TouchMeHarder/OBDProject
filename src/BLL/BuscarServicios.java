/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BLL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.bluetooth.*;

/**
 *
 * @author practicas
 */
public class BuscarServicios {

    private final UUID OBEX_OPP = new UUID(0x1105);

    private final int URL = 0x0100;

    public Map<String, String> dispositivosBth() {
        Object busquedaServicios = new Object();

        BuscarDispositivos dispositivos = new BuscarDispositivos();

        final Map<String, String> resultado = new HashMap<String, String>();

        DiscoveryListener serviciosDisponibles = new DiscoveryListener() {
            @Override
            public void deviceDiscovered(RemoteDevice rd, DeviceClass dc) {
            }

            @Override
            public void servicesDiscovered(int i, ServiceRecord[] srs) {
                for (int j = 0; j < srs.length; j++) {
                    String url = srs[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
                    
                    if (url == null) {
                        continue;
                    }
                    
                }
            }

            @Override
            public void serviceSearchCompleted(int i, int i1) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void inquiryCompleted(int i) {
            }
        };

        return resultado;
    }

}
