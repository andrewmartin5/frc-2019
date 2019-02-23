package frc.robot.subsystems;

import com.chopshop166.chopshoplib.outputs.DigitalOutputDutyCycle;
import com.mach.LightDrive.Color;
import com.mach.LightDrive.LightDriveCAN;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Leds extends Subsystem {
    LightDriveCAN ldrive_can;

    public Leds() { // NOPMD
        super();
        // Take values that the subsystem needs from the map, and store them in the
        // class

        LightDriveCAN ldrive_can = new LightDriveCAN();

    }

    @Override
    public void initDefaultCommand() {
        setTeamColor();
    }

    private boolean isBlueTeam() {
        Alliance team = DriverStation.getInstance().getAlliance();
        if (team == DriverStation.Alliance.Blue) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * public Command setTeamColor(boolean turnOn) { return new
     * InstantCommand("turn on team color", this, () -> { if (isBlueTeam()) {
     * ldrive_can.SetColor(1, Color.BLUE, (float) 0.8);
     * 
     * } else { ldrive_can.SetColor(3, Color.RED, (float) 0.8); }
     * ldrive_can.Update(); }); }
     */
    public Command setTeamColor() {
        // The command is named "Sample Command" and requires this subsystem.
        return new Command("Set team color", this) {

            protected void initialize() {
                // Called just before this Command runs the first time
                if (isBlueTeam()) {
                    ldrive_can.SetColor(1, Color.BLUE, (float) 0.8);

                } else {
                    ldrive_can.SetColor(3, Color.RED, (float) 0.8);
                }
                ldrive_can.Update();
            }

            @Override
            protected boolean isFinished() {
                // Make this return true when this Command no longer needs to run execute()
                return false;
            }

            @Override
            protected void end() {
                // Called once after isFinished returns true
                if (isBlueTeam()) {
                    ldrive_can.SetColor(1, Color.OFF, (float) 0.8);

                } else {
                    ldrive_can.SetColor(3, Color.OFF, (float) 0.8);
                }
                ldrive_can.Update();

            }

            // Called when another command which requires one or more of the same
            // subsystems is scheduled to run
            @Override
            protected void interrupted() {
                end();
            }

        };
    }

    public Command turnOnVisionLights() {
        return new Command("Turn on the color known as vision", this) {
            @Override
            protected void initialize() {
                // Called repeatedly when this Command is scheduled to run
                ldrive_can.SetColor(3, Color.GREEN, 1);
                ldrive_can.Update();
            }

            @Override
            protected boolean isFinished() {
                // Make this return true when this Command no longer needs to run execute()
                return true;
            }

        };
    }

    public Command turnOffVisionLights() {
        return new Command("Turn off the color known as vision", this) {
            @Override
            protected void initialize() {
                // Called once after isFinished returns true
                ldrive_can.SetColor(3, Color.OFF, 1);

            }

            @Override
            protected boolean isFinished() {
                // Make this return true when this Command no longer needs to run execute()
                return true;
            }

        };
    }

    public Command turnOnGreen() {
        // The command is named "Sample Command" and requires this subsystem.
        return new Command("Turn on the color known as verde", this) {

            @Override
            protected void execute() {
                // Called repeatedly when this Command is scheduled to run
                ldrive_can.SetColor(2, Color.GREEN, (float) 0.8);
                ldrive_can.Update();
            }

            @Override
            protected boolean isFinished() {
                // Make this return true when this Command no longer needs to run execute()
                return false;
            }

            @Override
            protected void end() {
                // Called once after isFinished returns true
                ldrive_can.SetColor(2, Color.OFF, (float) 0.8);

            }

        };
    }

    public Command turnOnRed() {
        // The command is named "Sample Command" and requires this subsystem.
        return new Command("Turn on the color known as rojo", this) {

            @Override
            protected void execute() {
                // Called repeatedly when this Command is scheduled to run
                ldrive_can.SetColor(3, Color.RED, (float) 0.8);
                ldrive_can.Update();

            }

            @Override
            protected boolean isFinished() {
                // Make this return true when this Command no longer needs to run execute()
                return false;
            }

            @Override
            protected void end() {
                // Called once after isFinished returns true
                ldrive_can.SetColor(2, Color.OFF, (float) 0.8);

            }

        };
    }

    public Command turnOnBlue() {
        // The command is named "Sample Command" and requires this subsystem.
        return new Command("Turn on the color known as Azul", this) {

            @Override
            protected void execute() {
                // Called repeatedly when this Command is scheduled to run
                ldrive_can.SetColor(1, Color.BLUE, (float) 0.8);
                ldrive_can.Update();

            }

            @Override
            protected boolean isFinished() {
                // Make this return true when this Command no longer needs to run execute()
                return false;
            }

            @Override
            protected void end() {
                ldrive_can.SetColor(2, Color.OFF, (float) 0.8);

                // Called once after isFinished returns true
            }

        };
    }

    public Command killAllLights() {
        // The command is named "Sample Command" and requires this subsystem.
        return new Command("Turn on the color known as Azul", this) {

            @Override
            protected void execute() {
                // Called repeatedly when this Command is scheduled to run
                ldrive_can.SetColor(1, Color.OFF);

            }

            @Override
            protected boolean isFinished() {
                // Make this return true when this Command no longer needs to run execute()
                return false;
            }

            @Override
            protected void end() {
                // Called once after isFinished returns true
            }
        };
    }

    public Command justBreathe(DigitalOutputDutyCycle color, int frequency) {
        // The command is named "Sample Command" and requires this subsystem.
        return new Command("Fade", this) {
            boolean isDutyCycleIncreasing = true;
            double period;
            final double executePeriod = 20 * 0.001; // Approx how often execute is called
            final double dutyCycleChangePerPeriod = 2.0;
            double changeAmount;

            @Override
            protected void initialize() {
                // Called just before this Command runs the first time
                period = (1.0 / frequency);
                changeAmount = dutyCycleChangePerPeriod / ((period / executePeriod));
                color.enablePWM(0);
                isDutyCycleIncreasing = true;
            }

            @Override
            protected void execute() {
                // Called repeatedly when this Command is scheduled to run
                if (isDutyCycleIncreasing == true) {
                    color.updateDutyCycle(color.getPWMRate() + changeAmount);
                } else {
                    color.updateDutyCycle(color.getPWMRate() - changeAmount);
                }
                if ((color.getPWMRate() >= 1) || (color.getPWMRate() <= 0)) {
                    isDutyCycleIncreasing = !isDutyCycleIncreasing;
                }

            }

            @Override
            protected boolean isFinished() {
                // Make this return true when this Command no longer needs to run execute()
                return false;
            }

            @Override
            protected void end() {
                // Called once after isFinished returns true
            }

            // Called when another command which requires one or more of the same
            // subsystems is scheduled to run
            @Override
            protected void interrupted() {
                end();
            }
        };
    }
}
