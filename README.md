<div align="center">
  <img src="app/src/main/res/drawable/logo_v3.png" alt="Logo de Master Martini" width="220" />

  # Master Martini

  **Aplicación móvil educativa de recetas y cursos de pastelería.**
</div>

## Descripción

Master Martini es una aplicación Android desarrollada como proyecto semestral. Consume los microservicios Spring Boot del proyecto a través de un **API Gateway** y presenta contenido real de recetas, cursos y lecciones.

La app incluye autenticación con JWT, almacenamiento seguro del token, listado paginado de recetas, detalle de recetas y un **panel de administración** para crear contenido.

## Funcionalidades

- Registro e inicio de sesión con JWT.
- Restauración de sesión y cierre de sesión.
- Inicio con accesos directos a recetas y cursos.
- Exploración y búsqueda de recetas contra el microservicio de recetas.
- Filtros por recetas o cursos.
- Detalle de receta con ingredientes, preparación e insumos.
- Cursos y lecciones.
- Reproducción de videos de YouTube.
- **Panel de administración** para crear recetas y cursos.

## Arquitectura

La app mantiene el patrón **MVVM** con separación por capas:

```text
ui/          → Pantallas Compose y ViewModels
domain/      → Interfaces de repositorio y excepciones de dominio
data/        → DTOs, Retrofit, DataStore e implementaciones de repositorio
model/       → Modelos de presentación
di/          → Inyección manual de dependencias (AppContainer)
```

Reglas del proyecto:

- Los DTOs viven en `data/dto`.
- Retrofit y DataStore nunca se usan directamente desde `ui`.
- El mapeo DTO → modelo es privado dentro de cada repositorio.

## Conexión con el backend

Toda la app consume únicamente el **API Gateway**:

```text
http://10.0.2.2:8083/     (emulador Android)
```

| Microservicio | Puerto local | Ruta en el Gateway |
|---|---|---|
| `ms-recetas` | 8090 | `/api/v1/recipes/**`, `/api/v1/admin/recipes/**` |
| `ms-cursos` | 8091 | `/api/cursos/**`, `/api/categorias/**`, `/api/v1/admin/cursos/**` |
| `ms-contacto` | 8092 | `/api/contacto/**`, `/api/consultas/**` |
| `ms-auth` | 8094 | `/api/auth/**` |
| `api_gateway` | 8083 | Punto único de entrada |

> `10.0.2.2` solo funciona en el emulador. En un teléfono físico hay que usar la IP LAN del PC y agregarla a `network_security_config.xml`.

## Panel de administración

### Acceso restringido

El panel **solo** es accesible para la cuenta:

```text
Correo:     admin@mastermartini.cl
Contraseña: Admin1234
Rol:        ADMIN
```

La validación se hace en dos niveles:

1. **Frontend:** el botón `Panel admin` solo aparece si el usuario autenticado tiene rol `ADMIN` **y** su correo es exactamente `admin@mastermartini.cl`.
2. **Backend:** el Gateway exige `ROLE_ADMIN` en `/api/v1/admin/**` y en las escrituras de cursos y categorías.

Si un usuario normal inicia sesión, no ve ninguna entrada al panel. Si intenta llamar a los endpoints protegidos, recibe `403 Forbidden`.

### Qué permite hacer

**Recetas** (microservicio `ms-recetas`):

- Listar todas las recetas, incluidas las no publicadas.
- Crear una receta con título, descripción, categoría, duración, rendimiento, video, imagen, ingredientes y pasos.
- Decidir si se publica de inmediato.
- Eliminar recetas con confirmación.

**Cursos** (microservicio `ms-cursos`):

- Listar todos los cursos, incluidos los borradores.
- Crear un curso con título, descripción, categoría, imagen y objetivos de aprendizaje.
- Publicar u ocultar un curso.
- Agregar lecciones con título, descripción, orden, URL de video y duración.
- Eliminar cursos y sus lecciones con confirmación.

### Endpoints utilizados por el panel

```text
GET    /api/v1/admin/recipes              Listar todas las recetas
POST   /api/v1/admin/recipes              Crear receta
DELETE /api/v1/admin/recipes/{id}         Eliminar receta
GET    /api/v1/recipes/categorias         Categorías de recetas

GET    /api/v1/admin/cursos               Listar todos los cursos (incluye borradores)
POST   /api/cursos                        Crear curso
PATCH  /api/cursos/{id}/publicacion       Publicar u ocultar curso
DELETE /api/cursos/{id}                   Eliminar curso
POST   /api/cursos/{cursoId}/lecciones    Agregar lección
GET    /api/categorias                    Categorías de cursos
```

### Categorías iniciales

Las bases de datos se crean con Liquibase. Para que los formularios tengan opciones desde el primer arranque, se insertan categorías iniciales:

```text
Recetas:  Cócteles · Postres · Repostería · Técnicas básicas
Cursos:   Coctelería · Repostería · Barismo · Técnicas básicas
```

Si se eliminan todas las categorías de un microservicio, el formulario correspondiente muestra un aviso y no permite guardar hasta que exista al menos una.

### Archivos del panel

```text
data/dto/admin/                 DTOs de creación de recetas y cursos
data/remote/AdminApi.kt         Endpoints administrativos con Retrofit
data/repository/AdminRepositoryImpl.kt
domain/repository/AdminRepository.kt
model/admin/AdminModels.kt
ui/viewmodel/admin/AdminViewModel.kt
ui/screens/admin/AdminScreen.kt
ui/screens/admin/AdminRecipeForm.kt
ui/screens/admin/AdminCourseForm.kt
ui/screens/admin/AdminLessonDialog.kt
ui/screens/admin/AdminFormComponents.kt
```

## Privacidad y almacenamiento

- El token JWT se guarda con **DataStore Preferences**.
- El token se envía automáticamente como `Authorization: Bearer` mediante un interceptor de OkHttp.
- Si el backend responde `401`, la sesión se limpia y se vuelve al login.
- No se almacenan imágenes del dispositivo ni datos sensibles adicionales.

## Permisos

Solo se declara `INTERNET`, necesario para consumir el Gateway y reproducir videos de YouTube.

## Tecnologías

- Kotlin
- Jetpack Compose
- Material 3
- Android SDK 36
- Gradle con Kotlin DSL
- Retrofit 2.11 + Gson
- OkHttp 4.12
- DataStore Preferences
- Coil Compose
- android-youtube-player 13.0.0

## Estructura principal

```text
app/src/main/java/com/example/proyecto_semestral/
├── data/
│   ├── dto/            # DTOs de auth, recetas y administración
│   ├── local/          # AuthTokenStore con DataStore
│   ├── remote/         # Retrofit, interceptor y APIs
│   └── repository/     # Implementaciones de repositorio
├── domain/repository/  # Contratos de repositorio
├── model/              # Modelos de auth, recetas, admin y contenido
├── di/                 # AppContainer
├── navigation/         # Flujo de navegación
├── ui/
│   ├── components/     # Componentes reutilizables
│   ├── screens/        # Auth, inicio, exploración, recetas, cursos, videos y admin
│   ├── viewmodel/      # ViewModels por feature
│   └── theme/          # Colores, tipografías y formas
└── MainActivity.kt     # Punto de entrada
```

## Requisitos

- Android Studio.
- JDK 11 o superior.
- Android SDK 36.
- Dispositivo o emulador con Android 7.0 (API 24) o superior.
- Los microservicios y el API Gateway en ejecución.

## Ejecución

1. Levanta `ms-auth`, `ms-recetas`, `ms-cursos` y `api_gateway`.
2. Abre el proyecto en Android Studio.
3. Espera a que Gradle sincronice las dependencias.
4. Selecciona un dispositivo o emulador.
5. Ejecuta el módulo `app`.

Para compilar desde Windows:

```powershell
.\gradlew.bat :app:assembleDebug
```

## Proyecto académico

Desarrollado como parte del proyecto semestral de Desarrollo de Aplicaciones Móviles.
