# Dex's - Aplicación de E-commerce de Ropa

## Descripción del Proyecto

**Dex's** es una aplicación móvil de e-commerce desarrollada en Android con Jetpack Compose, inspirada en la serie de televisión "Dexter". La aplicación permite a los usuarios explorar, agregar al carrito y comprar productos de ropa relacionados con los personajes de la serie.

## Características Principales

- 🛍️ **Catálogo de Productos**: Visualización de productos de ropa en una cuadrícula moderna
- 🛒 **Carrito de Compras**: Funcionalidad completa de carrito con animaciones
- 📍 **Página de Contacto**: Información de contacto con mapa integrado de Google Maps
- ⭐ **Sistema de Reseñas**: Reseñas de clientes con calificaciones por estrellas
- 🎵 **Música de Fondo**: Servicio de música de fondo con la canción "Perfidia"
- 🎨 **Diseño Moderno**: Interfaz de usuario moderna con Material Design 3

## Estructura del Proyecto

```
Dexs/
├── app/                                    # Módulo principal de la aplicación
│   ├── src/main/
│   │   ├── java/com/example/evidencia3/
│   │   │   ├── MainActivity.kt            # Actividad principal
│   │   │   ├── MainScreen.kt              # Pantalla principal con navegación
│   │   │   ├── Product.kt                 # Modelo de datos para productos
│   │   │   ├── CartPage.kt                # Pantalla del carrito de compras
│   │   │   ├── ClothingViewModel.kt       # ViewModel para gestión de estado
│   │   │   ├── DummyData.kt               # Datos de prueba
│   │   │   ├── BackgroundSoundService.kt  # Servicio de música de fondo
│   │   │   └── ui/theme/                  # Temas y colores de la aplicación
│   │   ├── res/                           # Recursos de la aplicación
│   │   │   ├── mipmap/                    # Iconos y logos
│   │   │   ├── drawable/                  # Imágenes y recursos gráficos
│   │   │   └── values/                    # Strings, colores, estilos
│   │   └── AndroidManifest.xml            # Configuración de la aplicación
│   └── build.gradle.kts                   # Configuración de dependencias del módulo
├── build.gradle.kts                       # Configuración global del proyecto
├── settings.gradle.kts                    # Configuración de módulos
└── README.md                              # Este archivo
```

## Tecnologías Utilizadas

- **Lenguaje**: Kotlin
- **UI Framework**: Jetpack Compose
- **Arquitectura**: MVVM (Model-View-ViewModel)
- **Navegación**: Modal Navigation Drawer
- **Mapas**: Google Maps API
- **Imágenes**: Coil para carga de imágenes
- **Animaciones**: Jetpack Compose Animations
- **Servicios**: Android Service para música de fondo

## Funcionalidades Implementadas

### 1. Pantalla Principal (Ventas)
- Grid de productos con imágenes
- Botones de "Agregar al carrito" con animaciones
- Barra de búsqueda (preparada para implementación)
- Navegación lateral con drawer

### 2. Carrito de Compras
- Lista de productos agregados
- Funcionalidad de eliminar productos
- Botón de checkout (preparado para implementación)
- Contador de items en el ícono del carrito

### 3. Página de Contacto
- Información de contacto (email y teléfono)
- Mapa integrado mostrando ubicación de Tecmilenio Las Torres
- Diseño moderno con cards redondeadas

### 4. Sistema de Reseñas
- Lista de reseñas de clientes
- Calificaciones por estrellas
- Avatares de personajes de la serie Dexter
- Comentarios de usuarios

### 5. Servicios de Fondo
- Reproducción automática de música de fondo
- Control de volumen optimizado
- Gestión del ciclo de vida del servicio

## Configuración del Proyecto

### Requisitos
- Android Studio Arctic Fox o superior
- Android SDK 24+ (Android 7.0)
- Kotlin 1.8+

### Instalación
1. Clona el repositorio
2. Abre el proyecto en Android Studio
3. Sincroniza las dependencias de Gradle
4. Ejecuta la aplicación en un dispositivo o emulador

### Dependencias Principales
- `androidx.compose.ui`: Interfaz de usuario con Compose
- `androidx.compose.material3`: Componentes Material Design 3
- `androidx.lifecycle`: Gestión del ciclo de vida
- `coil-compose`: Carga de imágenes
- `com.google.android.gms:play-services-maps`: Integración con Google Maps

## Personalización

### Temas y Colores
Los colores y temas se encuentran en `app/src/main/java/com/example/evidencia3/ui/theme/`:
- `Color.kt`: Paleta de colores personalizada
- `Theme.kt`: Configuración del tema de la aplicación
- `Type.kt`: Tipografías personalizadas

### Productos
Los productos se definen en `MainScreen.kt` y pueden ser modificados fácilmente agregando nuevos elementos a la lista `dexterClothes`