/**
 * Collects and provides metrics about the server performance.
 * This class tracks the number of total, successful, and failed requests,
 * as well as the total response time.
 */
package jazzyframework.core;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Metrics {
    public static final AtomicInteger totalRequests = new AtomicInteger(0);
    public static final AtomicInteger successfulRequests = new AtomicInteger(0);
    public static final AtomicInteger failedRequests = new AtomicInteger(0);
    public static final AtomicLong totalResponseTime = new AtomicLong(0);
}
