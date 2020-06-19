package server.devices;

import Smarthome.Error;
import Smarthome.State;
import Smarthome.WashingDevice;
import Smarthome.WashingProgram;
import com.zeroc.Ice.Current;

public class WashingDeviceI extends DeviceI implements WashingDevice {
    private WashingProgram washingProgram = new WashingProgram();

    public WashingDeviceI(String name) {
        super(name);
    }

    @Override
    public void runProgram(WashingProgram program, Current current) throws Error {
        if (this.getState(null) == State.Off)
            throw new Error("Turn on washing device before running program.");

        String timeString = String.format("%d:%d:%d", program.startTime.hour, program.startTime.minute, program.startTime.second);
        System.out.printf("[WASHING DEVICE %s] Program: temperature = %f running at %s\n", this.name, program.temperature, timeString);

        this.washingProgram = program;
    }

    @Override
    public void stopProgram(Current current) {
        System.out.println("[WASHING DEVICE %s] Program stopped\n");
        washingProgram = null;
    }
}
