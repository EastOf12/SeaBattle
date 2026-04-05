package ru.session.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import ru.session.SessionStatus;
import ru.user.model.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "other_user_id", nullable = false)
    private User otherUser;

    @Column(name = "user_map", columnDefinition = "jsonb")
    private String userMapJson;

    @Column(name = "other_user_map", columnDefinition = "jsonb")
    private String otherUserMapJson;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "current_turn_user_id")
    private Long userWalkId;

    @Column(name = "status")
    private String status = String.valueOf(SessionStatus.LOBBY);

    @Column(name = "winner_id")
    private Long winnerId;

    public Session(User user, User otherUser) {
        this.user = user;
        this.otherUser = otherUser;
        initialize();
    }

    //Инициализация сессии
    private void initialize() {
        int[][] emptyMap = new int[10][10];
        setUserMap(emptyMap);
        setOtherUserMap(emptyMap);

        // 🔥 Случайно выбираем, кто ходит первым: 50/50
        boolean userGoesFirst = new Random().nextBoolean();
        this.userWalkId = userGoesFirst ? user.getId() : otherUser.getId();
    }

    //Конвертер: int[][] → JSON String
    public void setUserMap(int[][] map) {
        this.userMapJson = Arrays.stream(map)
                .map(row -> Arrays.stream(row)
                        .mapToObj(String::valueOf)
                        .collect(Collectors.joining(",", "[", "]")))
                .collect(Collectors.joining(",", "[", "]"));
    }

    //Конвертер: JSON String → int[][]
    public int[][] getUserMap() {
        if (userMapJson == null || userMapJson.isEmpty()) {
            return new int[10][10];
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(userMapJson, int[][].class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse userMapJson", e);
        }
    }

    //Конвертер: int[][] → JSON String
    public void setOtherUserMap(int[][] map) {
        this.otherUserMapJson = Arrays.stream(map)
                .map(row -> Arrays.stream(row)
                        .mapToObj(String::valueOf)
                        .collect(Collectors.joining(",", "[", "]")))
                .collect(Collectors.joining(",", "[", "]"));
    }

    //Конвертер: JSON String → int[][]
    public int[][] getOtherUserMap() {
        if (otherUserMapJson == null || otherUserMapJson.isEmpty()) {
            return new int[10][10];
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(otherUserMapJson, int[][].class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse otherUserMapJson", e);
        }
    }

    // Меняем очередность хода
    public void switchTurn() {
        userWalkId = userWalkId.equals(user.getId()) ? otherUser.getId() : user.getId();
    }
}
