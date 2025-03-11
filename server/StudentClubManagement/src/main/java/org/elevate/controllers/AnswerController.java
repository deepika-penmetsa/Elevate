package org.elevate.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.elevate.dtos.AnswerDTO;
import org.elevate.dtos.ApiResponseDTO;
import org.elevate.exceptions.ClubNotFoundException;
import org.elevate.exceptions.QuestionNotFoundException;
import org.elevate.models.Answer;
import org.elevate.services.AnswerService;

import java.util.List;

@RestController
@RequestMapping("/student/answers")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Answer APIs", description = "APIs for managing answers to questions in a club, allowing users to post and retrieve answers.")
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    /**
     * Creates a new answer for a question (Accessible by Students, Club Admins, and Super Admins).
     *
     * @param answerDTO The answer details including the question ID, content, and author.
     * @return A response entity containing a success message and the created answer.
     * @throws ClubNotFoundException If the specified club does not exist.
     * @throws QuestionNotFoundException If the specified question does not exist.
     */
    @PostMapping
    @Operation(
        summary = "Create an Answer",
        description = "Allows users to post an answer to a specific question in a club."
    )
    public ResponseEntity<ApiResponseDTO> createAnswer(@RequestBody AnswerDTO answerDTO)
            throws ClubNotFoundException, QuestionNotFoundException {
        Answer savedAnswer = answerService.createAnswer(answerDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>("Answer posted successfully", savedAnswer));
    }

    /**
     * Retrieves all answers for a specific question (Accessible by Students, Club Admins, and Super Admins).
     *
     * @param questionId The unique ID of the question of whose answers need to be fetched.
     * @return A response entity containing a list of answers for the specified question.
     */
    @GetMapping("/{questionId}")
    @Operation(
        summary = "Get Answers for a Question",
        description = "Fetches all answers associated with a specific question ID."
    )
    public ResponseEntity<ApiResponseDTO<List<Answer>>> getAnswersByQuestion(
        @Parameter(
            description = "The unique ID of the question whose answers need to be retrieved",
            required = true,
            example = "1"
        )
        @PathVariable Long questionId
    ) {
        List<Answer> answers = answerService.getAnswersByQuestion(questionId);
        return ResponseEntity.ok(new ApiResponseDTO<>("Answers fetched successfully", answers));
    }
}
