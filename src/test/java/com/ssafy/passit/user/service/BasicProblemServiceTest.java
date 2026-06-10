package com.ssafy.passit.user.service;

import com.ssafy.passit.problem.dto.request.MultipleChoiceProblemCreateRequest;
import com.ssafy.passit.problem.dto.request.ShortAnswerProblemCreateRequest;
import com.ssafy.passit.problem.repository.ProblemRepository;
import com.ssafy.passit.problem.service.BasicProblemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BasicProblemServiceTest {

    @Mock
    ProblemRepository repository;

    @InjectMocks
    BasicProblemService problemService;

    @Test
    @DisplayName("객관식 문제 저장 성공")
    void saveMultipleChoiceProblem_success() {
        var request = MultipleChoiceProblemCreateRequest.builder()
                .certId(1L)
                .question("1+1은?")
                .choice1Content("-1")
                .choice2Content("2")
                .choice3Content("100")
                .choice4Content("10000")
                .answerNumber(2)
                .build();

        given(repository.saveProblem(any())).willReturn(1L);

        problemService.saveMultipleChoiceProblem(request);

        verify(repository).saveProblem(any());
        verify(repository).saveMultipleChoiceProblem(any());
    }

    @Test
    @DisplayName("주관식 문제 저장 성공")
    void saveShortAnswerProblem_success() {
        var request = ShortAnswerProblemCreateRequest.builder()
                .certId(1L)
                .question("1+1은?")
                .answer("2")
                .build();

        given(repository.saveProblem(any())).willReturn(1L);

        problemService.saveShortChoiceProblem(request);

        verify(repository).saveProblem(any());
        verify(repository).saveShortAnswerProblem(any());
    }
}
