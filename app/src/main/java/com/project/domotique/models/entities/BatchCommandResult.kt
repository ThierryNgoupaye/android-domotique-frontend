package com.project.domotique.models.entities

data class BatchCommandResult(
    val total: Int,
    val success: Int,
    val failed: Int
) {
    val isFullSuccess: Boolean get() = failed == 0 && total > 0
    val isPartialSuccess: Boolean get() = success > 0 && failed > 0
    val isEmpty: Boolean get() = total == 0
}

