<div align="center">
  <img src="app/src/main/res/drawable/logo_v3.png" alt="Logo de Master Martini" width="220" />

  # Master Martini

  **Aplicación móvil educativa de recetas y cursos de pastelería.**
</div>

## Descripción

Master Martini es una aplicación Android desarrollada como proyecto semestral. Está orientada a emprendedores y profesionales gastronómicos que buscan descubrir recetas, aprender técnicas de chocolatería y bollería, y consultar contenido útil para sus negocios.

El proyecto funciona sin backend ni base de datos. Los usuarios, las sesiones, los perfiles y las recetas administradas se conservan únicamente en memoria mientras el proceso de la aplicación permanece activo.

## Funcionalidades

### Usuarios

- Registro e inicio de sesión.
- Validación de credenciales y bloqueo de usuarios.
- Perfil editable con nombre, correo, teléfono, negocio, imagen e intereses.
- Selección de intereses en chocolatería, bollería, pastelería y panadería.
- Selección de fotografía de perfil desde la galería.

### Contenido

- Inicio con contenido recomendado y accesos directos a recetas y cursos.
- Exploración con búsqueda y filtros por recetas o cursos.
- Detalle de recetas con descripción, video, ingredientes, preparación e insumos.
- Opción para compartir recetas mediante el menú nativo de Android.
- Cursos de chocolatería y bollería con lecciones consecutivas.
- Reproducción de videos reales de YouTube integrada en recetas y cursos.

### Administración

- Panel administrativo independiente.
- Búsqueda, edición, bloqueo, desbloqueo y eliminación de usuarios.
- Creación, edición y eliminación de recetas.
- Formulario de recetas con título, enlace de YouTube, descripción, ingredientes, pasos, insumos e imagen.
- Publicación inmediata de las recetas administradas en Explorar y en su pantalla de detalle.

### Contacto

- Formulario con información del usuario y su negocio.
- Validación de correo y descripción.
- Confirmación simulada del envío de la consulta.

## Acceso administrativo

Las siguientes cuentas están disponibles para probar el panel administrativo:

| Correo | Contraseña |
| --- | --- |
| `matias@admin.cl` | `123123` |
| `pedro@admin.cl` | `123123` |
| `cristobal@admin.cl` | `123123` |

Solo estas direcciones tienen rol administrativo. El dominio `@admin.cl` está reservado y no puede utilizarse durante el registro normal.

## Funciones nativas y permisos

### Selección de imágenes

La aplicación utiliza el selector de archivos de Android para elegir la foto de perfil y las imágenes de las recetas. Las referencias de las imágenes se mantienen durante la ejecución de la aplicación.

### Compartir recetas

El botón **Compartir receta** abre el menú nativo de Android mediante `Intent.ACTION_SEND`. El texto compartido incluye el título, la descripción, el video, la duración, el rendimiento y los ingredientes disponibles.

### Permisos declarados

| Permiso | Uso |
| --- | --- |
| `READ_MEDIA_IMAGES` | Lectura de imágenes en Android 13 o superior. |
| `READ_MEDIA_VISUAL_USER_SELECTED` | Acceso limitado a imágenes seleccionadas en versiones recientes de Android. |
| `READ_EXTERNAL_STORAGE` | Compatibilidad con la selección de imágenes hasta Android 12L. |
| `INTERNET` | Reproducción de YouTube y carga de contenido remoto. |

La aplicación no solicita acceso a la ubicación, cámara, micrófono, contactos ni sensores del dispositivo.

## Tecnologías utilizadas

- **Kotlin**
- **Jetpack Compose**
- **Material 3**
- **ViewModel** y estados reactivos con `StateFlow`
- **Android SDK 36**
- **Gradle con Kotlin DSL**
- **android-youtube-player 13.0.0** para videos de YouTube
- **Coil Compose 2.7.0** para carga de imágenes

## Estructura principal

```text
app/src/main/java/com/example/proyecto_semestral/
├── model/              # Usuarios, recetas y catálogos de contenido
├── navigation/         # Rutas y flujo principal de la aplicación
├── repository/admin/   # Usuarios, sesión y recetas en memoria
├── ui/
│   ├── components/     # Componentes reutilizables y reproductor
│   ├── screens/        # Pantallas públicas y administrativas
│   └── theme/          # Colores, tipografías y formas
├── viewmodel/          # Estado y lógica de presentación
└── MainActivity.kt     # Punto de entrada
```

## Requisitos

- Android Studio.
- JDK 11 o superior.
- Android SDK 36.
- Dispositivo o emulador con Android 7.0 (API 24) o superior.
- Conexión a Internet para reproducir videos de YouTube.

## Ejecución

1. Clona el repositorio:

   ```bash
   git clone <URL_DEL_REPOSITORIO>
   ```

2. Abre el proyecto en Android Studio.
3. Espera a que Gradle sincronice las dependencias.
4. Selecciona un emulador o dispositivo físico.
5. Ejecuta el módulo `app`.

Para comprobar la compilación desde Windows:

```powershell
.\gradlew.bat :app:assembleDebug
```

## Consideraciones

- No existe integración con un backend, Room ni otro sistema de persistencia.
- Los usuarios registrados, cambios de perfil y recetas creadas se pierden cuando Android finaliza el proceso de la aplicación.
- El formulario de contacto no envía información a un servicio empresarial real.
- Los videos pertenecen a sus respectivos autores en YouTube y requieren conexión a Internet.
- En algunos emuladores puede ser necesario utilizar renderizado gráfico por software si el controlador OpenGL provoca cierres.

## Proyecto académico

Desarrollado como parte del proyecto semestral de Desarrollo de Aplicaciones Móviles.
