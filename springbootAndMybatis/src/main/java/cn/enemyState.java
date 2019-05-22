package cn;

import robocode.AdvancedRobot;
import robocode.Rules;
import robocode.ScannedRobotEvent;

class enemyState
{
    public double headingRadian = 0.0D;
    public double bearingRadian = 0.0D;
    public double distance = 0.0D;
    public double absoluteBearing = 0.0D;
    public double x = 0.0D;
    public double y = 0.0D;
    public double velocity = 0.0D;
    public double energy = 100.0D;

    public double lastEnemyHeading = 0.0D;
    public int thisStep = 0;

    public void update(ScannedRobotEvent e, AdvancedRobot me)
    {
        this.headingRadian = e.getHeadingRadians();
        this.bearingRadian = e.getBearingRadians();
        this.distance = e.getDistance();
        this.absoluteBearing = (this.bearingRadian + me.getHeadingRadians());
        this.x = (me.getX() + Math.sin(this.absoluteBearing) * this.distance);
        this.y = (me.getY() + Math.cos(this.absoluteBearing) * this.distance);
        this.velocity = e.getVelocity();
        this.energy = e.getEnergy();

        this.thisStep = encode(this.headingRadian - this.lastEnemyHeading, this.velocity);
        this.lastEnemyHeading = this.headingRadian;
    }

    public static int encode(double dh, double v) {
        if (Math.abs(dh) > Rules.MAX_TURN_RATE_RADIANS) {
            return 65535;
        }

        int dhCode = (int)Math.rint(Math.toDegrees(dh)) + 10;
        int vCode = (int)Math.rint(v + 8.0D);
        return (char)(17 * dhCode + vCode);
    }

    public void decode(int symbol) {
        this.headingRadian += Math.toRadians(symbol / 17 - 10);
        this.velocity = (symbol % 17 - 8);
    }
}