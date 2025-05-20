package jazzyframework.controllers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jazzyframework.core.Metrics;

/**
 * Tests for the MetricsController class.
 */
public class MetricsControllerTest {
    
    /**
     * Setup method that runs before each test to reset metrics.
     */
    @BeforeEach
    public void setup() {
        // Reset metrics to ensure consistent test results
        Metrics.totalRequests.set(0);
        Metrics.successfulRequests.set(0);
        Metrics.failedRequests.set(0);
        Metrics.totalResponseTime.set(0);
    }
    
    /**
     * Test for getting metrics with no requests.
     */
    @Test
    public void testGetMetricsWithNoRequests() {
        // Create a new controller instance for this test
        MetricsController controller = new MetricsController();
        
        // When
        String result = controller.getMetrics();
        
        // Then
        assertTrue(result.contains("Total Requests: 0"), "Should contain total requests line");
        assertTrue(result.contains("Total Failed Requests: 0"), "Should contain failed requests line");
        assertTrue(result.contains("Average Response Time (ms): 0"), "Should contain average response time line");
    }
    
    /**
     * Test for getting metrics with some requests.
     */
    @Test
    public void testGetMetricsWithRequests() {
        // Create a new controller instance for this test
        MetricsController controller = new MetricsController();
        
        // Given
        Metrics.totalRequests.set(10);
        Metrics.failedRequests.set(2);
        Metrics.totalResponseTime.set(500);
        
        // When
        String result = controller.getMetrics();
        
        // Then
        assertTrue(result.contains("Total Requests: 10"), "Should contain total requests line");
        assertTrue(result.contains("Total Failed Requests: 2"), "Should contain failed requests line");
        assertTrue(result.contains("Average Response Time (ms): 50"), "Should contain average response time line");
    }
    
    /**
     * Test for getting metrics with failed requests.
     */
    @Test
    public void testGetMetricsWithFailedRequests() {
        // Create a new controller instance for this test
        MetricsController controller = new MetricsController();
        
        // Given
        Metrics.totalRequests.set(5);
        Metrics.failedRequests.set(5);
        Metrics.totalResponseTime.set(100);
        
        // When
        String result = controller.getMetrics();
        
        // Then
        assertTrue(result.contains("Total Requests: 5"), "Should contain total requests line");
        assertTrue(result.contains("Total Failed Requests: 5"), "Should contain failed requests line");
        assertTrue(result.contains("Average Response Time (ms): 20"), "Should contain average response time line");
    }
} 