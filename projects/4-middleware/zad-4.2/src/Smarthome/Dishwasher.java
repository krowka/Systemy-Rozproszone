//
// Copyright (c) ZeroC, Inc. All rights reserved.
//
//
// Ice version 3.7.3
//
// <auto-generated>
//
// Generated from file `smarthome.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package Smarthome;

public interface Dishwasher extends WashingDevice
{
    void setSilentMode(boolean isSilent, com.zeroc.Ice.Current current)
        throws Error;

    void setSterilizationMode(boolean isSterilized, com.zeroc.Ice.Current current)
        throws Error;

    /** @hidden */
    static final String[] _iceIds =
    {
        "::Ice::Object",
        "::Smarthome::Device",
        "::Smarthome::Dishwasher",
        "::Smarthome::WashingDevice"
    };

    @Override
    default String[] ice_ids(com.zeroc.Ice.Current current)
    {
        return _iceIds;
    }

    @Override
    default String ice_id(com.zeroc.Ice.Current current)
    {
        return ice_staticId();
    }

    static String ice_staticId()
    {
        return "::Smarthome::Dishwasher";
    }

    /**
     * @hidden
     * @param obj -
     * @param inS -
     * @param current -
     * @return -
     * @throws com.zeroc.Ice.UserException -
    **/
    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_setSilentMode(Dishwasher obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
        throws com.zeroc.Ice.UserException
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        boolean iceP_isSilent;
        iceP_isSilent = istr.readBool();
        inS.endReadParams();
        obj.setSilentMode(iceP_isSilent, current);
        return inS.setResult(inS.writeEmptyParams());
    }

    /**
     * @hidden
     * @param obj -
     * @param inS -
     * @param current -
     * @return -
     * @throws com.zeroc.Ice.UserException -
    **/
    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_setSterilizationMode(Dishwasher obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
        throws com.zeroc.Ice.UserException
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        boolean iceP_isSterilized;
        iceP_isSterilized = istr.readBool();
        inS.endReadParams();
        obj.setSterilizationMode(iceP_isSterilized, current);
        return inS.setResult(inS.writeEmptyParams());
    }

    /** @hidden */
    final static String[] _iceOps =
    {
        "getName",
        "getState",
        "ice_id",
        "ice_ids",
        "ice_isA",
        "ice_ping",
        "runProgram",
        "setSilentMode",
        "setSterilizationMode",
        "stopProgram",
        "turnOff",
        "turnOn"
    };

    /** @hidden */
    @Override
    default java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceDispatch(com.zeroc.IceInternal.Incoming in, com.zeroc.Ice.Current current)
        throws com.zeroc.Ice.UserException
    {
        int pos = java.util.Arrays.binarySearch(_iceOps, current.operation);
        if(pos < 0)
        {
            throw new com.zeroc.Ice.OperationNotExistException(current.id, current.facet, current.operation);
        }

        switch(pos)
        {
            case 0:
            {
                return Device._iceD_getName(this, in, current);
            }
            case 1:
            {
                return Device._iceD_getState(this, in, current);
            }
            case 2:
            {
                return com.zeroc.Ice.Object._iceD_ice_id(this, in, current);
            }
            case 3:
            {
                return com.zeroc.Ice.Object._iceD_ice_ids(this, in, current);
            }
            case 4:
            {
                return com.zeroc.Ice.Object._iceD_ice_isA(this, in, current);
            }
            case 5:
            {
                return com.zeroc.Ice.Object._iceD_ice_ping(this, in, current);
            }
            case 6:
            {
                return WashingDevice._iceD_runProgram(this, in, current);
            }
            case 7:
            {
                return _iceD_setSilentMode(this, in, current);
            }
            case 8:
            {
                return _iceD_setSterilizationMode(this, in, current);
            }
            case 9:
            {
                return WashingDevice._iceD_stopProgram(this, in, current);
            }
            case 10:
            {
                return Device._iceD_turnOff(this, in, current);
            }
            case 11:
            {
                return Device._iceD_turnOn(this, in, current);
            }
        }

        assert(false);
        throw new com.zeroc.Ice.OperationNotExistException(current.id, current.facet, current.operation);
    }
}
