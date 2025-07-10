package cineconecta.cineconecta.repository.Films;

import cineconecta.cineconecta.model.Users.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentsRepository extends JpaRepository<Comments, UUID> {
}
