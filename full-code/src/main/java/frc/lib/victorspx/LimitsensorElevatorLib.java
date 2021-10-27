package org.usfirst.frc.team7636.lib.victorspx;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.*;

import org.usfirst.frc.team7636.robot.*;

public class LimitsensorElevatorLib {

    SmartDashboard smartDashboard;
    boolean top= false;
    boolean middle=false;
    boolean down;
    
    public void init() {
    }

    public void run() {
       
        smartDashboard.putBoolean("top", top);
        smartDashboard.putBoolean("middle",middle );
        smartDashboard.putBoolean("down",down );
        
        if (Robot.limitSwitchUp.get() && (!Robot.limitSwitchDown.get())) {
            top = true;
            middle = false;
            down = false;
        } else if ((!Robot.limitSwitchUp.get()) && Robot.limitSwitchDown.get()) {
           top = false;
            middle = false;
            down = true;
        } else {
            top = false;
            middle = true;
            down = false;
        }
        if(top){
            if(Robot.Operator.getY(Hand.kLeft)>0){
                Robot.elevator.set(ControlMode.PercentOutput,Robot.Operator.getY(Hand.kLeft));
            }
        }
        if(middle){
                Robot.elevator.set(ControlMode.PercentOutput,Robot.Operator.getY(Hand.kLeft));
        
        }
        if(down){
            if(Robot.Operator.getY(Hand.kLeft)<0){
                Robot.elevator.set(ControlMode.PercentOutput,Robot.Operator.getY(Hand.kLeft));
            }
        }
        
      
    }

}