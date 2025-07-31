package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
//import com.revrobotics.SparkMax;
//import com.revrobotics.SparkMaxLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
//TEST  

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;

public class DriveBaseSubsystem extends SubsystemBase{
    private final SparkMax leftMotor = new SparkMax(Constants.DriveBase.LEFT_MOTOR_ID, MotorType.kBrushless);
    private final SparkMax rightMotor = new SparkMax(Constants.DriveBase.RIGHT_MOTOR_ID, MotorType.kBrushless);

    private final DifferentialDrive drive = new DifferentialDrive(leftMotor, rightMotor);

    public DriveBaseSubsystem() {
        leftMotor.setInverted(false);//needs to be false because it starts counterclockwise
        rightMotor.setInverted(true);//needs to be true because it starts clockwise
    }

    public void tankDrive(double leftSpeed, double rightSpeed) {
        drive.tankDrive(leftSpeed, rightSpeed);//sends power to left or right motor
    }

    public void arcadeDrive(double speed, double rotation) {
        drive.arcadeDrive(speed, rotation);//based on the motor controller, the motor will change based on how much faster or slower it has to go and how much it rotates left or right
    }

    public void stop() {
        drive.stopMotor();//stops the motor
    }
}
