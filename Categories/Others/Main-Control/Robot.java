package org.usfirst.frc.team7636.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Compressor;

import edu.wpi.first.wpilibj.DigitalInput;//limit sensor

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team7636.lib.*;
import org.usfirst.frc.team7636.lib.victorspx.*;
import org.usfirst.frc.team7636.lib.victorspx.compassdrive.*;

public class Robot extends TimedRobot {

    // This part is used for declaring variables and objects
    public static WPI_VictorSPX frontLeftMotor;
    public static VictorSPX rearLeftMotor;
    public static WPI_VictorSPX frontRightMotor;
    public static VictorSPX rearRightMotor;
    public static VictorSPX Arm;
    public static VictorSPX intake;
    public static VictorSPX elevator;
    public static VictorSPX elevator2;

    public static Joystick Driver;
    public static XboxController Operator;

    public static Timer time;

    public static DoubleSolenoid upperDSolenoid;
    public static DoubleSolenoid mainDSolenoid;
    
    
    public static DoubleSolenoid branchDSolenoid;
    public static Compressor botCompressor;

    public static DigitalInput limitSwitchUp;
    public static DigitalInput limitSwitchDown;

    private SolenoidCompressorLib S;
    private LimitsensorElevatorLib L;
    private NeutralModeLib N;
    private ArmIntakeLib A;

    private RobotCamera Cam1;
    private RobotCamera Cam2;

    private Drive CompassDrive;
    

    /**
     * This method is where you initialize variables and objects. Setup any
     * configurations of the robot here.
     */
    @Override
    public void robotInit() {

        frontLeftMotor = new WPI_VictorSPX(0);
        frontRightMotor = new WPI_VictorSPX(1);
        rearLeftMotor = new VictorSPX(2);
        rearRightMotor = new VictorSPX(3);
        Arm = new VictorSPX(4);
        intake = new VictorSPX(5);
        elevator = new VictorSPX(6);
        elevator2 = new VictorSPX(7);

        Driver = new Joystick(0); // Chassis
        Operator = new XboxController(1); // Elevator Arm and intake
        time = new Timer();

        upperDSolenoid = new DoubleSolenoid(0, 1); // DoubleSolenoid
        mainDSolenoid = new DoubleSolenoid(2,3);
        //branchDSolenoid = new DoubleSolenoid(4, 5);
        botCompressor = new Compressor(0);

        limitSwitchUp = new DigitalInput(2); // limitswitch
        limitSwitchDown = new DigitalInput(3);

        frontLeftMotor.setInverted(false);
        frontRightMotor.setInverted(true);
        rearLeftMotor.setInverted(false);
        rearRightMotor.setInverted(true);;
       
        elevator.setInverted(false);
        elevator2.set(ControlMode.Follower, 6);

        // CompassDrive
        CompassDrive = new Drive();
        CompassDrive.init();

        // AngleDetection
        
        // Arm & Intake
        A = new ArmIntakeLib();
        A.init();

        // Elevator
        L = new LimitsensorElevatorLib();

        // NetworkTable

        // NeutralMode
        N = new NeutralModeLib();
        N.init();

        // RobotCamera
        Cam1 = new RobotCamera(0);
        Cam2 = new RobotCamera(1);
        Cam1.init();
        Cam2.init();

        // Solenoid & Compressor
        S = new SolenoidCompressorLib();

        // Other Threads
    }

    @Override
    public void autonomousInit() {
        // Camera
        Cam1.init();
        Cam2.init();
    }

    @Override
    public void autonomousPeriodic() {
            
        // NeutralMode
        
        // Drive
        CompassDrive.run();
        
        // Arm & Intake
        //A.run();

        Robot.intake.set(ControlMode.PercentOutput, 
                -Robot.Operator.getTriggerAxis(Hand.kRight) + Robot.Operator.getTriggerAxis(Hand.kLeft));
              
        // Elevator
        L.run();

        // PCM
        S.init();
        S.run();

    }

    @Override
    public void teleopInit() {
        // Camera
        Cam1.init();
        Cam2.init();
        A.reset();
    }

    @Override
    public void teleopPeriodic() {

        // NeutralMode

        // Drive
        CompassDrive.run();
        
        // Arm & Intake
        //A.run();
        
        Robot.intake.set(ControlMode.PercentOutput, 
                -Robot.Operator.getTriggerAxis(Hand.kRight) + Robot.Operator.getTriggerAxis(Hand.kLeft));

        // Elevator
        L.run();

        // PCM
        S.init();
        S.run();

    }

    public static void output(){
        System.out.print("SelfCor_ModeCount ++\n");
    }

}