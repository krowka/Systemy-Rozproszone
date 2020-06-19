package server;

import Smarthome.*;
import server.devices.*;

public class Server {
    public static void main(String[] args) {
        ActiveDevicesI activeDevices = new ActiveDevicesI("devices");

        Device device = new DeviceI("device1");
        Device device2 = new DeviceI("device2");
        WashingMachine washingMachine = new WashingMachineI("washingMachine");
        Dishwasher dishwasher = new DishwasherI("dishwasher");
        AirConditioner airConditioner = new AirConditionerI("airConditioner");
        TemperatureSensor temperatureSensor = new TemperatureSensorI("temperatureSensor");
        WashingDevice washingDevice = new WashingDeviceI("washingDevice");

        activeDevices.addDevice(device);
        activeDevices.addDevice(device2);
        activeDevices.addDevice(washingMachine);
        activeDevices.addDevice(dishwasher);
        activeDevices.addDevice(airConditioner);
        activeDevices.addDevice(temperatureSensor);
        activeDevices.addDevice(washingDevice);

        try (com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args)) {
            DevicesServantLocator staticServantLocator = new DevicesServantLocator(activeDevices);

            com.zeroc.Ice.ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("Adapter", "default -p 8888");
            adapter.addServantLocator(staticServantLocator, "");

            adapter.activate();

            System.out.println("Server is listening on port 8888...");

            communicator.waitForShutdown();
        }
    }
}
