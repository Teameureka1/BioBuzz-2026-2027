package org.firstinspires.ftc.teamcode.pedro;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Config {
        DcMotorEx fl;
        public DcMotorEx fr;
        public DcMotorEx bl;
        public DcMotorEx br;
        public DcMotorEx intake;




        // Intake Toggle
        public boolean intakeOn = false;
        public boolean bPressed = false;



    HardwareMap hwMap;
        private final OpMode opMode;

        public Config(OpMode opMode) {
            this.opMode = opMode;
        }

        public void init() {
            hwMap = opMode.hardwareMap;
            fr = hwMap.get(DcMotorEx.class, "fr");
            fl = hwMap.get(DcMotorEx.class, "fl");
            br = hwMap.get(DcMotorEx.class, "br");
            bl = hwMap.get(DcMotorEx.class, "bl");;


            intake = hwMap.get(DcMotorEx.class, "intake");

            fr.setDirection(DcMotorSimple.Direction.FORWARD);
            fl.setDirection(DcMotorSimple.Direction.REVERSE);
            bl.setDirection(DcMotorSimple.Direction.REVERSE);
            br.setDirection(DcMotorSimple.Direction.FORWARD);
            intake.setDirection(DcMotorSimple.Direction.FORWARD);


            // Brake Mode
            fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        }
    }
