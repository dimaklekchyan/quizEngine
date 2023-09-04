package ru.klekchyan.quizEngine.models

data class QuizError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val exception: Throwable? = null,
)