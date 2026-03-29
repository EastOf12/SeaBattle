package ru.session.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionDto {
    private Long id;
    private Long userId;
    private String userName;
    private Long otherUserId;
    private String otherUserName;
    private String userMapJson;
    private String otherUserMapJson;
    private String createdAt;
}
