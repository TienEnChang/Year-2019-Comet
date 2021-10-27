package org.usfirst.frc.team7636.lib;

import edu.wpi.first.wpilibj.DoubleSolenoid;

import edu.wpi.first.wpilibj.GenericHID.Hand;

import org.usfirst.frc.team7636.robot.*;

public class SolenoidCompressorLib {

    boolean uForward = false;
    boolean mForward = false;
    public void init() {
        Robot.botCompressor.start();
        Robot.botCompressor.setClosedLoopControl(true);
    }

    public void run() {
        if (Robot.Operator.getBumperPressed(Hand.kLeft)) {
            if(mForward){
                mForward = false;
                Robot.mainDSolenoid.set(DoubleSolenoid.Value.kReverse);
            }
            else if(!mForward){
                mForward = true;
                Robot.mainDSolenoid.set(DoubleSolenoid.Value.kForward);
            }
        }
        if (Robot.Operator.getBumperPressed(Hand.kRight)) {
            if(uForward){
                 uForward = false;
                Robot.upperDSolenoid.set(DoubleSolenoid.Value.kReverse);
             }
            else if(!uForward){
                 uForward = true;
                Robot.upperDSolenoid.set(DoubleSolenoid.Value.kForward);
             }
        }
    }
        
    }


