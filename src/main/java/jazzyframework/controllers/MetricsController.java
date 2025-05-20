/**
 * Controller for providing metrics about the server performance.
 * This controller provides a simple text report of request counts and response times.
 */
package jazzyframework.controllers;

import jazzyframework.core.Metrics;

public class MetricsController {
    
    /**
     * Returns a text report of the server metrics.
     * Includes total requests, failed requests, and average response time.
     * 
     * @return A text string containing the metrics report
     */
    public String getMetrics() {
        int requests = Metrics.totalRequests.get();
        int errors = Metrics.failedRequests.get();
        long avgResponse = requests > 0 ? Metrics.totalResponseTime.get() / requests : 0;
        return "Total Requests: " + requests + "\n" +
               "Total Failed Requests: " + errors + "\n" +
               "Average Response Time (ms): " + avgResponse + "\n";
    }
}
