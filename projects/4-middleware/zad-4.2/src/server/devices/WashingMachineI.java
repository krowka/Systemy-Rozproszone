package server.devices;

import Smarthome.Error;
import Smarthome.State;
import Smarthome.WashingMachine;
import com.zeroc.Ice.Current;

public class WashingMachineI extends WashingDeviceI implements WashingMachine {
    private int speed = 500; // spins per minute
    private boolean isEco = false;

    public WashingMachineI(String name) {
        super(name);
    }

    @Override
    public void setSpinSpeed(int speed, Current current) throws Error {
        if (this.getState(null) == State.Off)
            throw new Error("Turn on washing machine before getting setting spin speed.");

        System.out.printf("[WASHING MACHINE]: Spin speed set to %d spins per minute\n", speed);

        this.speed = speed;
    }

    @Override
    public void setEcoMode(boolean isEco, Current current) throws Error {
        if (this.getState(null) == State.Off)
            throw new Error("Turn on washing machine before getting setting ECO mode\n");

        String isEcoString = isEco ? "on" : "off";
        System.out.printf("[WASHING MACHINE]: ECO mode %s\n", isEcoString);

        this.isEco = isEco;
    }
}
