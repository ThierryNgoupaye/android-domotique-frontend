package com.project.domotique.utils





enum class ErrorMessage(val label: String) {

    INVALID_CREDENTIALS("Identifiant ou mot de passe incorrect"),
    UNKNOWN_ERROR("Une erreur interne est survenue, veuillez réessayer plus tard !"),
    FORBIDDEN("Votre session a expiré, veuillez vous reconnecter !"),
    BAD_REQUEST("Vérifiez les informations renseignées et réessayer !"),

    CONFLICT("Vérifiez les informations renseignées et réessayer !"),

    EMPTY_FIELD("Veuillez remplir tous les champs obligatoires"),
}