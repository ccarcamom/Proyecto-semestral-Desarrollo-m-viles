<div align="center">
  <img src="app/src/main/res/drawable/logo_v3.png" alt="Logo de Master Martini" width="220" />

  # Master Martini

  **Aplicación móvil educativa de recetas y cursos de pastelería.**
</div>

## Descripción

Master Martini es una aplicación Android desarrollada como proyecto semestral. Presenta contenido educativo de pastelería, chocolatería y bollería incluido directamente en la aplicación.

Esta versión no incluye autenticación, registro, perfiles, panel administrativo, formularios simulados ni almacenamiento de datos del usuario. Tampoco utiliza base de datos, preferencias, archivos locales o repositorios mutables en memoria.

## Funcionalidades

- Inicio con accesos directos a recetas y cursos.
- Exploración y búsqueda de contenido.
- Filtros por recetas o cursos.
- Detalle de recetas con ingredientes, preparación e insumos.
- Opción para compartir recetas mediante el menú nativo de Android.
- Cursos de chocolatería y bollería con lecciones consecutivas.
- Reproducción de videos de YouTube.

## Privacidad y almacenamiento

- La aplicación no solicita ni conserva información personal.
- No existen cuentas de usuario ni credenciales administrativas.
- No se seleccionan ni guardan imágenes del dispositivo.
- No existe persistencia con Room, DataStore, SharedPreferences o archivos locales.
- Las recetas y cursos son contenido editorial de solo lectura incluido con la aplicación.

## Permisos

Solo se declara `INTERNET`, necesario para reproducir videos de YouTube.

## Tecnologías

- Kotlin
- Jetpack Compose
- Material 3
- Android SDK 36
- Gradle con Kotlin DSL
- android-youtube-player 13.0.0

## Estructura principal

```text
app/src/main/java/com/example/proyecto_semestral/
├── model/content/      # Catálogos editoriales de solo lectura
├── navigation/         # Flujo de navegación
├── ui/components/      # Componentes reutilizables
├── ui/screens/         # Inicio, exploración, recetas, cursos y videos
├── ui/theme/           # Colores, tipografías y formas
└── MainActivity.kt     # Punto de entrada
```

## Requisitos

- Android Studio.
- JDK 11 o superior.
- Android SDK 36.
- Dispositivo o emulador con Android 7.0 (API 24) o superior.
- Conexión a Internet para reproducir videos de YouTube.

## Ejecución

1. Abre el proyecto en Android Studio.
2. Espera a que Gradle sincronice las dependencias.
3. Selecciona un dispositivo o emulador.
4. Ejecuta el módulo `app`.

Para compilar desde Windows:

```powershell
.\gradlew.bat :app:assembleDebug
```

## Proyecto académico

Desarrollado como parte del proyecto semestral de Desarrollo de Aplicaciones Móviles.
