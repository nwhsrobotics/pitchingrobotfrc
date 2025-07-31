package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.FlywheelShooter;


public class ShootWhenReady extends Command {
    private final FlywheelShooter shooter;
    private boolean hasFed = false;

    public ShootWhenReady(FlywheelShooter shooter) {
        this.shooter = shooter;
        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        hasFed = false;
    }

    @Override
    public void execute() {
        if (shooter.isAtSpeed() && !hasFed) {
            shooter.feedBall();
            hasFed = true;
        }
    }

    @Override
    public boolean isFinished() {
        // end once we've fed the ball and the shooter is not feeding
        return hasFed && !shooter.isFeeding();
    }

    @Override
    public void end(boolean interrupted) {
        
    }
}
