package org.elevate.services;

import org.springframework.stereotype.Service;
import org.elevate.dtos.AnswerDTO;
import org.elevate.exceptions.ClubNotFoundException;
import org.elevate.exceptions.QuestionNotFoundException;
import org.elevate.exceptions.UserNotFoundException;
import org.elevate.models.*;
import org.elevate.repositories.AnswerRepository;
import org.elevate.repositories.ClubRepository;
import org.elevate.repositories.QuestionRepository;
import org.elevate.repositories.UserRepository;

import java.util.List;

@Service
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final QuestionRepository questionRepository;

    public AnswerService(AnswerRepository answerRepository, UserRepository userRepository,
                         ClubRepository clubRepository, QuestionRepository questionRepository) {
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
        this.clubRepository = clubRepository;
        this.questionRepository = questionRepository;
    }

    /**
     * Create a new answer
     */
    public Answer createAnswer(AnswerDTO answerDTO) throws ClubNotFoundException, QuestionNotFoundException {
        User user = userRepository.findById(answerDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Club club = clubRepository.findById(answerDTO.getClubId())
                .orElseThrow(() -> new ClubNotFoundException("Club not found"));

        Question question = questionRepository.findById(answerDTO.getQuestionId())
                .orElseThrow(() -> new QuestionNotFoundException("Question not found"));

        Answer answer = new Answer();
        answer.setUser(user);
        answer.setClub(club);
        answer.setQuestion(question);
        answer.setAnswer(answerDTO.getAnswer());

        return answerRepository.save(answer);
    }

    /**
     * Get all answers for a question
     */
    public List<Answer> getAnswersByQuestion(Long questionId) {
        return answerRepository.findByQuestion_QuestionId(questionId);
    }
}
