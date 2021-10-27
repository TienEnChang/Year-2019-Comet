package org.usfirst.frc.team7636.lib.victorspx;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import org.usfirst.frc.team7636.robot.*;

public class NeutralModeLib {

    private int ModeCount;
    private boolean ModeFlag;

    public void init() {

        ModeCount = 0;
        ModeFlag = false;

        Robot.Arm.setNeutralMode(NeutralMode.Brake);
        Robot.intake.setNeutralMode(NeutralMode.Brake);
        Robot.elevator.setNeutralMode(NeutralMode.Brake);
        Robot.elevator2.setNeutralMode(NeutralMode.Brake);

        Robot.frontLeftMotor.setNeutralMode(NeutralMode.Brake);
        Robot.frontRightMotor.setNeutralMode(NeutralMode.Brake);
        Robot.rearLeftMotor.setNeutralMode(NeutralMode.Brake);
        Robot.rearRightMotor.setNeutralMode(NeutralMode.Brake);

    }

    public void run() {

        NeutralMode_ModeSwitch();

        if((ModeCount%2) == 0) {
            Robot.frontLeftMotor.setNeutralMode(NeutralMode.Brake);
            Robot.frontRightMotor.setNeutralMode(NeutralMode.Brake);
            Robot.rearLeftMotor.setNeutralMode(NeutralMode.Brake);
            Robot.rearRightMotor.setNeutralMode(NeutralMode.Brake);

        } else {
            Robot.frontLeftMotor.setNeutralMode(NeutralMode.Coast);
            Robot.frontRightMotor.setNeutralMode(NeutralMode.Coast);
            Robot.rearLeftMotor.setNeutralMode(NeutralMode.Coast);
            Robot.rearRightMotor.setNeutralMode(NeutralMode.Coast);
        }

    }

    private void NeutralMode_ModeSwitch() {

        if((ModeFlag == true) && !Robot.Driver.getRawButton(2)) {
            ModeFlag = false;

        } else if((ModeFlag == false) && Robot.Driver.getRawButton(2)) {
            ModeFlag = true;
            ModeCount ++;
            System.out.print("NeutralMode_ModeCount ++\n");
        }

    }

}