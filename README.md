# 🏠 Domotique App - Application Android de Domotique

Application Android native de contrôle domotique permettant de gérer des maisons intelligentes avec leurs appareils connectés (lumières, volets, portes de garage).

##  Aperçu du projet

**Domotique App** est une application Android moderne développée en **Kotlin** qui permet aux utilisateurs de :
- Gérer plusieurs maisons via une API REST
- Contrôler des appareils domotiques en temps réel (lumières, volets roulants, portes de garage)
- Exécuter des commandes personnalisées groupées (ex: "Fermer tous les volets du 1er étage")
- Gérer les accès partagés à leurs maisons
- Visualiser l'état des appareils par pièce et par étage

---

##  Architecture

Le projet suit une **architecture MVVM (Model-View-ViewModel)** avec une organisation par **features** pour une meilleure séparation des responsabilités et une scalabilité optimale.

### Structure des packages

```
com.project.domotique/
├── features/
│   ├── auth/                          # Feature d'authentification
│   │   ├── data/                      # Couche données (API, DTOs)
│   │   ├── domain/                    # Couche métier (entities, repositories)
│   │   └── presentation/              # Couche présentation (UI, ViewModels)
│   │
│   │
│   │
│   │
│   ├── onboarding/                    # Lancement de l'App pour la première fois
│   │
│   └── home/                          # Feature principale (maison)
│       ├── devices/                   # Gestion des appareils
│       │   ├── data/                  # API devices
│       │   ├── domain/                # Entities, repositories
│       │   └── presentation/          # Fragments, adapters, ViewModels
│       │
│       ├── house/                     # Gestion des maisons
│       │   ├── data/
│       │   ├── domain/
│       │   └── presentation/
│       │
│       ├── customCommands/            # Commandes groupées
│       │   ├── domain/
│       │   └── presentation/
│       │
│       └── houseAccess/               # Gestion des accès partagés
│           ├── data/
│           ├── domain/
│           └── presentation/
│
├── shared/                            # Composants réutilisables
│   ├── LoadingDialog.kt
│   ├── ConfirmPopupDialog.kt
│   └── CongratulationDialog.kt
│
└── utils/                             # Utilitaires
    ├── Api.kt                         # Client HTTP générique
    ├── LocalStorageManager.kt         # Gestion SharedPreferences
    ├── RoomDistributor.kt             # Algorithme de distribution des devices
    ├── CustomCommandResolver.kt       # Résolution des commandes groupées
    ├── ErrorMessage.kt                # Gestion centralisée des erreurs
    └── Constants.kt                   # Constantes globales
```

---

##  Pourquoi MVVM + Repository Pattern ?

### ViewModel

Les **ViewModels** assurent :
- **Séparation UI/Logique** : la logique métier est isolée des fragments/activities
- **Survie aux changements de configuration** : les données persistent lors de rotations d'écran
- **Gestion du cycle de vie** : évite les fuites mémoire grâce à `viewModelScope`
- **Réactivité** : utilisation de `LiveData` pour observer les changements d'état

**Exemple** : `DeviceViewModel`
```kotlin
class DeviceViewModel : ViewModel() {
    private val _deviceState = MutableLiveData<UiState<List<DeviceEntity>>>()
    val deviceState: LiveData<UiState<List<DeviceEntity>>> = _deviceState
    
    fun retrieveDeviceList(houseId: Int, token: String) {
        _deviceState.postValue(UiState(loading = true))
        viewModelScope.launch {
            // Appel API via Repository
            deviceRepository.getHouseDevices(houseId, token) { code, data ->
                // Traitement et émission du résultat
            }
        }
    }
}
```

### Repository Pattern

Les **Repositories** centralisent l'accès aux données :
- **Abstraction de la source de données** : l'UI ignore si les données viennent de l'API, de la BDD, ou du cache
- **Testabilité** : facile de mocker pour les tests unitaires
- **Réutilisabilité** : plusieurs ViewModels peuvent utiliser le même repository

**Exemple** : `DeviceRepository`
```kotlin
interface DeviceRepository {
    fun getHouseDevices(
        houseId: Int, 
        token: String, 
        doAction: (statusCode: Int, data: List<DeviceEntity>?) -> Unit
    )
}
```

---

## Pourquoi RecyclerView ?

Le **RecyclerView** est utilisé partout dans l'app car il offre :

### 1. Performance optimale
- **View Recycling** : réutilise les vues au lieu d'en créer constamment
- **ViewHolder Pattern** : évite les appels répétés à `findViewById()`
- Idéal pour les longues listes (appareils, maisons, commandes)

### 2. Flexibilité des layouts
- **LinearLayoutManager** : listes verticales/horizontales (devices par pièce)
- **GridLayoutManager** : grille 2 colonnes (commandes personnalisées)
- Support natif du swipe-to-refresh

### 3. Gestion avancée avec Adapters personnalisés

**Exemple** : `RoomDeviceAdapter` avec ViewTypes multiples
```kotlin
class RoomDeviceAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    
    override fun getItemViewType(position: Int): Int {
        return when (rooms[position].totalDevices) {
            1 -> VIEW_TYPE_SINGLE
            2 -> VIEW_TYPE_DOUBLE
            3 -> VIEW_TYPE_TRIPLE
            else -> VIEW_TYPE_LIST
        }
    }
    
    // Affichage différent selon le nombre d'appareils par pièce
}
```

**Avantages** :
- UI adaptée dynamiquement au contenu
- Callbacks pour communiquer avec les ViewModels
- Gestion du cycle de vie des Dialogs dans les ViewHolders

---

##  Librairies utilisées

### Core Android
| Librairie | Version | Usage |
|-----------|---------|-------|
| **androidx.core:core-ktx** | 1.17.0 | Extensions Kotlin pour Android |
| **androidx.appcompat** | 1.7.1 | Compatibilité anciennes versions Android |
| **material** | 1.13.0 | Material Design Components |
| **constraintlayout** | 2.2.1 | Layouts complexes et performants |

### Architecture & Lifecycle
| Librairie | Version | Usage |
|-----------|---------|-------|
| **lifecycle-viewmodel-ktx** | 2.10.0 | ViewModels avec coroutines |
| **lifecycle-livedata-ktx** | 2.10.0 | LiveData réactif |
| **fragment-ktx** | 1.8.9 | Extensions Kotlin pour Fragments |

### Navigation
| Librairie | Version | Usage |
|-----------|---------|-------|
| **navigation-fragment-ktx** | 2.9.7 | Navigation Component |
| **navigation-ui-ktx** | 2.9.7 | Intégration avec BottomNavigationView |

### Networking & Serialization
| Librairie | Version | Usage |
|-----------|---------|-------|
| **Gson** | 2.13.2 | Parsing JSON ↔ Kotlin objects |
| **Coroutines** | Built-in | Appels API asynchrones, batch commands |

### UI/UX
| Librairie | Version | Usage |
|-----------|---------|-------|
| **Glide** | 5.0.5 | Chargement d'images optimisé |
| **swiperefreshlayout** | 1.2.0 | Pull-to-refresh sur les listes |


---

## Fonctionnalités principales

### 1. Authentification
- Inscription / Connexion sécurisée
- Gestion de session avec token JWT
- Persistence des credentials (SharedPreferences)

### 2. Gestion des maisons
- Liste de toutes les maisons accessibles
- Filtrage par étage et type d'appareil
- Sélection de la maison active (SharedViewModel)

### 3. Contrôle des appareils
- Vue par pièce avec distribution intelligente (RoomDistributor)
- Commandes individuelles (ON/OFF, OPEN/CLOSE)
- Affichage de l'état en temps réel
- Pull-to-refresh avec délai de 5s pour l'animation physique

### 4. Commandes personnalisées (Batch Commands)
- 18 commandes prédéfinies (ex: "Fermer tous les volets")
- **Résolution intelligente** avec filtres :
    - Par type d'appareil (lumière, volet, porte)
    - Par étage (rez-de-chaussée, 1er étage)
    - Par état actuel (évite les envois inutiles)
- **Exécution parallèle** avec Kotlin Coroutines (`async/awaitAll`)
- Feedback détaillé : "7/7 commandes envoyées avec succès"

### 5. Gestion des accès
- Accorder l'accès à d'autres utilisateurs
- Révoquer les accès
- Liste des utilisateurs ayant accès

---

##  Composants techniques clés

### 1. CustomCommandResolver
Algorithme de résolution des commandes groupées avec composition de sous-commandes :
```kotlin
TURN_ON_ALL_LIGHT_IN_ALL_ROOM = 
    resolveAtom(LIGHT, floor=1, TURN_ON) + 
    resolveAtom(LIGHT, floor=2, TURN_ON)
```

**Optimisations** :
- Filtre par état (n'envoie pas TURN_ON à une lumière déjà allumée)
- Vérification des commandes disponibles (`availableCommands`)
- Extraction intelligente de l'étage depuis l'ID du device

### 2. RoomDistributor
Distribution automatique des appareils par pièce selon une configuration prédéfinie :
- 14 pièces réparties sur 2 étages
- Algorithme de tri et d'affectation
- Support du garage avec porte automatique

### 3. Api.kt - Client HTTP générique
Client HTTP réutilisable avec Kotlin reified types :
```kotlin
inline fun <reified T> get(path: String, onSuccess: (Int, T?) -> Unit, token: String?)
inline fun <reified K, reified T> post(path: String, data: K, onSuccess: (Int, T?) -> Unit, token: String?)
```

### 4. UiState<T>
Pattern générique pour gérer tous les états UI :
```kotlin
data class UiState<T>(
    val loading: Boolean = false,
    val success: Boolean = false,
    val data: T? = null,
    val errors: String? = null
)
```

### 5. Dialogs réutilisables
- **LoadingDialog** : loader avec message personnalisable
- **ConfirmPopupDialog** : confirmation d'actions critiques
- **CongratulationDialog** : feedback positif (bienvenue, succès)

---

##  Installation

### Prérequis
- **Android Studio** Arctic Fox ou supérieur
- **JDK 11** minimum
- **Android SDK 24+** (Android 7.0+)
- **Gradle 9.0.0**

### Étapes

1. **Cloner le repository**
```bash
git clone https://github.com/ThierryNgoupaye/android-domotique-frontend.git
cd domotique-app
```

2. **Configurer l'API**

Modifier `Constants.kt` avec l'URL de votre backend :
```kotlin
const val BASE_URL: String = "https://your-api.com/api"
```

3. **Build & Run**
```bash
./gradlew build
./gradlew installDebug
```

Ou via Android Studio : `Run > Run 'app'`

---

##  API Backend

L'app communique avec une API REST PolyHome :

- **Authentification** : JWT Bearer Token
- **Format** : JSON

### Endpoints principaux
| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/users/register` | Inscription |
| POST | `/users/login` | Connexion |
| GET | `/houses` | Liste des maisons |
| GET | `/houses/{id}/devices` | Appareils d'une maison |
| POST | `/houses/{id}/devices/{deviceId}/command` | Commande device |
| GET | `/houses/{id}/users` | Accès partagés |
| POST | `/houses/{id}/users` | Accorder accès |

---

##  Screenshots

### Authentification
| Onboarding | Inscription | Connexion |
|------------|-------------|-----------|
| *Slides de présentation* | *Formulaire d'inscription* | *Login avec validation* |

### Maisons & Appareils
| Liste des maisons | Filtres par type | Vue par pièces |
|-------------------|------------------|----------------|
| *Sélection maison active* | *LIGHT / SHUTTER / DOOR* | *Contrôles par room* |

### Commandes personnalisées
| Grid 2 colonnes | Dialog de confirmation | Résultat batch |
|-----------------|------------------------|----------------|
| *18 commandes disponibles* | *"Fermer tous les volets ?"* | *"7/7 commandes envoyées"* |

---



##  Contribution

Les contributions sont les bienvenues ! Pour contribuer :

1. Fork le projet
2. Créer une branche feature (`git checkout -b feature/AmazingFeature`)
3. Commit les changements (`git commit -m 'Add AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrir une Pull Request

---

##  License

Ce projet est sous licence MIT. Voir le fichier `LICENSE` pour plus de détails.

---

##  Auteur

**Thierry NGOUPAYE**  
Étudiant en informatique - Spécialisation Ingénierie Logicielle et Intelligence Artificielle.


---


##  Ressources utiles

- [Icônes ios/android/windows](https://icons8.com/icons)
- [Background dégradé et formes customisés](https://app.haikei.app/)
- [Flat Icons](https://storyset.com/)
- [UI Design](https://dribbble.com/)
- [Documentation Android officielle](https://developer.android.com/docs)
- [Guide MVVM Architecture](https://developer.android.com/topic/architecture)
- [Material Design Guidelines](https://material.io/design)


---

