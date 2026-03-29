package ru.session.model;

import jakarta.persistence.*;
import lombok.*;
import ru.user.model.User;

import java.time.LocalDateTime;
import java.util.Arrays;
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
    private LocalDateTime createdAt;

    public Session(User user, User otherUser) {
        this.user = user;
        this.otherUser = otherUser;
        initializeMaps();
    }

    //Инициализация пустых карт
    private void initializeMaps() {
        int[][] emptyMap = new int[10][10];
        setUserMap(emptyMap);
        setOtherUserMap(emptyMap);
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

        String[] rows = userMapJson.replaceAll("^\\[|\\]$", "").split("\\],\\[");
        int[][] map = new int[rows.length][];

        for (int i = 0; i < rows.length; i++) {
            String[] values = rows[i].split(",");
            map[i] = new int[values.length];
            for (int j = 0; j < values.length; j++) {
                map[i][j] = Integer.parseInt(values[j]);
            }
        }
        return map;
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

        String[] rows = otherUserMapJson.replaceAll("^\\[|\\]$", "").split("\\],\\[");
        int[][] map = new int[rows.length][];

        for (int i = 0; i < rows.length; i++) {
            String[] values = rows[i].split(",");
            map[i] = new int[values.length];
            for (int j = 0; j < values.length; j++) {
                map[i][j] = Integer.parseInt(values[j]);
            }
        }
        return map;
    }
}
