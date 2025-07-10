package cineconecta.cineconecta.dto;

import cineconecta.cineconecta.model.Users.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentsRequest {
    private String text;
    private String user;
}
