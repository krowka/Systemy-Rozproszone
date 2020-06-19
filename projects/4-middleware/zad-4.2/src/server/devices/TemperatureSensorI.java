package server.devices;

import Smarthome.Error;
import Smarthome.State;
import Smarthome.TemperatureSensor;
import com.zeroc.Ice.Current;

public class TemperatureSensorI extends DeviceI implements TemperatureSensor {
    private float temperature = 20.0F;

    public TemperatureSensorI(String name) {
        super(name);
    }

    @Override
    public float getTemperature(Current current) throws Error {
        if (this.getState(null) == State.Off)
            throw new Error("Turn on temperature sensor before getting temperature value.");

        return temperature;
    }
}
