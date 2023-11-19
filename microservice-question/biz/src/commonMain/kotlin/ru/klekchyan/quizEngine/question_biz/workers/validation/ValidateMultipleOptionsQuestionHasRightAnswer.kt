package ru.klekchyan.quizEngine.question_biz.workers.validation

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.klekchyan.quizEngine.question_common.QuizQuestionContext
import ru.klekchyan.quizEngine.question_common.helpers.errorValidation
import ru.klekchyan.quizEngine.question_common.helpers.fail
import ru.klekchyan.quizEngine.question_common.models.QuizQuestionType

fun CorChainDsl<QuizQuestionContext>.validateMultipleOptionsQuestionHasRightAnswer(title: String) = worker {
    this.title = title
    on {
        questionValidating.type == QuizQuestionType.MULTIPLE_OPTIONS_QUESTION &&
        questionValidating.answers.count { it.isRight } != 1
    }
    handle {
        fail(
            errorValidation(
                field = "answers",
                violationCode = "badRightAnswersCount",
                description = "multiple options question type must contain one right answer"
            )
        )
    }
}