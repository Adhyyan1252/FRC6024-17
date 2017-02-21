//Sunday 21st February @ 20:30
package org.usfirst.frc.team6024.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	public static Joystick logitech;
	public static NetworkTable table;
	public static VictorSP TL, TR, BR, BL;
	public static AHRS navX;
	public static VictorSP shooter, winch;
	
	public void robotInit(){
		table = NetworkTable.getTable("datatable");
		logitech = new Joystick(0);
		navX = new AHRS(I2C.Port.kOnboard);
		navX.reset();
		TL = new VictorSP(2);
		TR = new VictorSP(1);
		BR = new VictorSP(9);
		BL = new VictorSP(7);
		shooter = new VictorSP(3);
		winch = new VictorSP(4);
		TR.setInverted(true);
		BR.setInverted(true);		
		Auto.autoSetup();
		Movement.drive(0, 0);
		Vision.camera = NetworkTable.getTable("camera");
		Vision.camera.putString("task", "stall");
		Vision.visionInit();
		Auto.chooser = new SendableChooser<Integer>();
		Auto.choose();
		SmartDashboard.putData("Chooser", Auto.chooser);
		dashboard();
	}
	public void disabled(){
		Movement.drive(0,0);
		dashboard();
	}
	public void autonomousInit(){
		Robot.navX.reset();
		dashboard();
		Auto.autoInit();
		Movement.drive(0, 0);
	}
	public void testPeriodic(){
		if(logitech.getRawButton(1)){
			double sp=((logitech.getRawAxis(2)*-1)+1)/2;
			shooter.set(sp);
			System.out.println(sp);
		}
	}
	
	public void disabledInit(){
		Vision.camera.putString("task", "stall");
	}
	
	public void autonomousPeriodic(){
		dashboard();
		long randomvariable = 0;
		randomvariable++;
}
	public static double xDist = 0, yDist = 0;
	
	public void teleopInit(){
		dashboard();
		Movement.drive(0, 0);
		
	}

	public void teleopPeriodic(){
		
		dashboard();
		Movement.teleOpMove();
		if(logitech.getRawButton(10) && Vision.canRunGear()){
			Vision.runGear();
		}
	}	
	
	
	public static void dashboard(){
		table.putNumber("TL", TL.get());
		table.putNumber("TR", TR.get());
		table.putNumber("BL", BL.get());
		table.putNumber("BR", BR.get());
		table.putNumber("HE", navX.getFusedHeading());
		table.putNumber("Time", System.currentTimeMillis());
		Auto.autoDash();
	}
}