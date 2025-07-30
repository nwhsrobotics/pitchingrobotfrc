package main.java.frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveBaseSubsystem;
import edu.wpi.first.wpilibj.XboxController;

public class DriveCommand extends CommandBase{
    private final DriveBaseSubsystem drivebase;
    private final XboxController controller;

    public DriveCommand(DriveBaseSubsystem subsystem, XboxController controller) {
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
