package org.firstinspires.ftc.teamcode.Teleop;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedro.Config;

@TeleOp(name = "BioBuzz")
public class BioBuzz extends OpMode {

    Config robot;
    @Override
    public void init() {

        robot = new Config(this);
        robot.init();

        telemetry.addLine("BioBuzz Initialized");
        telemetry.update();
    }

    @Override
    public void loop() {

        double y = -gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x;
        double rotation = gamepad1.right_stick_x;

        // Speed Control
        double speed = 0.3 + (0.7 * gamepad1.right_trigger);

        // Mecanum Math
        double fl = (y + x + rotation);
        double bl = (y - x + rotation);
        double fr = (y - x - rotation);
        double br = (y + x - rotation);

        // Normalize
        double max = Math.max(
                Math.max(Math.abs(fl), Math.abs(bl)),
                Math.max(Math.abs(fr), Math.abs(br))
        );

        if (max > 1.0) {
            fl /= max;
            bl /= max;
            fr /= max;
            br /= max;
        }

        // Apply Speed
        fl *= speed;
        bl *= speed;
        fr *= speed;
        br *= speed;

        // Set Motor Powers
        robot.fr.setPower(fl);
        robot.bl.setPower(bl);
        robot.fr.setPower(fr);
        robot.br.setPower(br);

        // =========================
        // INTAKE TOGGLE
        // =========================

        // Toggle intake with B
        if (gamepad1.b && !robot.bPressed) {
            robot.intakeOn = !robot.intakeOn;
        }

        robot.bPressed = gamepad1.b;

        // Reverse intake with X
        if (gamepad1.x) {
            robot.intake.setPower(-1.0);
        }
        else if (robot.intakeOn) {
            robot.intake.setPower(1.0);
        }
        else {
            robot.intake.setPower(0.0);
        }

        // =========================
        // TELEMETRY
        // =========================

        telemetry.addData("Speed", speed);
        telemetry.addData("Intake On", robot.intakeOn);

        telemetry.addData("FL", fl);
        telemetry.addData("FR", fr);
        telemetry.addData("BL", bl);
        telemetry.addData("BR", br);

        telemetry.addData("Raw Y", gamepad1.left_stick_y);
        telemetry.addData("Raw X", gamepad1.left_stick_x);

        telemetry.update();
    }
}