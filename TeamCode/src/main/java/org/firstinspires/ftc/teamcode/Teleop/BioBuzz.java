package org.firstinspires.ftc.teamcode.Teleop;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "BioBuzz")
public class BioBuzz extends OpMode {

    // Drive Motors
    private DcMotorEx frontLeftMotor;
    private DcMotorEx frontRightMotor;
    private DcMotorEx backLeftMotor;
    private DcMotorEx backRightMotor;

    // Intake
    private DcMotorEx intake;

    // Intake Toggle
    private boolean intakeOn = false;
    private boolean previousB = false;

    @Override
    public void init() {

        // Hardware Mapping
        frontLeftMotor  = hardwareMap.get(DcMotorEx.class, "fl");
        frontRightMotor = hardwareMap.get(DcMotorEx.class, "fr");
        backLeftMotor   = hardwareMap.get(DcMotorEx.class, "bl");
        backRightMotor  = hardwareMap.get(DcMotorEx.class, "br");

        intake = hardwareMap.get(DcMotorEx.class, "intake");

        // Motor Directions
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        intake.setDirection(DcMotorSimple.Direction.FORWARD);

        // Brake Mode
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

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
        frontLeftMotor.setPower(fl);
        backLeftMotor.setPower(bl);
        frontRightMotor.setPower(fr);
        backRightMotor.setPower(br);

        // =========================
        // INTAKE TOGGLE
        // =========================

        // Toggle intake with B
        if (gamepad1.b && !previousB) {
            intakeOn = !intakeOn;
        }

        previousB = gamepad1.b;

        // Reverse intake with X
        if (gamepad1.x) {
            intake.setPower(-1.0);
        }
        else if (intakeOn) {
            intake.setPower(1.0);
        }
        else {
            intake.setPower(0.0);
        }

        // =========================
        // TELEMETRY
        // =========================

        telemetry.addData("Speed", speed);
        telemetry.addData("Intake On", intakeOn);

        telemetry.addData("FL", fl);
        telemetry.addData("FR", fr);
        telemetry.addData("BL", bl);
        telemetry.addData("BR", br);

        telemetry.addData("Raw Y", gamepad1.left_stick_y);
        telemetry.addData("Raw X", gamepad1.left_stick_x);

        telemetry.update();
    }
}