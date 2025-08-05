package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.FlywheelShooter;


public class ShootWhenReadyCommand extends Command {
    private final FlywheelShooter shooter;
    private boolean hasFed = false;

    public ShootWhenReadyCommand(FlywheelShooter shooter) {
        this.shooter = shooter;
        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        hasFed = false;
    }

    @Override
    public void execute() {
    }


    @Override
    public void end(boolean interrupted) {
        
    }
}
