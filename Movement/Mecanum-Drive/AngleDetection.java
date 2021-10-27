package org.usfirst.frc.team7636.lib.victorspx.compassdrive;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.DriverStation;

import org.usfirst.frc.team7636.robot.*;

public class AngleDetection {
    
    public AHRS ahrs;

    public double ModeCount1;
    public boolean ModeFlag1;

    public double ModeCount2;
    public boolean ModeFlag2;
    
    public boolean POVflag;

    public double aimAngle1, aimAngle2, aimAngle3;

    public void init() {

        try {
            ahrs = new AHRS(SPI.Port.kMXP);
            
        } catch (RuntimeException ex) {
            DriverStation.reportError("Error instantiatng navX-MXP:" + ex.getMessage(), true);
        }

        restart();
    }

    public void restart() {

        ahrs.zeroYaw();
        System.out.print("ahrs.zeroYaw():"+ahrs.getAngle()+"\n");

        ModeCount1 = 0;
        ModeFlag1 = false;
        ModeCount2 = 0;
        ModeFlag2 = false;
        POVflag = false;
        
        aimAngle1= 0;
        aimAngle2= 0;
        aimAngle3= 0;

    }

    public void run() {

        Thread Main_DetectionThread = new Thread(() -> {
            
            while(!Thread.interrupted()) {

                aimAngle1 = AngleCorrection(aimAngle1);
                
                SelfCor_ModeSwitch();
                Joystick_ModeSwitch();

                if((ModeCount1%2)==1){
                    aimAngle1 = ahrs.getAngle();  
                    //System.out.print("SelfCorrection Disabled");                  
                }
               
                if (Robot.Driver.getRawButton(7)) {
                    restart();
                }

            }

        });

        Thread POV_DetectionThread = new Thread(() -> {

            while(!Thread.interrupted()) {

                if(Robot.Driver.getPOV() == 0 || Robot.Driver.getPOV() == 90 || Robot.Driver.getPOV() == 180|| Robot.Driver.getPOV() == 270) {

                    aimAngle2 = Robot.Driver.getPOV();

                    while(((aimAngle2 - ahrs.getAngle()) < -180) || (180 < (aimAngle2 - ahrs.getAngle()))) {
                        aimAngle2 = AngleCorrection(aimAngle2);
                    }

                    POVflag = true;

                    try {
                        Thread.sleep(500); // delay

                    } catch(Exception e) {
                        //TODO: handle exception
                    }

                } else {
                    POVflag = false;
                }                
    
                //System.out.print("aimAngle2: "+aimAngle2+"\n");
            }
        });

        Thread Joystick_DetectionThread = new Thread(() -> {
           
            while(!Thread.interrupted()) {
                
                if( Math.sqrt(Math.pow(Robot.Driver.getY(), 2) + Math.pow(Robot.Driver.getX(), 2)) < Math.sqrt(0.02)) {

                    aimAngle3 = 90 - ahrs.getAngle();

                } else {
                    
                    if((-0.1 < Robot.Driver.getX()) && (Robot.Driver.getX() < 0.1)  &&  (0.1 <= Robot.Driver.getY())) {
                        aimAngle3 = 270;

                    } else if((-0.1 < Robot.Driver.getX()) && (Robot.Driver.getX() < 0.1)  &&  (Robot.Driver.getY() <= -0.1)) {
                        aimAngle3 = 90;

                    } else if((-0.1 < Robot.Driver.getY()) && (Robot.Driver.getY() < 0.1)  &&  (0.1 <= Robot.Driver.getX())) {
                        aimAngle3 = 0;

                    } else if((-0.1 < Robot.Driver.getY()) && (Robot.Driver.getY() < 0.1)  &&  (Robot.Driver.getX() <= -0.1)) {
                        aimAngle3 = 180;

                    } else {

                        if((0.1 <= Robot.Driver.getX()) && (0.1 <= Robot.Driver.getY())) {
                            aimAngle3 = -Math.toDegrees( Math.atan( Robot.Driver.getY() / Robot.Driver.getX() ) );

                        } else if((Robot.Driver.getX() <= -0.1) && (0.1 <= Robot.Driver.getY())) {
                            aimAngle3 = 180 -Math.toDegrees( Math.atan( Robot.Driver.getY() / Robot.Driver.getX() ) );

                        } else if((Robot.Driver.getX() <= -0.1) && (Robot.Driver.getY() <= -0.1)) {
                            aimAngle3 = 180 -Math.toDegrees( Math.atan( Robot.Driver.getY() / Robot.Driver.getX() ) );

                        } else if((0.1 <= Robot.Driver.getX()) && (Robot.Driver.getY() <= -0.1)) {
                            aimAngle3 = -Math.toDegrees( Math.atan( Robot.Driver.getY() / Robot.Driver.getX() ) );
                        }

                    }

                }

                if((ModeCount2%2) == 0) {
                    aimAngle3 = -aimAngle3 +90;
                    aimAngle3 = aimAngle3 -ahrs.getAngle();
                    aimAngle3 = -aimAngle3 +90;
                }

                aimAngle3 = AngleCorrection(aimAngle3);
                
                //System.out.print("aimAngle3: "+aimAngle3+"\n");
            }

        });

        
        Main_DetectionThread.setPriority(1);
        Joystick_DetectionThread.setPriority(2);
        POV_DetectionThread.setPriority(3);
        
        Main_DetectionThread.start();
        Joystick_DetectionThread.start();
        POV_DetectionThread.start();
        
    }

    private void SelfCor_ModeSwitch() {

        if((ModeFlag1 == true) && !Robot.Driver.getRawButton(1)) {
            ModeFlag1 = false;

        } else if((ModeFlag1 == false) && Robot.Driver.getRawButton(1)) {
            ModeFlag1 = true;
            ModeCount1 ++;
            Robot.output();
        }
        
    }

    private void Joystick_ModeSwitch() {

        if((ModeFlag2 == true) && !Robot.Driver.getRawButton(2)) {
            ModeFlag2 = false;

        } else if((ModeFlag2 == false) && Robot.Driver.getRawButton(2)) {
            ModeFlag2 = true;
            ModeCount2 ++;
        }
        
    }

    private double AngleCorrection(double aimAngle) {

        if(((aimAngle - ahrs.getAngle()) < -180)) {
            aimAngle += 360;

        } else if((180 < (aimAngle - ahrs.getAngle()))) {
            aimAngle -= 360;
        }

        return aimAngle;
    }

}