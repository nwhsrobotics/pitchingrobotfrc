package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.DriveBaseSubsystem;
import edu.wpi.first.wpilibj.XboxController;

public class DriveCommand extends Command{
    private final DriveBaseSubsystem drivebase;
    private final CommandXboxController controller;

    public DriveCommand(DriveBaseSubsystem subsystem, CommandXboxController controller) {
        this.drivebase = subsystem;
        this.controller = controller;
        addRequirements(subsystem);
    }

    @Override
    public void execute() {
        double speed = -controller.getLeftY();  // go forward or back
        double rotation = controller.getRightX();  // go left or right
        drivebase.arcadeDrive(speed, rotation);
    }

    @Override
    public void end(boolean interrupted) {
        drivebase.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
    
}
