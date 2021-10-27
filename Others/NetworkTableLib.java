package org.usfirst.frc.team7636.lib;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;

public class NetworkTableLib {

    private NetworkTableInstance inst;
    private NetworkTable table;
    private NetworkTableEntry xEntry;
    private NetworkTableEntry yEntry;

    public void init() {
        inst = NetworkTableInstance.getDefault();
        table = inst.getTable("datatabte");
        xEntry = table.getEntry("X");
        yEntry = table.getEntry("Y");
    }

    public void run() {
        inst.startClientTeam(7636);
        inst.startDSClient();
    }

}