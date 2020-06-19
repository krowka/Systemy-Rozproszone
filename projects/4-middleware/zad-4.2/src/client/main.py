import Ice
import sys

import Smarthome

device_dict = {}


def list_devices(communicator):
    base = communicator.stringToProxy("devices:tcp -h localhost -p 8888")
    active_devices = Smarthome.ActiveDevicesPrx.checkedCast(base)
    device_list = active_devices.listDevices()

    print('Connected devices: ')
    for index in range(0, len(device_list)):
        device_dict[index] = device_list[index]
        print("   [{0}] {1}".format(index, device_list[index]))


def device_handler(base, device=None, function_name=None):
    is_direct_call = not device

    if not device:
        device = Smarthome.DevicePrx.checkedCast(base)
        print("DEVICE FUNCTIONS: getState, turnOff, turnOn")
        function_name = input("Function name ==> ")

    if function_name == "getState":
        print(device.getState())
        return True
    elif function_name == "turnOff":
        device.turnOff()
        return True
    elif function_name == "turnOn":
        device.turnOn()
        return True
    elif is_direct_call:
        print("Not supported function ", function_name)

    return False


def temperature_sensor_handler(base):
    sensor = Smarthome.TemperatureSensorPrx.checkedCast(base)
    print("TEMPERATURE SENSOR FUNCTIONS: getState, turnOff, turnOn, getTemperature.")
    function_name = input("Function name ==> ")
    executed = device_handler(base, sensor, function_name)

    if function_name == "getTemperature":
        print(sensor.getTemperature(), "°C")
    elif not executed:
        print("Not supported function ", function_name)


def air_conditioner_handler(base):
    air_conditioner = Smarthome.AirConditionerPrx.checkedCast(base)
    print("AIR CONDITIONER FUNCTIONS: getState, turnOff, turnOn, setTemperature.")
    function_name = input("Function name ==> ")
    executed = device_handler(base, air_conditioner, function_name)

    if function_name == "setTemperature":
        value = input("Desired temperature ===> ")
        air_conditioner.setTemperature(float(value))
    elif not executed:
        print("Not supported function ", function_name)


def make_program():
    print("Let's make new program")
    hour = int(input("hour ===> "))
    minute = int(input("minute ===> "))
    second = int(input("second ===> "))
    start_time = Smarthome.TimeOfDay(hour, minute, second)

    temperature = float(input("water temperature ===> "))
    return Smarthome.WashingProgram(temperature, start_time)


def washing_device_handler(base, washing_device=None, function_name=None):
    is_direct_call = not washing_device

    if not washing_device:
        washing_device = Smarthome.WashingDevicePrx.checkedCast(base)
        print("WASHING DEVICE FUNCTIONS: getState, turnOff, turnOn, runProgram, stopProgram.")
        function_name = input("Function name ==> ")

    executed = device_handler(base, washing_device, function_name)

    if function_name == "runProgram":
        program = make_program()
        washing_device.runProgram(program)
        return True
    elif function_name == "stopProgram":
        washing_device.stopProgram()
        return True
    elif not executed and is_direct_call:
        print("Not supported function ", function_name)

    return executed


def get_bool_input():
    i = input("EcoMode (true/false) ===> ")
    if i == "true":
        return True
    elif i == "false":
        return False
    else:
        raise RuntimeError("Invalid bool value: " + i + "\nSupported value: true, false.")


def washing_machine_handler(base):
    washing_machine = Smarthome.WashingMachinePrx.checkedCast(base)
    print("WASHING MACHINE FUNCTIONS: getState, turnOff, turnOn, runProgram, stopProgram, setSpinSpeed, setEcoMode.")
    function_name = input("Function name ==> ")
    executed = washing_device_handler(base, washing_machine, function_name)

    if function_name == "setSpinSpeed":
        washing_machine.setSpinSpeed(int(input("Spin speed ===> ")))
    elif function_name == "setEcoMode":
        eco_mode = get_bool_input()
        washing_machine.setEcoMode(eco_mode)
    elif not executed:
        print("Not supported function ", function_name)


def dishwasher_handler(base):
    dishwasher = Smarthome.DishwasherPrx.checkedCast(base)
    print("WASHING DEVICE FUNCTIONS: getState, turnOff, turnOn, runProgram, stopProgram, setSilentMode, "
          "setSterilizationMode.")
    function_name = input("Function name ==> ")
    executed = washing_device_handler(base, dishwasher, function_name)

    if function_name == "setSilentMode":
        silent_mode = get_bool_input()
        dishwasher.setSilentMode(silent_mode)
    elif function_name == "setSterilizationMode":
        sterilized = get_bool_input()
        dishwasher.setSterilizationMode(sterilized)
    elif not executed:
        print("Not supported function ", function_name)


def select_device(communicator, device_name, device_type):
    base = communicator.stringToProxy(device_name + ":tcp -h localhost -p 8888")  # connect to object proxy

    if device_type == "Device":
        device_handler(base)
    elif device_type == "TemperatureSensor":
        temperature_sensor_handler(base)
    elif device_type == "AirConditioner":
        air_conditioner_handler(base)
    elif device_type == "WashingDevice":
        washing_device_handler(base)
    elif device_type == "WashingMachine":
        washing_machine_handler(base)
    elif device_type == "Dishwasher":
        dishwasher_handler(base)
    else:
        raise RuntimeError("Unsupported device type: " + device_type)


def main():
    with Ice.initialize(sys.argv) as communicator:
        while True:
            try:
                print("\n\n[L] list devices")
                print("[S] select device")
                mode = (input('===> '))
                if mode not in ["L", "S", "l", "s"]:
                    raise RuntimeError("Invalid mode \nAvailable modes: L or S")

                if mode in ["L", "l"]:
                    list_devices(communicator)
                else:
                    base = communicator.stringToProxy("devices:tcp -h localhost -p 8888")
                    active_devices = Smarthome.ActiveDevicesPrx.checkedCast(base)

                    device_name = input("Device name or ID ==> ")
                    if device_name.isdigit():
                        device_name = device_dict.get(int(device_name))
                    device_category = active_devices.getDeviceCategory(device_name)
                    select_device(communicator, device_name, device_category)

            except Exception as e:
                print(e)
                print()


if __name__ == "__main__":
    main()
