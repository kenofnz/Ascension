package blockfighter.server;

import blockfighter.shared.Globals;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

public class Core {

    public static final ExecutorService SHARED_THREADPOOL = Executors.newFixedThreadPool((Byte) Globals.ServerConfig.NUM_THREADS.getValue(),
            new BasicThreadFactory.Builder()
                    .namingPattern("Server-Shared-Thread-%d")
                    .daemon(true)
                    .priority(Thread.NORM_PRIORITY)
                    .build());

    public static final ScheduledExecutorService SHARED_SCHEDULED_THREADPOOL = Executors.newScheduledThreadPool((Byte) Globals.ServerConfig.NUM_SCHEDULED_THREADS.getValue(),
            new BasicThreadFactory.Builder()
                    .namingPattern("Server-Shared-Scheduled-Thread-%d")
                    .daemon(false)
                    .priority(Thread.NORM_PRIORITY)
                    .build());
}
