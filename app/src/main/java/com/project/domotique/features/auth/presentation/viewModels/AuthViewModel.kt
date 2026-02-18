package com.project.domotique.features.auth.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.domotique.features.auth.domain.repositotries.AuthRepository
import com.project.domotique.features.auth.data.repositories.AuthRepositoryImpl
import com.project.domotique.features.home.UiState
import kotlinx.coroutines.launch
import javax.net.ssl.HttpsURLConnection

class AuthViewModel : ViewModel() {

    private val authRepository : AuthRepository = AuthRepositoryImpl()
    private val _authState = MutableLiveData(UiState<String?>())
    val authState : LiveData<UiState<String?>> = _authState

    fun registerUser(login:String, password: String, confirmPassword: String)
    {
        if(login.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) _authState.postValue(
            UiState(errors = "Veuillez remplir tous les champs")
        )
        else if(confirmPassword != password) _authState.postValue(
            UiState(
                loading = false,
                errors = "Les mots de passe ne correspondent pas"
            )
        )
        else if(login.length < 2) _authState.postValue(UiState(errors = "Le nom d'utilisateur doit contenir au moins 2 caractères"))
        else if(password.length < 6) _authState.postValue(UiState(errors = "Le mot de passe doit contenir au moins 6 caractères"))
        else
        {
            _authState.value = UiState(loading = true)
            viewModelScope.launch {
                authRepository.register(login, password) { code ->
                println(code)
                when(code){
                    HttpsURLConnection.HTTP_OK ->
                        _authState.postValue(
                            UiState(
                                loading = false,
                                success = true
                            )
                        )
                    HttpsURLConnection.HTTP_BAD_REQUEST ->
                        _authState.postValue(
                            UiState(
                                loading = false,
                                errors = "Les champs saisis sont incorrects !"
                            )
                        )
                    HttpsURLConnection.HTTP_CONFLICT ->
                        _authState.postValue(
                            UiState(
                                loading = false,
                                errors = "Le nom d'utilisateur renseigné existe déjà !"
                            )
                        )
                    else -> _authState.postValue(UiState(errors = "Une erreur est survenue, veuillez réessayer plutard."))
                    }
                }
            }
        }
    }


    fun authenticateUser(login: String, password: String)
    {
        if(login.isNotEmpty() && password.isNotEmpty())
        {
            _authState.value = UiState(loading = true)
            viewModelScope.launch {
                authRepository.login(login, password) { code , loginResponse ->
                    when(code)
                    {
                        HttpsURLConnection.HTTP_OK -> {
                            loginResponse?.let {
                                _authState.postValue(
                                    UiState(
                                        loading = false,
                                        success = true,
                                        data = loginResponse.token
                                    )
                                )
                            }
                        }
                        HttpsURLConnection.HTTP_UNAUTHORIZED -> {
                            _authState.postValue(
                                UiState(
                                    loading = false,
                                    errors = "Login ou mot de passe incorrect !"
                                )
                            )
                        }
                        HttpsURLConnection.HTTP_NOT_FOUND -> {
                            _authState.postValue(
                                UiState(
                                    loading = false,
                                    errors = "Aucun compte n'est associé à ces identifiants"
                                )
                            )
                        }
                        else -> {
                            _authState.postValue(
                                UiState(
                                    loading = false,
                                    errors = "Une erreur est survenue, veuillez réessayer plutard."
                                )
                            )
                        }
                    }
                }

            }
        }
        else _authState.postValue(UiState(errors = "Veuillez remplir tous les champs !"))
    }
}