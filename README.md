<div align="center">
  <img src="app/src/main/res/drawable/logo.png" alt="Logo de Master Martini" width="280" />

  # Master Martini

  **Aplicación móvil educativa de recetas, técnicas y cursos de pastelería.**
</div>

## Descripción

Master Martini es una aplicación Android desarrollada como proyecto semestral. Su objetivo es ofrecer a emprendedores y profesionales gastronómicos un espacio donde puedan descubrir recetas, consultar técnicas y acceder a cursos relacionados con chocolatería, bollería y pastelería.

La aplicación utiliza contenido local y flujos simulados con fines demostrativos y académicos.

## Funcionalidades

- Registro e inicio de sesión.
- Persistencia local de la sesión del usuario.
- Pantalla de inicio con contenido recomendado.
- Búsqueda de recetas, videos y cursos.
- Filtros por tipo de contenido.
- Detalle de recetas con ingredientes y preparación.
- Opción para compartir recetas mediante el menú nativo de Android.
- Detalle de cursos y listado de lecciones.
- Reproductor de video simulado.
- Formulario de contacto con validaciones y opción para agregar la ubicación actual.
- Perfil con datos personales, información del negocio e intereses.
- Selección de una foto de perfil desde la galería del dispositivo.
- Navegación inferior entre las secciones principales.
- Transiciones animadas al navegar entre pantallas.

## Funciones nativas y permisos

La aplicación utiliza funciones propias de Android para interactuar con la galería, la ubicación y el menú para compartir contenido. Los permisos se solicitan únicamente cuando el usuario intenta utilizar la función que los necesita.

### Foto de perfil

Desde la pantalla **Mi perfil** se puede tocar directamente el avatar para seleccionar una imagen de la galería. Android solicita el permiso correspondiente según la versión del dispositivo y permite escoger la fotografía mediante su selector nativo.

La aplicación conserva el acceso a la imagen seleccionada y guarda su referencia localmente al presionar **Guardar cambios**. Si el permiso se rechaza, la galería no se abre y se muestra un mensaje informativo.

### Ubicación en el formulario de contacto

El formulario de contacto incluye el botón **Usar mi ubicación actual**. Al presionarlo, la aplicación solicita acceso a la ubicación precisa o aproximada y, si el usuario lo autoriza, completa el campo con las coordenadas obtenidas.

La ubicación no se consulta en segundo plano. Solo se obtiene cuando el usuario presiona el botón. Si el permiso se rechaza o los servicios de ubicación están desactivados, la aplicación informa que no pudo obtenerla y permite completar el campo manualmente.

### Compartir recetas

En el detalle de una receta, el botón **Compartir receta** abre el menú nativo de Android. El contenido compartido incluye el título, la descripción, la duración, el rendimiento y los ingredientes.

Esta función no necesita un permiso adicional: Android entrega el contenido únicamente a la aplicación que el usuario selecciona en el menú para compartir.

### Permisos declarados

| Permiso | Uso |
| --- | --- |
| `READ_MEDIA_IMAGES` | Acceso a imágenes en Android 13 o superior. |
| `READ_MEDIA_VISUAL_USER_SELECTED` | Acceso limitado a las imágenes elegidas por el usuario en versiones recientes de Android. |
| `READ_EXTERNAL_STORAGE` | Compatibilidad con la galería hasta Android 12L. |
| `ACCESS_COARSE_LOCATION` | Obtención de una ubicación aproximada. |
| `ACCESS_FINE_LOCATION` | Obtención de una ubicación precisa cuando el usuario la autoriza. |

La aplicación continúa funcionando si estos permisos no se conceden; solamente quedan deshabilitadas las funciones asociadas.

## Animaciones

La interfaz utiliza animaciones breves para ofrecer una navegación más fluida sin retrasar la interacción. Se incluyen:

- Transiciones de desplazamiento y fundido entre pantallas.
- Cambios animados en la barra de navegación inferior.
- Transiciones de color en los filtros de contenido.
- Animaciones al seleccionar intereses y casillas de verificación.

## Tecnologías utilizadas

- **Kotlin**
- **Jetpack Compose**
- **Material 3**
- **Android SDK 36**
- **Gradle con Kotlin DSL**
- **ViewModel** para el estado de los formularios
- **SharedPreferences** para la sesión local

## Estructura principal

```text
app/src/main/java/com/example/proyecto_semestral/
├── model/          # Modelos y catálogos de contenido
├── navigation/     # Navegación, sesión y usuarios
├── ui/
│   ├── components/ # Componentes reutilizables
│   ├── screens/    # Pantallas de la aplicación
│   └── theme/      # Colores, tipografías y formas
├── viewmodel/      # Estado y validación de formularios
└── MainActivity.kt # Punto de entrada de la aplicación
```

## Requisitos

- Android Studio
- JDK 21
- Android SDK 36
- Dispositivo o emulador con Android 7.0 (API 24) o superior

## Ejecución

1. Clona el repositorio:

   ```bash
   git clone <URL_DEL_REPOSITORIO>
   ```

2. Abre el proyecto en Android Studio.
3. Espera a que Gradle sincronice las dependencias.
4. Selecciona un emulador o dispositivo físico.
5. Ejecuta el módulo `app`.

También puedes comprobar la compilación desde una terminal de Windows:

```powershell
.\gradlew.bat :app:assembleDebug
```

## Consideraciones

- Las cuentas y los contenidos se utilizan como demostración local.
- El reproductor no consume videos externos; simula la experiencia de reproducción.
- El formulario de contacto valida los datos y puede obtener la ubicación, pero no envía la consulta a un servicio empresarial real.
- La información del perfil, incluida la referencia a la fotografía seleccionada, se almacena localmente en el dispositivo.
- Durante las pruebas en un dispositivo o emulador, es necesario aceptar los permisos de imágenes y ubicación para comprobar esas funciones.

## Proyecto académico

Desarrollado como parte del proyecto semestral de Desarrollo de Aplicaciones Móviles.
