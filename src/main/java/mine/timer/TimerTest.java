package mine.timer;

import java.io.IOException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TimerTest
{
    @Scheduled(fixedRate=2000L)
    public void timer1()
            throws IOException
    {
    }
}