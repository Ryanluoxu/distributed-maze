import java.util.List;

/**
 * 此接口仅用来定义 Game 的本地方法，Game 可以不必 implements 该接口
 */
public interface GameLocal {

    /**
     * 首位玩家 tracker.getGameInfo 之后：initGame 得到 gameState
     * --JH
     */
    GameStateVO initGame(int N, int K, List<PlayerVO> playerList);

    /**
     * 通过比对 playerList 和自身的 playerId，判断当前玩家是否是 pServer 或者 bServer
     * -- LX
     */
    boolean isPrimaryServer();
    boolean isBackupServer();

    /**
     * 每0.5s一次的定时任务。
     * isPrimaryServer：检查其他玩家
     * isBackupServer：检查首位玩家（pServer）
     * 普通玩家跳过。
     * -- LW
     */
    void ScheduleCheck();

}
