
public class Clock
{
    private int time;
    
    public Clock(int hours)
    {
        time = hours * 60;
    }
    
    /**
     * Gets the hour part of time left
     */
    public int getHours()
    {
        return time/60;
    }
    
    /**
     * Gets the minute part of time left
     */
    public int getMinutes()
    {
        return time%60;
    }
    
    /**
     * Gets total amount of minutes left
     */
    public int getTime()
    {
        return time;
    }
    
    /**
     * 
     * @param minutes
     * @throws GameOver
     */
    public void passTime(int minutes)
    {
        boolean plentyTime = time > 60;
        time -= minutes;
        if (time <= 0) {
            throw new GameOver("The time is up. The bomb went off, destroying everything\nand everyone in the school.");
        }
        if (plentyTime && time <= 60) {
            System.out.println("Time is running out! Only one hour left!");
        }
    }
}
