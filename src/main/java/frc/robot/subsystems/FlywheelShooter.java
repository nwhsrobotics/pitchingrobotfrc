package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.subsystems.RobotCanUtils.CANSparkFlexController;
import frc.robot.subsystems.RobotCanUtils.CANSparkMaxController;
import frc.robot.subsystems.RobotCanUtils.MotorKind;
//figure out why it is going faster than it is said to be

public class FlywheelShooter extends SubsystemBase {
    /*
     * private final MotorController flywheelMotor;
    private final MotorController flywheelMotor2;
     */

    public static double rpmNotStatic;
    private final SparkFlexConfig configEl = new SparkFlexConfig();

    //public static final double ff = Constants.CANAssignments.ff;

    // Flywheel shooter uses Spark Flex now.
    //having trouble with ff, ask parth
    private final CANSparkFlexController flywheelMotor = new CANSparkFlexController(Constants.CANAssignments.FLYWHEEL_MOTOR_ID, MotorKind.NEO30AMP, configEl, IdleMode.kBrake,/*need to tune p, just setting it rlly low bc it might help with oscillating prob */ 0.000, 0.0, 0.1, /*Constants.CANAssignments.ff*/  Constants.CANAssignments.MAX_VELOCITY_RPM, Constants.CANAssignments.MAX_ACCEL_RPM_S, 20, 11.0);
    private final CANSparkFlexController flywheelMotor2 = new CANSparkFlexController(Constants.CANAssignments.FLYWHEEL_MOTOR2_ID, MotorKind.NEO30AMP, configEl, IdleMode.kBrake, 0.000, 0.0, 0.0, /*Constants.CANAssignments.ff*/ Constants.CANAssignments.MAX_VELOCITY_RPM, Constants.CANAssignments.MAX_ACCEL_RPM_S, 20 , 11.0);
    // private final CANSparkMaxController flywheelMotor = new CANSparkMaxController(Constants.CANAssignments.FLYWHEEL_MOTOR_ID, MotorKind.VORTEX, configEl, IdleMode.kBrake,/*need to tune p, just setting it rlly low bc it might help with oscillating prob */ 0.000, 0.0, 0.1, /*Constants.CANAssignments.ff*/  Constants.CANAssignments.MAX_VELOCITY_RPM, Constants.CANAssignments.MAX_ACCEL_RPM_S, 20, 11.0);
    // private final CANSparkMaxController flywheelMotor2 = new CANSparkMaxController(Constants.CANAssignments.FLYWHEEL_MOTOR2_ID, MotorKind.VORTEX, configEl, IdleMode.kBrake, 0.000, 0.0, 0.0, /*Constants.CANAssignments.ff*/ Constants.CANAssignments.MAX_VELOCITY_RPM, Constants.CANAssignments.MAX_ACCEL_RPM_S, 20 , 11.0);

    
    private final RelativeEncoder flywheelEncoder;
    private final RelativeEncoder flywheelEncoder2;

    private double targetRPM = 0;
        private double basePower;
    
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
    
            //need to see if this will help, errors showing up though
    
            stopFlywheel(); 
        }
    
    
        public void startFlywheel(double mph) {
            targetRPM = mph * Constants.CANAssignments.MPH_TO_RPM * 3;
            basePower = targetRPM * Constants.CANAssignments.OUTPUT_PER_RPM;
    }

    public void testFireAtLowPower() {
    flywheelMotor.set(0.1); // 10% power, start low
    flywheelMotor2.set(-0.1);
}

    public void stopFlywheel() {
        targetRPM = 0;
        flywheelMotor.set(0);
        flywheelMotor2.set(0);
    }

    // Call this periodically
    @Override
    public void periodic() {
        // Flywheel control
        if (targetRPM > 0) {
            double currentRPM = getCurrentRPM();
            //output = Math.min(Math.max(output, 0), 1);
            // flywheelMotor.getClosedLoopController().setReference(targetRPM , ControlType.kVelocity);
            // flywheelMotor2.getClosedLoopController().setReference(-targetRPM, ControlType.kVelocity);
            flywheelMotor.getClosedLoopController().setReference(targetRPM, ControlType.kVelocity, ClosedLoopSlot.kSlot0, basePower * 12);
            flywheelMotor2.getClosedLoopController().setReference(-targetRPM, ControlType.kVelocity, ClosedLoopSlot.kSlot0, -basePower * 12);
            SmartDashboard.putNumber("currentRPM", currentRPM );
            SmartDashboard.putNumber("targetRPM", targetRPM );
            SmartDashboard.putNumber("Flywheel1 Output", flywheelMotor.getAppliedOutput());
        } else {
            flywheelMotor.set(0);
            flywheelMotor2.set(0);
        }

        // Feeding control
        rpmNotStatic = getCurrentRPM();
    }

    // Get current RPM from encoder
    public double getCurrentRPM() {
        return flywheelEncoder.getVelocity();
        //return (flywheelEncoder.getVelocity() + Math.abs(flywheelEncoder2.getVelocity())) / 2.0;
    }



    //public boolean isAtSpeed() {
       
    //}


}
