package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
//import com.revrobotics.SparkMax;
//import com.revrobotics.SparkMaxLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
//TEST  

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;
import frc.robot.subsystems.RobotCanUtils.CANSparkMaxController;
import frc.robot.subsystems.RobotCanUtils.MotorKind;

public class DriveBaseSubsystem extends SubsystemBase{
    private final SparkMaxConfig configDrive = new SparkMaxConfig();
    private final CANSparkMaxController leftMotor = new CANSparkMaxController(Constants.DriveBase.LEFT_MOTOR_ID,MotorKind.NEO30AMP, configDrive, IdleMode.kBrake, 0);
    private final CANSparkMaxController rightMotor = new CANSparkMaxController(Constants.DriveBase.RIGHT_MOTOR_ID,MotorKind.NEO30AMP,configDrive, IdleMode.kBrake, 0);

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
