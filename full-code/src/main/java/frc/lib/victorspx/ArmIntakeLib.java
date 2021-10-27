package org.usfirst.frc.team7636.lib.victorspx;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team7636.robot.*;

public class ArmIntakeLib {

    private boolean ModeFlag;
    //private double aLiftDrop;
    
     public Encoder sampleEncoder;
    public SmartDashboard smartDashboard;
    public double encAngle = 0;
    public double armAngle;
    public double clear =0 ;
    public double clear2 = 10;
    public double run =0;
    public boolean noenctag = false;

    public void init() {
        ModeFlag = false;
        //aLiftDrop = 0;
        sampleEncoder = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
        sampleEncoder.setMaxPeriod(.1);
        sampleEncoder.setMinRate(10);
        sampleEncoder.setDistancePerPulse(5);
        sampleEncoder.setReverseDirection(true);
        sampleEncoder.setSamplesToAverage(7);
        sampleEncoder.reset();
    }
    
    public void run() {

        // Intake
        Robot.intake.set(ControlMode.PercentOutput, 
                -Robot.Operator.getTriggerAxis(Hand.kRight) + Robot.Operator.getTriggerAxis(Hand.kLeft));
        // Arm
        armAngle = round(sampleEncoder.get()+clear+clear2);
        if(Robot.Operator.getAButton()){
            sampleEncoder.reset(); 

        }
        if(armAngle>300){
            sampleEncoder.reset();
        }
        if(Robot.Operator.getStickButtonPressed(Hand.kRight)){
            noenctag = true;
            
        }
        if(noenctag){

        }
        if(Robot.Operator.getY(Hand.kRight) < -0.2&&!noenctag) {
            encAngle =100;    
            if(armAngle<80){
            Robot.Arm.set(ControlMode.PercentOutput,0.2);
            run=0.2;
            }
            else{
                Robot.Arm.set(ControlMode.PercentOutput,0.0);
                run=0.0;
            }
        }
        else if(Robot.Operator.getY(Hand.kRight) < -0.2&&noenctag)
         {
            Robot.Arm.set(ControlMode.PercentOutput,0.2);
         }
            else if(Robot.Operator.getY(Hand.kRight) > 0.2&&!noenctag) {
            //100~20
            if(armAngle>30){
            Robot.Arm.set(ControlMode.PercentOutput,-0.7);
            run=-0.8;}
            else if(armAngle>30){
                encAngle =10;
                Robot.Arm.set(ControlMode.PercentOutput,-0.4);
                run=-0.4;
                }
            
            }
            else if(Robot.Operator.getY(Hand.kRight) > 0.2&&noenctag) {
                Robot.Arm.set(ControlMode.PercentOutput,-0.4);
            }
            
            
            
            //20~10
           
            
            
            

         else if(!noenctag) {
             if(encAngle==10){
            if(armAngle<20){
                Robot.Arm.set(ControlMode.PercentOutput,0.03*(encAngle-round(armAngle)));
                run=0.03*(encAngle-round(armAngle));
                }
            }
        }
        else{
            Robot.Arm.set(ControlMode.PercentOutput,0.0);
        }
        smartDashboard.putNumber("angle",armAngle);
        smartDashboard.putNumber("angle",armAngle);
        smartDashboard.putNumber("run",run);

    }
    private double round(double angle) {

        if((-sampleEncoder.get()+clear) < 0) {
            clear += 497;
    
        } else if((497 < (-sampleEncoder.get()+clear))) {
            clear -= 497;
        }
    
        return ((-sampleEncoder.get()+clear)*360/497);
    }
    
    
    public void reset(){
        sampleEncoder.reset();
    }
    public void noenc(){

    }

} 