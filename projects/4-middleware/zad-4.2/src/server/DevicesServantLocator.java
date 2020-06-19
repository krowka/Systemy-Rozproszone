package server;

import Smarthome.ActiveDevices;
import Smarthome.Device;
import com.zeroc.Ice.Object;
import com.zeroc.Ice.*;
import server.devices.ActiveDevicesI;

public class DevicesServantLocator implements ServantLocator {
    private final ActiveDevicesI activeDevices;

    public DevicesServantLocator(ActiveDevicesI activeDevices) {
        this.activeDevices = activeDevices;
    }

    @Override
    public LocateResult locate(Current current) {
        String name = current.id.name;

        if (name.equals(this.activeDevices.getName())) {
            return new ServantLocator.LocateResult(this.activeDevices, null);
        }

        return new ServantLocator.LocateResult(activeDevices.getByName(name), null);
    }

    @Override
    public void deactivate(String category) {

    }

    @Override
    public void finished(Current curr, Object servant, java.lang.Object cookie) throws UserException {

    }
}