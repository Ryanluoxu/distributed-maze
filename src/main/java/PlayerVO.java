public class PlayerVO {
    private String ip;
    private Integer port;
    private String playerId;
    private boolean isPrimaryServer;
    private boolean isBackupServer;

    public PlayerVO(String playerId) {
        this.playerId = playerId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public boolean getPrimaryServer() {
        return isPrimaryServer;
    }

    public void setPrimaryServer(boolean primaryServer) {
        isPrimaryServer = primaryServer;
    }

    public boolean getBackupServer() {
        return isBackupServer;
    }

    public void setBackupServer(boolean backupServer) {
        isBackupServer = backupServer;
    }

    @Override
    public String toString() {
        return "PlayerVO{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", playerId='" + playerId + '\'' +
                ", isPrimaryServer=" + isPrimaryServer +
                ", isBackupServer=" + isBackupServer +
                '}';
    }
}
