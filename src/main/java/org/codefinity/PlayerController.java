package org.codefinity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlayerController {
    @Autowired
    private PlayerRepository playerRepository;
    @Transactional
    public String login(String name, String password){
        return playerRepository.loginPlayer(name, password).toString();
    }
    @Transactional
    public String registerPlayer(String name, String password) {
        try {
            playerRepository.addPlayer(name, password);
            return "Player " + name + " signed up successfully!";
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            e.printStackTrace();
            if (errorMessage.contains("Error: Player")) {
                return errorMessage.substring(errorMessage.indexOf("Error:"));
            }

            return "Error: Database issue occurred!";
        }
    }
    @Transactional
    public void updateMaxScore(String playerName, int score) {
        try {
            playerRepository.updateMaxScore(playerName, score);
            System.out.println("✅ Score updated successfully for: " + playerName);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error updating max score: " + e.getMessage());
        }
    }





}
