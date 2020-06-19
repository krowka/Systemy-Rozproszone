package server.devices;

import Smarthome.ActiveDevices;
import Smarthome.Device;
import Smarthome.Error;
import com.zeroc.Ice.Current;

import java.util.HashMap;
import java.util.Map;

public class ActiveDevicesI implements ActiveDevices {
    private Map<String, Device> devices = new HashMap<>();
    private String name;

    public ActiveDevicesI(String name) {
        this.name = name;
    }

    @Override
    public String[] listDevices(Current current) {
        return devices.keySet().toArray(String[]::new);
    }

    @Override
    public String getDeviceCategory(String deviceName, Current current) throws Error {
        if (!devices.containsKey(deviceName))
            throw new Error("There is no device with name " + deviceName);
        Device device = devices.get(deviceName);
        String className = device.getClass().getSimpleName();
        String categoryName = className.substring(0, className.length() - 1);
        return categoryName;
    }

    public void addDevice(Device device) {
        this.devices.put(device.getName(null), device);
    }

    public Map<String, Device> getDevicesMap() {
        return devices;
    }

    public String getName() {
        return this.name;
    }

    public Device getByName(String name) {
        return devices.get(name);
    }
}
