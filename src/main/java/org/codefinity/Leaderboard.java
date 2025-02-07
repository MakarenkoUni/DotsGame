package org.codefinity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Leaderboard {
    public class PlayerPublic{
        String name;
        int maxScore;

        public PlayerPublic(String name, Integer maxScore) {
            this.name = name;
            this.maxScore = maxScore;
        }
        public String getName() {
            return name;
        }

        public int getMaxScore() {
            return maxScore;
        }

    }
    @Autowired
    private PlayerRepository playerRepository;

    public List<PlayerPublic> getTopPlayers() {
        List<Object[]> rawData = playerRepository.getLeaderboard();

        return rawData.stream().map(obj ->
                new PlayerPublic(
                        (String) obj[0],
                        (int) obj[1]
                )
        ).collect(Collectors.toList());
    }
}

