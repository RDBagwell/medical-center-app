package helper;

import java.util.Timer;
import java.util.TimerTask;

public class AutoLogout {
    private static final long INACTIVITY_TIMEOUT =  30 * 60 * 1000; // 30 minutes
    private Timer timer;

    public AutoLogout(){
        timer = new Timer(true);
        System.out.println("Started");
    }

    public void startLogoutTimer(){
        timer.schedule(new LogoutTask(), INACTIVITY_TIMEOUT);
    }

    public void resetLogoutTimer(){
        timer.cancel();
        timer = new Timer();
        timer.schedule(new LogoutTask(), INACTIVITY_TIMEOUT);
    }

    private static class LogoutTask extends TimerTask{
        @Override
        public void run() {
            System.out.println("User logged out due to inactivity.");
            System.exit(0);
        }
    }
}
