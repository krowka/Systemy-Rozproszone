module Smarthome {
    enum State {Off, On};

    sequence<string> DevicesNames;

    struct TimeOfDay {
        short hour;
        short minute;
        short second;
    };

    struct WashingProgram {
        float temperature;
        TimeOfDay startTime;
    };

    exception Error {
        string reason;
    };

    interface Device {
        State getState();
        string getName();
        void turnOff();
        void turnOn();
    };

    interface TemperatureSensor extends Device {
        float getTemperature() throws Error;
    };

    interface AirConditioner extends Device {
        void setTemperature(float temperature) throws Error;
    };

    interface WashingDevice extends Device {
        void runProgram(WashingProgram program) throws Error;
        void stopProgram();
    };

    interface WashingMachine extends WashingDevice {
        void setSpinSpeed(int speed) throws Error;
        void setEcoMode(bool isEco) throws Error;
    };

    interface Dishwasher extends WashingDevice {
        void setSilentMode(bool isSilent) throws Error;
        void setSterilizationMode(bool isSterilized) throws Error;
    };

    interface ActiveDevices {
        DevicesNames listDevices();
        string getDeviceCategory(string deviceName) throws Error;
    };
};