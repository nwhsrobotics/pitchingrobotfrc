package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.subsystems.RobotCanUtils.CANSparkMaxController;
import frc.robot.subsystems.RobotCanUtils.MotorKind;

public class FlywheelShooter extends SubsystemBase {
    /*
     * private final MotorController flywheelMotor;
    private final MotorController flywheelMotor2;
     */



    private final SparkMaxConfig configEl = new SparkMaxConfig();

    private final CANSparkMaxController flywheelMotor = new CANSparkMaxController(Constants.CANAssignments.FLYWHEEL_MOTOR_ID, MotorKind.NEO30AMP, configEl, IdleMode.kBrake, 0.1, 0.0, 0.0, Constants.CANAssignments.MAX_VELOCITY_RPM, Constants.CANAssignments.MAX_ACCEL_RPM_S, 20, 11.0);
    private final CANSparkMaxController flywheelMotor2 = new CANSparkMaxController(Constants.CANAssignments.FLYWHEEL_MOTOR2_ID, MotorKind.NEO30AMP, configEl, IdleMode.kBrake, 0.1, 0.0, 0.0,  Constants.CANAssignments.MAX_VELOCITY_RPM, Constants.CANAssignments.MAX_ACCEL_RPM_S, 20 , 11.0);

    private final RelativeEncoder flywheelEncoder;
    private final RelativeEncoder flywheelEncoder2;

    private double targetRPM = 0;

    public FlywheelShooter() {// do i have to add two fly wheel motors?
       /*
        * this.flywheelMotor = firstmotor;
        this.flywheelEncoder = encoder1;
        this.flywheelMotor2 = secondmotor;
        this.flywheelEncoder2 = encoder2; do i need this
        */ 
        flywheelEncoder = flywheelMotor.getEncoder();
        flywheelEncoder2 = flywheelMotor2.getEncoder();

        //pidController = flywheelMotor.getPIDController();
        //pidController.setFeedbackDevice(flywheelEncoder);



        // Make second flywheel follow, inverted
        //flywheelMotor2.follow(flywheelMotor, true);
        //change these to change the pid values
        stopFlywheel(); 
    }


    public void startFlywheel(double mph) {
        targetRPM = mph * Constants.CANAssignments.MPH_TO_RPM;
    }

    public void testFireAtLowPower() {
    flywheelMotor.set(0.1); // 10% power, start low
    flywheelMotor2.set(-0.1);
}

    public void stopFlywheel() {
        targetRPM = 0;
        flywheelMotor.set(0);
        flywheelMotor.set(0);
    }

    // Call this periodically
    @Override
    public void periodic() {
        // Flywheel control
        if (targetRPM > 0) {
            double currentRPM = getCurrentRPM();
            //output = Math.min(Math.max(output, 0), 1);
            flywheelMotor.getClosedLoopController().setReference(targetRPM, ControlType.kVelocity);
            flywheelMotor2.getClosedLoopController().setReference(-targetRPM, ControlType.kVelocity);
            SmartDashboard.putNumber("currentRPM", currentRPM);
            SmartDashboard.putNumber("targetRPM", targetRPM);
        } else {
            flywheelMotor.set(0);
            flywheelMotor2.set(0);
        }

        // Feeding control
    }

    // Get current RPM from encoder
    public double getCurrentRPM() {
        return (flywheelEncoder.getVelocity() + flywheelEncoder2.getVelocity()) / 2.0;
    }



    //public boolean isAtSpeed() {
       
    //}


}
