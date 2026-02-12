package com.project.domotique.viewModels


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.domotique.repositories.AuthRepository
import com.project.domotique.repositories.AuthRepositoryImpl
import com.project.domotique.uiState.UiState
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val authRepository : AuthRepository = AuthRepositoryImpl()
    private val _authState = MutableLiveData(UiState<String?>())
    val authState : LiveData<UiState<String?>> = _authState

    fun registerUser(login:String, password: String, confirmPassword: String)
    {
        _authState.value = UiState(loading = true)
        if(confirmPassword != password)
        {
            _authState.postValue(UiState(loading = false, errors = "Les mots de passe ne correspondent pas"))
        }
        else if(login.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty())
        {
            viewModelScope.launch {
                authRepository.register(login, password) { code ->
                when(code){
                    201 -> {
                        _authState.postValue(
                            UiState(
                                loading = false,
                                success = true
                            )
                        )
                    }
                    400 -> {
                        _authState.postValue(
                            UiState(
                                loading = false,
                                errors = "Les champs saisis sont incorrects !"
                            )
                        )
                    }
                    409 -> {
                        _authState.postValue(
                            UiState(
                                loading = false,
                                errors = "Le nom d'utilisateur renseigné existe déjà !"
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
        else if(login.length < 2)
        {
            _authState.postValue(
                UiState(
                    loading = false,
                    errors = "Le login doit contenir au moins 2 caractères"
                )
            )
        }
        else if(password.length < 6) {
            _authState.postValue(
                UiState(
                    loading = false,
                    errors = "Le mot de passe doit contenir au moins 8 caractères"
                )
            )
        }
        else {
            _authState.postValue(
                UiState(
                    loading = false,
                    errors = "Veuillez remplir tous les champs"
                )
            )
        }
    }


    fun authenticateUser(login: String, password: String)
    {
        _authState.value = UiState(loading = true)
        if(login.isNotEmpty() && password.isNotEmpty())
        {
            viewModelScope.launch {
                authRepository.login(login, password) { code , loginResponse ->
                    when(code)
                    {
                        200 -> {
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
                        401 -> {
                            _authState.postValue(
                                UiState(
                                    loading = false,
                                    errors = "Login ou mot de passe incorrect !"
                                )
                            )
                        }
                        404 -> {
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
        else
        {
            _authState.postValue(
                UiState(
                    loading = false,
                    errors = "Veuillez remplir tous les champs !"
                )
            )
        }

    }
}