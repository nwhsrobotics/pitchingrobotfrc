package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import com.revrobotics.RelativeEncoder;

public class FlywheelShooter extends SubsystemBase {

    private final MotorController flywheelMotor;
    private final MotorController flywheelMotor2;
    private final RelativeEncoder flywheelEncoder;
    private final RelativeEncoder flywheelEncoder2;

    private final MotorController starwheelMotor;
    private final Encoder starwheelEncoder;  // Optional, for precise rotation control

    private final double feedRotationDegrees = 90;  // How much starwheel rotates to feed ball
    private boolean feeding = false;
    private double feedStartTime = 0;

    private final double feedMotorSpeed = 0.5;  // Adjust motor speed to rotate starwheel

    private final PIDController pidController;

    private double targetRPM = 0;

    public FlywheelShooter(MotorController flywheelMotor, Encoder flywheelEncoder, MotorController flywheelMotor2, Encoder flywheelEncoder2,
                           MotorController starwheelMotor, Encoder starwheelEncoder) {// do i have to add two fly wheel motors?
        this.flywheelMotor = flywheelMotor;
        this.flywheelEncoder = flywheelEncoder;
        this.flywheelMotor2 = flywheelMotor2;
        this.flywheelEncoder2 = flywheelEncoder2;
        this.starwheelMotor = starwheelMotor;
        this.starwheelEncoder = starwheelEncoder;

        pidController = new PIDController(0, 0, 0);//change these to change the pid values
        pidController.setTolerance(50);  // tolerance +50, -50
    }

    public void startFlywheel(double rpm) {
        targetRPM = rpm;
    }

    public void testFireAtLowPower() {
    flywheelMotor.set(0.1); // 10% power, start low
}

    public void stopFlywheel() {
        targetRPM = 0;
        flywheelMotor.set(0);
    }

    // Call this periodically
    @Override
    public void periodic() {
        // Flywheel control
        if (targetRPM > 0) {
            double currentRPM = getCurrentRPM();
            double output = pidController.calculate(currentRPM, targetRPM);
            output = Math.min(Math.max(output, 0), 1);
            flywheelMotor.set(output);
        } else {
            flywheelMotor.set(0);
        }

        // Feeding control
        if (feeding) {
            double elapsed = Timer.getFPGATimestamp() - feedStartTime;
            // Simple time-based feed control - adjust time for 90 deg rotation
            if (elapsed < getFeedDurationSeconds()) {
                starwheelMotor.set(feedMotorSpeed);
            } else {
                starwheelMotor.set(0);
                feeding = false;
            }
        }
    }

    // Get current RPM from encoder
    public double getCurrentRPM() {
        return (flywheelEncoder.getVelocity() + flywheelEncoder2.getVelocity()) / 2.0;
    }

    public void isFeeding() {
        System.out.println("is the feeder feeeding?: " + feeding);
    }

    public boolean isAtSpeed() {
        return pidController.atSetpoint();
    }

    //feeding ball by rotation of 90 degrees
    public void feedBall() {
        if (!feeding) {
            feeding = true;
            feedStartTime = Timer.getFPGATimestamp();
        }
    }

    //amoiunt of time itll take to switch the ball from the star wheel to the motor
    private double getFeedDurationSeconds() {
        ///need to test
        return 0.5;  
    }
}

/*
 * package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.math.controller.PIDController;
import org.littletonrobotics.junction.Logger; // AdvantageKit

public class FlywheelShooter extends SubsystemBase {

    public enum ShooterState {
        IDLE,
        SPINNING_UP,
        FEEDING
    }

    private final MotorController flywheelMotor;
    private final Encoder flywheelEncoder;

    private final MotorController starwheelMotor;

    private final PIDController pidController;

    private double targetRPM = 0;
    private final double feedMotorSpeed = 0.5;

    private ShooterState state = ShooterState.IDLE;

    private boolean feeding = false;
    private double feedStartTime = 0;

    public FlywheelShooter(MotorController flywheelMotor, Encoder flywheelEncoder,
                           MotorController starwheelMotor) {
        this.flywheelMotor = flywheelMotor;
        this.flywheelEncoder = flywheelEncoder;
        this.starwheelMotor = starwheelMotor;

        pidController = new PIDController(0.01, 0.001, 0.005);
        pidController.setTolerance(50); // ±50 RPM tolerance
    }

    public void setState(ShooterState newState) {
        this.state = newState;
    }

    public ShooterState getState() {
        return state;
    }

    public void setFlywheelRPM(double rpm) {
        targetRPM = rpm;
        setState(ShooterState.SPINNING_UP);
    }

    public void increaseRPM() {
        targetRPM += 100;
    }

    public void decreaseRPM() {
        targetRPM = Math.max(0, targetRPM - 100);
    }

    public void stopFlywheel() {
        targetRPM = 0;
        flywheelMotor.set(0);
        setState(ShooterState.IDLE);
    }

    public double getCurrentRPM() {
        return flywheelEncoder.getRate() * 60;
    }

    public boolean isAtSpeed() {
        return pidController.atSetpoint();
    }

    public void feedBall() {
        if (!feeding) {
            feeding = true;
            feedStartTime = Timer.getFPGATimestamp();
            setState(ShooterState.FEEDING);
        }
    }

    private double getFeedDurationSeconds() {
        return 0.5; // test & tune!
    }

    @Override
    public void periodic() {
        double currentRPM = getCurrentRPM();
        double output = 0;

        switch (state) {
            case SPINNING_UP:
                output = pidController.calculate(currentRPM, targetRPM);
                output = Math.min(Math.max(output, 0), 1);
                flywheelMotor.set(output);
                break;

            case FEEDING:
                output = pidController.calculate(currentRPM, targetRPM);
                flywheelMotor.set(Math.min(Math.max(output, 0), 1));

                double elapsed = Timer.getFPGATimestamp() - feedStartTime;
                if (elapsed < getFeedDurationSeconds()) {
                    starwheelMotor.set(feedMotorSpeed);
                } else {
                    starwheelMotor.set(0);
                    feeding = false;
                    setState(ShooterState.SPINNING_UP);
                }
                break;

            case IDLE:
            default:
                flywheelMotor.set(0);
                starwheelMotor.set(0);
                break;
        }

        //logging
        Logger.recordOutput("Shooter/State", state.toString());
        Logger.recordOutput("Shooter/TargetRPM", targetRPM);
        Logger.recordOutput("Shooter/CurrentRPM", currentRPM);
        Logger.recordOutput("Shooter/AtSpeed", isAtSpeed());
        Logger.recordOutput("Shooter/FlywheelOutput", flywheelMotor.toString());
    }
}

 */
