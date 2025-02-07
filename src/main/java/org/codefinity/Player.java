package org.codefinity;

import jakarta.persistence.*;

@Entity
@Table(name = "\"Players\"")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;  // Changed from Long to Integer

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;  // Added password field
    @Column(name = "\"maxScore\"")
    private int maxScore;  // Player's high score

    // Getters & Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getMaxScore() { return maxScore; }
    public void setMaxScore(int maxScore) { this.maxScore = maxScore; }
}
