package server.devices;

import Smarthome.Dishwasher;
import Smarthome.Error;
import Smarthome.State;
import com.zeroc.Ice.Current;

public class DishwasherI extends WashingDeviceI implements Dishwasher {
    private boolean isSilent = false;
    private boolean isSterilized = false;

    public DishwasherI(String name) {
        super(name);
    }

    @Override
    public void setSilentMode(boolean isSilent, Current current) throws Error {
        if (this.getState(null) == State.Off)
            throw new Error("Turn on the dishwasher before getting setting silent mode.");

        String isSilentString = isSilent ? "on" : "off";
        System.out.printf("[DISHWASHER]: Silent mode %s\n", isSilentString);

        this.isSilent = isSilent;
    }

    @Override
    public void setSterilizationMode(boolean isSterilized, Current current) throws Error {
        if (this.getState(null) == State.Off)
            throw new Error("Turn on the dishwasher before getting setting sterilized mode.");

        String isSterilizedString = isSterilized ? "on" : "off";
        System.out.printf("[DISHWASHER]: Sterilized mode %s\n", isSterilizedString);

        this.isSterilized = isSterilized;
    }
}
