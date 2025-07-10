package cineconecta.cineconecta.service.Films;

import cineconecta.cineconecta.controllers.Comments.CommentsResponse;
import cineconecta.cineconecta.dto.CommentsRequest;
import cineconecta.cineconecta.model.Users.Achievements;
import cineconecta.cineconecta.model.Users.Comments;
import cineconecta.cineconecta.model.Users.Profile;
import cineconecta.cineconecta.model.Users.User;
import cineconecta.cineconecta.repository.Films.CommentsRepository;
import cineconecta.cineconecta.repository.Users.AchievementsRepository;
import cineconecta.cineconecta.repository.Users.ProfileRepository;
import cineconecta.cineconecta.repository.Users.UsersRepository;
import cineconecta.cineconecta.controllers.Fimls.FilmsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentsRepository commentsRepository;
    private final UsersRepository usersRepository;
    private final AchievementsRepository achievementsRepository;
    private final ProfileRepository profileRepository;
    private final UsersRepository userRepository;

    public CommentsResponse addCommentToUser(CommentsRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getUser());

        if (userOptional.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado con email: " + request.getUser());
        }

        User user = userOptional.get();
        Profile profile = user.getProfile();

        Achievements achievement = new Achievements();
        achievement.setName("Comentario");
        achievement.setDescription("Ha realizado un comentario en la plataforma");
        achievement.setProfile(profile);
        achievementsRepository.save(achievement);

        Comments comment = new Comments();
        comment.setContent(request.getText());
        comment.setUser(user);
        commentsRepository.save(comment);

        return CommentsResponse.builder().message("Comment save").build();
    }
}
