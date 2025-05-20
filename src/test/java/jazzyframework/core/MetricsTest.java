package jazzyframework.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Metrics class.
 */
public class MetricsTest {
    
    /**
     * Reset metrics before each test.
     */
    @BeforeEach
    public void setup() {
        // Reset all counters to exactly 0
        Metrics.totalRequests.set(0);
        Metrics.successfulRequests.set(0);
        Metrics.failedRequests.set(0);
        Metrics.totalResponseTime.set(0);
    }
    
    /**
     * Test for incrementing total requests.
     */
    @Test
    public void testIncrementTotalRequests() {
        // When - reset to ensure we start from 0
        Metrics.totalRequests.set(0);
        Metrics.totalRequests.incrementAndGet();
        Metrics.totalRequests.incrementAndGet();
        
        // Then
        assertEquals(2, Metrics.totalRequests.get(), "Total requests should be 2");
    }
    
    /**
     * Test for incrementing successful requests.
     */
    @Test
    public void testIncrementSuccessfulRequests() {
        // When - reset to ensure we start from 0
        Metrics.successfulRequests.set(0);
        Metrics.successfulRequests.incrementAndGet();
        
        // Then
        assertEquals(1, Metrics.successfulRequests.get(), "Successful requests should be 1");
    }
    
    /**
     * Test for incrementing failed requests.
     */
    @Test
    public void testIncrementFailedRequests() {
        // When - reset to ensure we start from 0
        Metrics.failedRequests.set(0);
        Metrics.failedRequests.incrementAndGet();
        
        // Then
        assertEquals(1, Metrics.failedRequests.get(), "Failed requests should be 1");
    }
    
    /**
     * Test for adding response time.
     */
    @Test
    public void testAddResponseTime() {
        // When - reset to ensure we start from 0
        Metrics.totalResponseTime.set(0);
        Metrics.totalResponseTime.addAndGet(100);
        Metrics.totalResponseTime.addAndGet(200);
        
        // Then
        assertEquals(300, Metrics.totalResponseTime.get(), "Total response time should be 300");
    }
    
    /**
     * Test for calculating average response time.
     */
    @Test
    public void testCalculateAverageResponseTime() {
        // Given
        Metrics.totalRequests.set(2);
        Metrics.totalResponseTime.set(300);
        
        // When
        long averageResponseTime = Metrics.totalResponseTime.get() / Metrics.totalRequests.get();
        
        // Then
        assertEquals(150, averageResponseTime, "Average response time should be 150");
    }
    
    /**
     * Test for handling zero requests when calculating average.
     */
    @Test
    public void testCalculateAverageResponseTimeWithZeroRequests() {
        // Given
        Metrics.totalRequests.set(0);
        Metrics.totalResponseTime.set(300);
        
        // When
        long averageResponseTime = Metrics.totalRequests.get() > 0 
            ? Metrics.totalResponseTime.get() / Metrics.totalRequests.get() 
            : 0;
        
        // Then
        assertEquals(0, averageResponseTime, "Average response time should be 0 if no requests");
    }
} 