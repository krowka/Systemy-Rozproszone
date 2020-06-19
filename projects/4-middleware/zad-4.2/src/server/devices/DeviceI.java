package server.devices;

import Smarthome.Device;
import Smarthome.State;
import com.zeroc.Ice.Current;

public class DeviceI implements Device {
    protected final String name;
    private State state = State.Off; // devices turned off by default ;)

    public DeviceI(String name) {
        this.name = name;
    }

    @Override
    public State getState(Current current) {
        return state;
    }

    @Override
    public String getName(Current current) {
        return name;
    }

    @Override
    public void turnOff(Current current) {
        System.out.printf("[DEVICE %s]: Turned off\n", name);
        this.state = State.Off;
    }

    @Override
    public void turnOn(Current current) {
        System.out.printf("[DEVICE %s]: Turned on\n", name);
        this.state = State.On;
    }
}
