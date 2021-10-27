package org.usfirst.frc.team7636.lib.victorspx.compassdrive;

import org.usfirst.frc.team7636.lib.victorspx.compassdrive.AngleDetection;
import org.usfirst.frc.team7636.robot.*;
import com.ctre.phoenix.motorcontrol.ControlMode;

public class Drive {

    private double aFrontBack;
    private double aLeftRight;
    private double aTurn;
    private double aPoint;
    private double aCorrect;

    private boolean Zflag;

    public static AngleDetection Angle;

    public void init() {

        Angle = new AngleDetection();
        Angle.init();
        Angle.run();

        Angle.restart();
        
        Zflag = false;
        aFrontBack = 0;
        aLeftRight = 0;
        aTurn = 0;
        aPoint = 0;
        aCorrect = 0;
    }

    public void run() {

        Shift();
        Turn();
        Point();
        SelfCorrection();

        if(Robot.Driver.getThrottle()<0){

            Robot.frontLeftMotor.set(ControlMode.PercentOutput, -aFrontBack +aLeftRight +aTurn +aPoint +aCorrect);
            Robot.rearLeftMotor.set(ControlMode.PercentOutput, -aFrontBack -aLeftRight +aTurn +aPoint +aCorrect);
            Robot.frontRightMotor.set(ControlMode.PercentOutput, -aFrontBack -aLeftRight -aTurn -aPoint -aCorrect);
            Robot.rearRightMotor.set(ControlMode.PercentOutput, -aFrontBack +aLeftRight -aTurn -aPoint -aCorrect);
        
        }else{

            Robot.frontLeftMotor.set(ControlMode.PercentOutput, 0);
            Robot.rearLeftMotor.set(ControlMode.PercentOutput, 0);
            Robot.frontRightMotor.set(ControlMode.PercentOutput, 0);
            Robot.rearRightMotor.set(ControlMode.PercentOutput, 0);    
            
        }
    }

    private void Shift() {

        double a = 0;

        if( Math.sqrt(Math.pow(Robot.Driver.getY(), 2) + Math.pow(Robot.Driver.getX(), 2)) <= Math.sqrt(0.02)) {
            a = 0;
        }else{
            a = Math.sqrt(Math.pow(Robot.Driver.getY(), 2) + Math.pow(Robot.Driver.getX(), 2)) - Math.sqrt(0.02);
        }

        aFrontBack = -a *Math.sin( Math.toRadians(Angle.aimAngle3) );
        aLeftRight = a *Math.cos( Math.toRadians(Angle.aimAngle3) );

    }

    private void Turn() {

        if((Robot.Driver.getZ()>= -0.2) && (Robot.Driver.getZ()<= 0.2)){
            aTurn = 0;
            Zflag = false;
            
        }else{
            aTurn = (10/8)*Robot.Driver.getZ() - (1/4);
            Zflag = true;
        }

    }

    private void Point() {

        if(Angle.POVflag){
            aPoint = MotionControl(Angle.aimAngle2, Angle.ahrs.getAngle());

        } else {
            aPoint = 0;
        }

    }

    private void SelfCorrection() {

        if(!Zflag && !Angle.POVflag){
            aCorrect = MotionControl(Angle.aimAngle1, Angle.ahrs.getAngle());

        } else {
            aCorrect = 0;
        }
        
    }

    private double MotionControl(double aimAngle, double robotAngle) {

        double value = 0;

        double speed = Math.sqrt( Math.pow((aimAngle - robotAngle),2) );

        if((speed >= 2) && (speed < 12)) {
            value = -0.2;

        } else if((speed >= 12) && (speed < 32)) {
            value = -0.3 + 0.1 *((speed - 32) / 20);

        } else if((speed >= 32) && (speed < 62)) {
            value = -0.4 + 0.1 *((speed - 62) / 30);

        } else if((speed >= 62) && (speed < 112)) {
            value = -0.6 + 0.2 *((speed - 112) / 50);

        } else if((speed >= 112) && (speed < 182)) {
            value = -0.8 + 0.2 *((speed - 182) / 70);
        }

        if((aimAngle - robotAngle) >= 2){
            value = -value;

        } else if(((aimAngle - robotAngle) >= -2) && ((aimAngle - robotAngle) <= 2)){
            value = 0;
        }

        return value;
    }
    
}