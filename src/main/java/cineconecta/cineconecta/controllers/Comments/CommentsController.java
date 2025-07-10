package cineconecta.cineconecta.controllers.Comments;

import cineconecta.cineconecta.controllers.Comments.CommentsResponse;
import cineconecta.cineconecta.dto.CommentsRequest;
import cineconecta.cineconecta.model.Users.Comments;
import cineconecta.cineconecta.service.Films.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.util.function.ThrowingSupplier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cine/comments")
public class CommentsController {

    private final CommentService commentService;

    @PostMapping()
    public ResponseEntity<CommentsResponse> addComment(@RequestBody CommentsRequest request) {
        return handleRequestProcess(() -> commentService.addCommentToUser(request));
    }

    private ResponseEntity<CommentsResponse> handleRequestProcess(ThrowingSupplier<CommentsResponse> supplier) {
        try {
            return ResponseEntity.ok(supplier.get());
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body(CommentsResponse.builder().message("Error: " + e).build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(CommentsResponse.builder().message("Internal Server Error: " + e ).build());
        }
    }
}
