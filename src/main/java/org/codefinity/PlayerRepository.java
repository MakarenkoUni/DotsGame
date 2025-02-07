package org.codefinity;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
@Repository
public interface PlayerRepository extends CrudRepository<Player, Integer> {
    @Query(value = "SELECT * FROM show_leaderboard()", nativeQuery = true)
    List<Object[]> getLeaderboard();
    @Modifying
    @Transactional
    @Query(value = "CALL update_max_score(?1, ?2)", nativeQuery = true)
    void updateMaxScore(String name, int score);
    @Query(value = "SELECT add_player(?1, ?2)", nativeQuery = true)
    void addPlayer(String name, String password);
    @Query(value = "Select login_player(?1, ?2)", nativeQuery = true)
    String loginPlayer(String name, String password);
}
