package com.desiremc.npc;

public class UnsupportedVersionException extends RuntimeException
{
    private static final long serialVersionUID = 8904476450714762706L;

    public UnsupportedVersionException(String msg)
    {
        super(msg);
    }
}
