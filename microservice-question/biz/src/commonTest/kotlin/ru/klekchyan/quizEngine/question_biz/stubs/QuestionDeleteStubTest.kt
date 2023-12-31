package ru.klekchyan.quizEngine.question_biz.stubs

import kotlinx.coroutines.test.runTest
import models.QuizCommonEntityId
import ru.klekchyan.quizEngine.question_biz.QuizQuestionProcessor
import ru.klekchyan.quizEngine.question_common.QuizQuestionContext
import ru.klekchyan.quizEngine.question_common.models.*
import ru.klekchyan.quizEngine.question_common.stubs.QuizQuestionStubs
import kotlin.test.Test
import kotlin.test.assertEquals

class QuestionDeleteStubTest {
    
    private val processor = QuizQuestionProcessor()

    private val id = QuizCommonEntityId("1")

    @Test
    fun success() = runTest {
        val ctx = QuizQuestionContext(
            command = QuizQuestionCommand.DELETE,
            state = QuizQuestionState.NONE,
            workMode = QuizQuestionWorkMode.STUB,
            stubCase = QuizQuestionStubs.SUCCESS,
            questionRequest = QuizQuestion(
                id = id
            )
        )
        processor.exec(ctx)
        assertEquals(QuizQuestionState.FINISHING, ctx.state)
        assertEquals(id, ctx.questionResponse.id)
    }

    @Test
    fun badId() = runTest {
        val ctx = QuizQuestionContext(
            command = QuizQuestionCommand.DELETE,
            state = QuizQuestionState.NONE,
            workMode = QuizQuestionWorkMode.STUB,
            stubCase = QuizQuestionStubs.BAD_ID,
            questionRequest = QuizQuestion(
                id = QuizCommonEntityId.NONE
            )
        )
        processor.exec(ctx)
        assertEquals(QuizQuestionState.FAILING, ctx.state)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
    }

    @Test
    fun cannotDelete() = runTest {
        val ctx = QuizQuestionContext(
            command = QuizQuestionCommand.DELETE,
            state = QuizQuestionState.NONE,
            workMode = QuizQuestionWorkMode.STUB,
            stubCase = QuizQuestionStubs.CANNOT_DELETE,
            questionRequest = QuizQuestion(
                id = id
            )
        )
        processor.exec(ctx)
        assertEquals(QuizQuestionState.FAILING, ctx.state)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
        assertEquals("internal-cannot-delete", ctx.errors.firstOrNull()?.code)
    }

    @Test
    fun dbError() = runTest {
        val ctx = QuizQuestionContext(
            command = QuizQuestionCommand.DELETE,
            state = QuizQuestionState.NONE,
            workMode = QuizQuestionWorkMode.STUB,
            stubCase = QuizQuestionStubs.DB_ERROR,
            questionRequest = QuizQuestion(
                id = id
            )
        )
        processor.exec(ctx)
        assertEquals(QuizQuestion(), ctx.questionResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badStubCase() = runTest {
        val ctx = QuizQuestionContext(
            command = QuizQuestionCommand.DELETE,
            state = QuizQuestionState.NONE,
            workMode = QuizQuestionWorkMode.STUB,
            stubCase = QuizQuestionStubs.BAD_FORMULATION,
            questionRequest = QuizQuestion(
                id = id
            )
        )
        processor.exec(ctx)
        assertEquals(QuizQuestion(), ctx.questionResponse)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}