package cn;

import robocode.*;
import robocode.util.Utils;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MissXia extends TeamRobot {

    static Boolean hithithit = Boolean.valueOf(false);
    enemyState enemy = new enemyState();
    private static final int MAX_PATTERN_LENGTH = 30;
    private static Map<String, int[]> matcher = new HashMap<>(40000);
    private static String enemyHistory;
    private static double FIRE_POWER = 3.0D;
    private static double FIRE_SPEED = Rules.getBulletSpeed(FIRE_POWER);
    private static List<Point2D.Double> predictions = new ArrayList<>();
    static final double BASE_MOVEMENT = 180.0D;
    static final double BASE_TURN = 2.094395102393195D;
    static double movement;

    public void run()
    {
        // 控制火炮手否向下锁定
        setAdjustGunForRobotTurn(true);
        // 控制雷达是否向下锁定
        setAdjustRadarForGunTurn(true);

        setBodyColor(Color.BLACK);
        setGunColor(Color.BLACK);
        setRadarColor(Color.BLACK);
        setScanColor(Color.BLACK);
        enemyHistory = "";
        movement = (1.0D / 0.0D);
        // 右转弯角的弧度
        setTurnRadarRight(400.0D);
        while (true) {
            scan();
            if (getDistanceRemaining() == 0.0D) {
                // 移动多少像素
                setAhead(MissXia.movement = -movement);
                // 弧度旋转反射镜
                setTurnRightRadians(2.094395102393195D);
                hithithit = Boolean.valueOf(false);
            }
        }
    }

    // 当机器人撞墙的时候
    public void onHitWall(HitWallEvent e)
    {
        if (Math.abs(movement) > 180.0D)
            movement = 180.0D;
    }

    // 当扫描的机器人死亡
    public void onRobotDeath(RobotDeathEvent e)
    {
        // 旋转雷达反射镜
        setTurnRadarRight(400.0D);
    }

    // 当机器人当机器人被击中时
    public void onHitByBullet(HitByBulletEvent e) {
        // 旋转雷达反射镜
        setTurnRadarRight(400.0D);
    }

    // 当你机器人和其他机器人相撞时
    public void onHitRobot(HitRobotEvent e) {
        if (!hithithit.booleanValue()) {
            double absoluteBearing = e.getBearingRadians() + getHeadingRadians();
            // 转弯角弧度
            turnRadarRightRadians(Utils.normalRelativeAngle(absoluteBearing - getRadarHeadingRadians()));
            hithithit = Boolean.valueOf(true);
        }
    }

    // 当用雷达扫描到其他机器人
    public void onScannedRobot(ScannedRobotEvent e)
    {
        this.enemy.update(e, this);

        if ((getGunTurnRemaining() == 0.0D) && (getEnergy() > 1.0D)) {
            smartFire();
        }

        trackHim();

        if (this.enemy.thisStep == 65535) {
            return;
        }
        record(this.enemy.thisStep);
        enemyHistory = (char)this.enemy.thisStep + enemyHistory;

        predictions.clear();
        Point2D.Double myP = new Point2D.Double(getX(), getY());
        Point2D.Double enemyP = project(myP, this.enemy.absoluteBearing, e.getDistance());
        String pattern = enemyHistory;
        for (double d = 0.0D; d < myP.distance(enemyP); d += FIRE_SPEED) {
            int nextStep = predict(pattern);
            this.enemy.decode(nextStep);
            enemyP = project(enemyP, this.enemy.headingRadian, this.enemy.velocity);
            predictions.add(enemyP);
            pattern = (char)nextStep + pattern;
        }

        this.enemy.absoluteBearing = Math.atan2(enemyP.x - myP.x, enemyP.y - myP.y);
        double gunTurn = this.enemy.absoluteBearing - getGunHeadingRadians();
        setTurnGunRightRadians(Utils.normalRelativeAngle(gunTurn));
    }

    // 火力计算设置
    public void smartFire()
    {
        FIRE_POWER = Math.min(Math.min(getEnergy() / 6.0D, 1000.0D / this.enemy.distance), this.enemy.energy / 3.0D);
        FIRE_SPEED = Rules.getBulletSpeed(FIRE_POWER);
        setFire(FIRE_POWER);
    }

    // 跟踪它
    public void trackHim()
    {
        double RadarOffset = Utils.normalRelativeAngle(this.enemy.absoluteBearing - getRadarHeadingRadians());
        setTurnRadarRightRadians(RadarOffset * 1.2D);
    }

    // 记录敌人位置
    private void record(int thisStep) {
        int maxLength = Math.min(30, enemyHistory.length());
        for (int i = 0; i <= maxLength; i++) {
            String pattern = enemyHistory.substring(0, i);
            int[] frequencies = (int[])matcher.get(pattern);
            if (frequencies == null)
            {
                frequencies = new int[357];
                matcher.put(pattern, frequencies);
            }
            frequencies[thisStep] += 1;
        }
    }

    // 预测它的位置
    private int predict(String pattern)
    {
        int[] frequencies = null;
        for (int patternLength = Math.min(pattern.length(), 30); frequencies == null; patternLength--) {
            frequencies = (int[])matcher.get(pattern.substring(0, patternLength));
        }
        int nextTick = 0;
        for (int i = 1; i < frequencies.length; i++) {
            if (frequencies[nextTick] < frequencies[i]) {
                nextTick = i;
            }
        }
        return nextTick;
    }

    private static Point2D.Double project(Point2D.Double p, double angle, double distance)
    {
        double x = p.x + distance * Math.sin(angle);
        double y = p.y + distance * Math.cos(angle);
        return new Point2D.Double(x, y);
    }

}
