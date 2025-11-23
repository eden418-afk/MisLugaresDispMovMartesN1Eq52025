# MisLugares

**MisLugares** es una aplicación Android que permite gestionar una colección de lugares de interés: verlos en una lista, consultar su ficha detallada, localizarlos en un mapa, llamar por teléfono, abrir su página web, valorarlos y asociarles fotografías desde la cámara o la galería.

El proyecto forma parte del libro **“El gran libro de Android (9ª edición)”**. A lo largo del libro se van construyendo distintos ejercicios (Activities, intents, RecyclerView, SQLite, preferencias, localización, cámara, etc.) que culminan en esta aplicación final.

---

## Requisitos e instalación

**Requisitos:**

- Android Studio (versión reciente compatible con proyectos Gradle).
- JDK configurado en el sistema (por ejemplo, JDK 17).
- SDK de Android instalado (minSdk y targetSdk según el `build.gradle` del proyecto).
- Dispositivo físico Android o emulador configurado (AVD).

**Instalación:**

1. Clonar el repositorio del proyecto:

   ```bash
   git clone https://github.com/usuario/mislugares.git
   cd mislugares

    Abrir la carpeta del proyecto en Android Studio (File > Open y seleccionar la carpeta raíz).

    Esperar a que Android Studio sincronice el proyecto con Gradle.
    Durante este paso se descargarán automáticamente las dependencias necesarias (AndroidX, componentes de preferencias, etc.).

    Si Android Studio muestra advertencias de SDK, abrir el SDK Manager e instalar la versión de Android solicitada por el proyecto.

Cómo usarlo
Ejecutar la aplicación

    Conectar un dispositivo Android con el modo desarrollador activado, o crear un emulador desde el AVD Manager.

    En Android Studio, seleccionar la configuración de ejecución app.

    Elegir el dispositivo o emulador donde se va a ejecutar.

    Pulsar el botón Run (▶).
    La app se compilará, se instalará en el dispositivo y se abrirá automáticamente.

La primera vez que se use, la aplicación puede solicitar permisos (por ejemplo, de localización o llamadas); es necesario aceptarlos para que todas las funciones funcionen correctamente.
Uso dentro de la aplicación

Una vez abierta MisLugares:

    Pantalla principal (MainActivity)

        Muestra una lista de lugares en un RecyclerView.

        Al pulsar sobre un elemento, se abre la ficha detallada del lugar.

        El botón flotante (FAB) permite crear un nuevo lugar.

    Ficha de lugar (VistaLugarActivity)

        Muestra nombre, tipo, dirección, teléfono, URL, comentarios, fecha/hora, foto y valoración.

        Desde esta pantalla se puede:

            Compartir el lugar (texto + enlace a Google Maps).

            Ver el lugar en el mapa.

            Realizar una llamada telefónica al lugar (si tiene número).

            Abrir la página web asociada.

            Tomar una fotografía con la cámara o seleccionar una desde la galería.

            Editar o borrar el lugar.

    Edición de lugar (EdicionLugarActivity)

        Permite modificar nombre, dirección, teléfono, URL, comentarios y tipo de lugar.

        Al guardar, se actualiza la información en la base de datos.

    Preferencias

        Desde el menú se accede a la pantalla de ajustes, donde se puede:

            Definir el máximo de lugares a mostrar.

            Elegir el criterio de ordenación (por defecto, por valoración o por proximidad).

Estructura del proyecto

La estructura general del proyecto Android es la siguiente:

app/
├─ manifests/
│  └─ AndroidManifest.xml
├─ java/
│  └─ com/example/mislugares/
│     ├─ modelo/        # Clases de dominio: Lugar, GeoPunto, TipoLugar
│     ├─ datos/         # Repositorios y acceso a SQLite
│     ├─ casos_uso/     # Lógica de negocio y navegación
│     └─ presentacion/  # Activities, adaptadores, clase Aplicacion
├─ res/
│  ├─ layout/           # Layouts de Activities, fragments, ítems de lista
│  ├─ menu/             # Menús de la barra de acciones
│  ├─ drawable/         # Iconos e imágenes
│  ├─ mipmap/           # Iconos de la app
│  ├─ values/           # Cadenas, estilos, colores, arrays
│  └─ xml/              # Preferencias, FileProvider, etc.
└─ README.md

Resumen de las capas:

    modelo/
    Define el modelo de dominio:

        Lugar: representa cada lugar con sus datos (nombre, dirección, coordenadas, tipo, foto, contacto, valoración).

        GeoPunto: almacena latitud y longitud y permite calcular distancias.

        TipoLugar: enum con los tipos de lugares y su icono.

    datos/
    Gestiona la persistencia:

        RepositorioLugares: interfaz de repositorio.

        LugaresBD: implementación con SQLite (SQLiteOpenHelper).

        LugaresDBAdapter: integra la base de datos con el adaptador del RecyclerView.

        LugaresLista: implementación en memoria para pruebas.

    casos_uso/
    Agrupa la lógica de negocio:

        CasosUsoLugar: operaciones de alto nivel sobre lugares (mostrar, editar, borrar, compartir, llamada, mapas, fotos).

        CasosUsoLocalizacion: permisos y obtención de la localización actual.

        CasosUsoActividades: navegación a pantallas genéricas (Acerca de, Preferencias).

    presentacion/
    Implementa la interfaz de usuario:

        MainActivity, VistaLugarActivity, EdicionLugarActivity, MapaActivity, etc.

        AdaptadorLugaresBD para el RecyclerView.

        Aplicacion como clase Application que inicializa el repositorio y la posición actual.

Esta estructura por capas facilita entender el proyecto, mantenerlo y extenderlo con nuevas funcionalidades sin mezclar la lógica de negocio con la interfaz ni con los detalles de acceso a datos.
