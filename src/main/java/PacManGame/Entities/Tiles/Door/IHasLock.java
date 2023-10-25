package PacManGame.Entities.Tiles.Door;

/**
 * Interface, which provides mechanism lock and its key
 */
public interface IHasLock {
    /**
     * Method for processing if entity has key from lock.
     * @param entityWithKey entity which May has key
     * @return true if entity has key from lock
     */
    boolean canEnter(IHasKey entityWithKey);
    Key getLockKey();
}
