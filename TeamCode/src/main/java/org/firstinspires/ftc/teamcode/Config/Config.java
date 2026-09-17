package org.firstinspires.ftc.teamcode.Config;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/* In this class basically if you go to another class and say "Config robot" it will take
 all of this class and put it there, but remember to add robot.init inside your init.
 You also you need "robot = new Config(this);" before robot.init.
 */

public class Config {


    // These are variables for opmode types
    private LinearOpMode linearOpMode;
    private OpMode opmode;


    // This allows a linear Opmode to use this
    public Config(LinearOpMode linearOpMode) {

        this.linearOpMode = linearOpMode;
    }


    // This allows a regular Opmode to use this
    public Config(OpMode opmode) {

        this.opmode = opmode;
    }


    // ================================== PUBLIC VARIABLES =======================================
    // These variables are for saving a pose at the end of auto and send it to the start of Teleop

    public static Pose savedPose = null;
    public static int lastAutoRun = 0;


    // LaunchTimer is for my launching sequence, so when we launch it waits a certain amount
    // of time.


    public ElapsedTime launchTimer = new ElapsedTime();
    HardwareMap hwMap;
    public double lastCalcDistance = 0;
    public double idleVelocity = 1000;
    private boolean manualLaunching = false;


    // ================================== INTAKE FUNCTIONS =======================================
    /* For the intake, we made functions, so then when we are trying to run the intake, all we have
    do is call robot.intakeIn(0); This is a super simple way and can easily change the power or
    speed the motor is running. This is a huge help, so than I only have to change it here and not
    in every single opmode.  */

    public void intakeIn() {
        intake.setVelocity(1180);
        kicker.setPower(1);
    }

    public void intakeOut() {
        intake.setVelocity(-1180);
        kicker.setPower(-1);
    }

    public void intakeStop() {
        intake.setVelocity(0);
        kicker.setPower(0);
    }


    // ================================= SERVO FUNCTIONS ==========================================
    // These functions are the same as the motors, but these are servos. The only difference is that
    // there values are between 0 and 1.


    public void wallOpen() {
        wall.setPosition(.39);
    }

    public void wallClose() {
        wall.setPosition(.53);
    }


    // =============================== LAUNCH FUNCTIONS ===========================================


    public void blueLaunchThreeUpdater(Follower follower) {

        if (!launching) {

            return;
        }


        double distance = blueGetDistanceFromGoal(follower.pose());
        double velocity = calculateLaunchVelocity(distance);
        lastCalcDistance = distance;


        switch (launchSequenceStep) {

            case 1:
                launcher.setVelocity(velocity);
                launcher2.setVelocity(velocity);
                launchSequenceStep++;
                break;
            case 2:
                if (launcher.getVelocity() > velocity - 20) {
                    launcher.setVelocity(velocity);
                    launcher2.setVelocity(velocity);
                    wallOpen();
                    intakeIn();
                    launchTimer.reset();
                    launchSequenceStep++;
                }
                break;
            case 3:
                if (launchTimer.seconds() > 1.75) {
                    intakeStop();
                    wallClose();
                    launcher.setVelocity(idleVelocity);
                    launcher2.setVelocity(idleVelocity);
                    launchSequenceStep = 1;
                    launching = false;
                }
                break;
        }

    }
    public void manualLaunchThreeUpdater() {

        if (!manualLaunching) {

            return;
        }


        double velocity = 1140;



        switch (launchSequenceStep) {

            case 1:
                launcher.setVelocity(velocity);
                launcher2.setVelocity(velocity);
                launchSequenceStep++;
                break;
            case 2:
                if (launcher.getVelocity() > velocity - 20) {
                    launcher.setVelocity(velocity);
                    launcher2.setVelocity(velocity);
                    wallOpen();
                    intakeIn();
                    launchTimer.reset();
                    launchSequenceStep++;
                }
                break;
            case 3:
                if (launchTimer.seconds() > 1.75) {
                    intakeStop();
                    wallClose();
                    launcher.setVelocity(idleVelocity);
                    launcher2.setVelocity(idleVelocity);
                    launchSequenceStep = 1;
                    manualLaunching = false;
                }
                break;
        }

    }

    public void redLaunchThreeUpdater(Follower follower) {

        if (!launching) {

            return;
        }


        double velocity = calculateLaunchVelocity(redGetDistanceFromGoal(follower.pose()));


        switch (launchSequenceStep) {

            case 1:
                launcher.setVelocity(velocity);
                launcher2.setVelocity(velocity);
                launchSequenceStep++;
                break;
            case 2:
                if (launcher.getVelocity() > velocity - 20) {
                    wallOpen();
                    intakeIn();
                    launchTimer.reset();
                    launchSequenceStep++;
                }
                break;
            case 3:
                if (launchTimer.seconds() > 1.75) {
                    intakeStop();
                    wallClose();
                    launcher.setVelocity(idleVelocity);
                    launcher2.setVelocity(idleVelocity);
                    launchSequenceStep = 1;
                    launching = false;
                }
                break;
        }

    }

    public boolean getLaunchStatus() {

        return launching;
    }

    public void startLaunch() {

        launching = true;
    }

    public void startManualLaunch() {

        manualLaunching = true;
    }

    public void stopManualLaunch() {

        manualLaunching = false;
        intakeStop();
        wallClose();
        launcher.setVelocity(idleVelocity);
        launcher2.setVelocity(idleVelocity);
    }

    public void stopLaunch() {

        launching = false;
        intakeStop();
        wallClose();
        launcher.setVelocity(idleVelocity);
        launcher2.setVelocity(idleVelocity);
    }


    /* We used Deismos graphing calculator to graph our launching velocity, and this is where we
    put in the equation. Then, we just past in the distance away from the goal we are, and there
    you go, you have an automatic launching velocity calculations, that makes it super simple to
    make it into an automatic launch sequence in a press of a button. :) */

    public double calculateLaunchVelocity(double distance) {
        double x = distance;
        double velocity = (-0.000234073 * x * x * x + (0.101791 * x * x - 8.57328 * x + 1300));
        return velocity;
    }


    // ============================== HARDWARE ===================================================
    public DcMotorEx frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor;
    public DcMotorEx intake, kicker, launcher, launcher2;
    public Servo vision, vision1, wall;
    public Limelight3A limelight;
    public ColorSensor intakeSensor;
    public ColorSensor transferSensor;


    // =============================== INTAKE & LAUNCH VARIBLES ===================================
    public boolean intakeIsOpen;
    public boolean intaking;
    private boolean launching = false;
    private int launchSequenceStep = 1;


    // ================= LIGHT VALUES ============================================================
    // when I set these to a number, they turn a certain color. You can take a look at the GoBilda
    // website and go to the indicators and check what numbers are what colors. Remember that
    // these values must be between 0 and 1.
    public final double OFF = 0.0;
    public final double GREEN = 0.475;
    public final double ORANGE = 0.333;


    public boolean intakeFull = false;
    public final double COLORIntake_THRESHOLD = 70;
    public final double COLORTransfer_THRESHOLD = 155;


    // ================== Intake Initialization ==================================================
    public long intakeStopTime = 0;
    public boolean wasIntaking = false;
    public final long REVERSE_TIME_MS = 100;


    // ================== GOAL HEADING & DISTANCES ===============================================

    /* This is where we get the distance away from the goal and calculate it. First, we use find
    our sides of our triangle, then we use inverse tangent to calculate the angle that we need the
    robot to turn, and there you go. We will use aim assist to rotate using these headings.
     */

    public double blueGetGoalHeading(Pose robotPose) {
        Pose goal = new Pose(5, 127.5);
        double opposite = goal.y() - robotPose.y();
        double adjacent = robotPose.x() - goal.x();
        double heading = Math.PI - Math.atan2(opposite, adjacent);
        return heading;
    }


    /* For the distance, it is not that much different. We use the same method, and find the two
    sides, opposite and adjacent. Then, once we figure that out, we just need to find the
    hypotenuse, or distance away from the triangle by using the Pythagorean Theorem. This gets us
    our distance away from the goal and is very simple to use for automatic launching, for telling
    how far away we are.  */

    public double blueGetDistanceFromGoal(Pose robotPose) {
        double opposite = 144 - robotPose.y();
        double adjacent = robotPose.x();
        double hypot = Math.sqrt((opposite * opposite) + (adjacent * adjacent));
        return hypot;
    }


    public double redGetGoalHeading(Pose robotPose) {
        Pose goal = new Pose(144, 138.5);
        double opposite = goal.y() - robotPose.y();
        double adjacent = 144 - robotPose.x();
        double heading = Math.atan2(opposite, adjacent);
        return heading;
    }


    public double redGetDistanceFromGoal(Pose robotPose) {
        double opposite = 144 - robotPose.y();
        double adjacent = 144 - robotPose.x();
        double hypot = Math.sqrt((opposite * opposite) + (adjacent * adjacent));
        return hypot;
    }


    // ================================ ANGLE WRAP ===============================================
    // Angle wrap is so that say if I spin the robot ten times to the left, and I want it to go to
    // the angle 270, then it would have to spin the way it did ten times before getting to the
    // desired position. What angle wrap does is that it turns whatever angle it is at, and makes
    // it between 360 degrees and 0 degrees. This allows the robot to only turn once without any
    // extra circles or anything like that.

    public static double angleWrap(double angle) {
        while (angle > Math.PI) angle -= 2 * Math.PI;
        while (angle < -Math.PI) angle += 2 * Math.PI;
        return angle;
    }


    // ================================== AUTONOMOUS POSITIONS =====================================


    public final Pose blueStartFar = new Pose(55, 8.39, Math.toRadians(90));
    public final Pose blueFarScorePose = new Pose(52, 11, Math.toRadians(115.5));
    public final Pose blueFarPark = new Pose(35.6, 12.85, Math.toRadians(90));
    public final Pose blueFarGrabFromHumanPlayerZoneOffTheWallSetup = new Pose(16.6, 23, Math.toRadians(-110));
    public final Pose blueFarGrabFromHumanPlayerZoneOffTheWall = new Pose(16, 2.2, Math.toRadians(-110));
    public final Pose blueFarGrabFromHumanPlayerZoneOffTheWallGetBack = new Pose(16.6, 2.5, Math.toRadians(-89));
    public final Pose blueFarGrabFromHumanPlayerZoneOffTheWallFacingHumanPlayerSetup = new Pose(35.2, 2, Math.toRadians(-160));
    public final Pose blueFarGrabFromHumanPlayerZoneOffTheWallFacingHumanPlayer = new Pose(14, 1.5, Math.toRadians(-160));
    public final Pose blueFarGrabFromHumanPlayerZoneOffTheWallFacingHumanPlayerTowardsGoalMoreSetup = new Pose(37.6, 15, Math.toRadians(173));
    public final Pose blueFarGrabFromHumanPlayerZoneOffTheWallFacingHumanPlayerTowardsGoalMore = new Pose(10.3, 15, Math.toRadians(173));
    public final Pose blueStartClose = new Pose(16, 115.7435, Math.toRadians(139));
    public final Pose blueScorePose = new Pose(45.5, 85, Math.toRadians(132));
    public final Pose blueScorePose2 = new Pose(53, 105, Math.toRadians(152.5));
    public final Pose bluePickup1Pose = new Pose(15, 75.5, Math.toRadians(180));
    public final Pose blueSetup1Pose = new Pose(40, 75.5, Math.toRadians(180));
    public final Pose bluePickup2Pose = new Pose(6.5, 51.5, Math.toRadians(180));
    public final Pose blueSetup2Pose = new Pose(43.576, 51.5, Math.toRadians(180));
    public final Pose blueGate = new Pose(19, 56, Math.toRadians(-90));
    public final Pose blueGateFacingGoalSetup = new Pose(19, 75.5, Math.toRadians(180));
    public final Pose blueGateFacingGoal = new Pose(13.5, 74.5, Math.toRadians(65));
    public final Pose blueAutoEnd = new Pose(30, 58, Math.toRadians(-90));
    public final Pose blueGateSetupPose = new Pose(17, 49.7, Math.toRadians(179.697));
    public final Pose bluePickup3Pose = new Pose(6.5, 27, Math.toRadians(179));
    public final Pose blueSetup3Pose = new Pose(43, 27, Math.toRadians(179));
    public final Pose blueGateHold = new Pose(9.5, 57.2, Math.toRadians(155.28));
    public final Pose blueGateHarvest = new Pose(7.75, 56.5, Math.toRadians(123.5));
    public final Pose blueGateHarvestSetup = new Pose(36.394, 54.5, Math.toRadians(155.7));


    public final Pose redStartFar = new Pose(89, 8.39, Math.toRadians(90));
    public final Pose redFarScorePose = new Pose(87, 11, Math.toRadians(65));
    public final Pose redFarScorePose2 = new Pose(87, 11, Math.toRadians(65));
    public final Pose redFarPark = new Pose(110, 10, Math.toRadians(90));
    public final Pose redFarGrabFromHumanPlayerZoneOffTheWallSetup = new Pose(144, 21, Math.toRadians(-70));
    public final Pose redFarGrabFromHumanPlayerZoneOffTheWall = new Pose(144, 1.5, Math.toRadians(-70));
    public final Pose redFarGrabFromHumanPlayerZoneOffTheWallGetBack = new Pose(144, 2.5, Math.toRadians(-90));
    public final Pose redFarGrabFromHumanPlayerZoneOffTheWallFacingHumanPlayerSetup = new Pose(114.7, 10, Math.toRadians(-20));
    public final Pose redFarGrabFromHumanPlayerZoneOffTheWallFacingHumanPlayer = new Pose(144, 9, Math.toRadians(-20));
    public final Pose redFarGrabFromHumanPlayerZoneOffTheWallFacingHumanPlayerTowardsGoalMoreSetup = new Pose(110, 16, Math.toRadians(10));
    public final Pose redFarGrabFromHumanPlayerZoneOffTheWallFacingHumanPlayerTowardsGoalMore = new Pose(142, 18, Math.toRadians(10));
    public final Pose redStartClose = new Pose(129.2095, 122.4811, Math.toRadians(39.3531));
    public final Pose redScorePose = new Pose(92, 92, Math.toRadians(41));
    public final Pose redScorePose2 = new Pose(91, 104, Math.toRadians(34));
    public final Pose redPickup1Pose = new Pose(134, 82.2643, Math.toRadians(0.6));
    public final Pose redSetup1Pose = new Pose(107.6, 81.6, Math.toRadians(1.33));
    public final Pose redPickup2Pose = new Pose(141, 57.5, Math.toRadians(.08));
    public final Pose redSetup2Pose = new Pose(107.6, 58, Math.toRadians(.08));
    public final Pose redSetupGate = new Pose(133, 58, Math.toRadians(0));
    public final Pose redGateFacingParkingZone = new Pose(139, 61.25, Math.toRadians(-100));
    public final Pose redGateFacingParkingZone2 = new Pose(137, 59, Math.toRadians(-90));
    public final Pose redGateFacingGoal = new Pose(134, 76, Math.toRadians(90));
    public final Pose redPickup3Pose = new Pose(142, 34.5, Math.toRadians(.8));
    public final Pose redSetup3Pose = new Pose(107.6, 34.5, Math.toRadians(.8));
    public final Pose redAutoEnd = new Pose(129, 57.8, Math.toRadians(-90));
    public final Pose redGateHold = new Pose(137.4, 61.134, Math.toRadians(37));
    public final Pose redGateHarvest = new Pose(135.6, 60, Math.toRadians(46.5));
    public final Pose redGateHarvestSetup = new Pose(117.33, 60.8657, Math.toRadians(39.38));


// ===================================== INITIALIZATION ==========================================


    public void init() {


        if (linearOpMode != null) {
            hwMap = linearOpMode.hardwareMap;
        } else {
            hwMap = opmode.hardwareMap;
        }


        // This is where we have to get stuff from the Driver Station
        frontLeftMotor = hwMap.get(DcMotorEx.class, "fl");
        backLeftMotor = hwMap.get(DcMotorEx.class, "bl");
        frontRightMotor = hwMap.get(DcMotorEx.class, "fr");
        backRightMotor = hwMap.get(DcMotorEx.class, "br");


        intake = hwMap.get(DcMotorEx.class, "intake");
        kicker = hwMap.get(DcMotorEx.class, "kicker");
        launcher = hwMap.get(DcMotorEx.class, "launcher");
        launcher2 = hwMap.get(DcMotorEx.class, "launcher2");


        vision = hwMap.get(Servo.class, "vision");
        vision1 = hwMap.get(Servo.class, "vision1");
        wall = hwMap.get(Servo.class, "wall");
        intakeSensor = hwMap.get(ColorSensor.class, "color");
        transferSensor = hwMap.get(ColorSensor.class, "sensor");
        limelight = hwMap.get(Limelight3A.class, "limelight");


        // Limelight Initialization
        limelight.start();


        // Motor Directions
        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        intake.setDirection(DcMotorSimple.Direction.FORWARD);
        kicker.setDirection(DcMotorSimple.Direction.REVERSE);
        launcher.setDirection(DcMotorSimple.Direction.REVERSE);


        // Wheel Brakes
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        // Launcher Initialization
        PIDFCoefficients pidf = new PIDFCoefficients(55, 0, 0, 15.5);
        launcher.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, pidf);
        launcher2.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, pidf);


    }
}