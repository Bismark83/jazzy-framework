/**
 * Configuration manager for the Jazzy Framework.
 * Loads configuration from application.properties file 
 * and provides access to configuration values.
 */
package jazzyframework.core;

public class Config {
    private boolean enableMetrics = true;
    private int serverPort = 8080;

    public boolean isEnableMetrics() {
        return enableMetrics;
    }

    public void setEnableMetrics(boolean enableMetrics) {
        this.enableMetrics = enableMetrics;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }
}
