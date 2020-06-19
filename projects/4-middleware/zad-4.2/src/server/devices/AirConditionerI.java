package server.devices;

import Smarthome.AirConditioner;
import Smarthome.Error;
import Smarthome.State;
import com.zeroc.Ice.Current;

public class AirConditionerI extends DeviceI implements AirConditioner {
    private float temperature = 20.0F;

    public AirConditionerI(String name) {
        super(name);
    }

    @Override
    public void setTemperature(float temperature, Current current) throws Error {
        if (this.getState(null) == State.Off)
            throw new Error("Turn on air conditioning before setting temperature value.");
        System.out.println("[AIR CONDITIONING] Temperature set to : " + temperature);
        this.temperature = temperature;
    }
}
