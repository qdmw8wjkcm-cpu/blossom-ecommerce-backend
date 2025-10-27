# Proyecto de E-commerce con Arquitectura Modular Monolítica

Este proyecto es una aplicación de comercio electrónico desarrollada en Java con Spring Boot, diseñada bajo una arquitectura de monolito modular.

## Descripción

El objetivo de este proyecto es construir una plataforma de e-commerce robusta y escalable, aprovechando las ventajas de una arquitectura modular que permite una clara separación de responsabilidades y facilita el mantenimiento y la evolución del sistema.

## Tecnologías y Dependencias

El proyecto utiliza un conjunto de tecnologías modernas para su desarrollo:

*   **Lenguaje**: Java 21
*   **Framework Principal**: Spring Boot 3.5.7
*   **Arquitectura**: Spring Modulith para la implementación del monolito modular.
*   **Acceso a Datos**:
    *   Spring Data JPA para la persistencia.
    *   PostgreSQL como base de datos de producción.
    *   H2 como base de datos en memoria para pruebas y desarrollo.
*   **Mensajería Asíncrona**:
    *   Spring AMQP y RabbitMQ para una comunicación basada en eventos entre módulos.
*   **Seguridad**:
    *   Spring Security para la autenticación y autorización.
    *   JSON Web Tokens (JWT) para la gestión de sesiones.
*   **APIs**:
    *   Spring Web para la creación de APIs REST.
    *   SpringDoc OpenAPI (Swagger) para la documentación automática de la API.
*   **Herramientas de Desarrollo**:
    *   Lombok para reducir el código repetitivo.
    *   MapStruct para el mapeo eficiente y seguro entre objetos (DTOs y Entidades).
    *   Spring Boot DevTools para el reinicio automático y otras utilidades de desarrollo.
*   **Gestión de Dependencias y Construcción**: Maven
*   **Variables de Entorno**: `spring-dotenv` para cargar variables desde un archivo `.env`.

## Arquitectura y Patrones de Diseño

### Monolito Modular

La aplicación sigue un enfoque de **monolito modular**. En lugar de un gran bloque de código, la aplicación se organiza en módulos cohesivos que representan diferentes dominios de negocio (por ejemplo, `Orders`, `Products`, `Users`).

**Ventajas**:
*   **Alta Cohesión y Bajo Acoplamiento**: Cada módulo tiene una responsabilidad bien definida y las interacciones entre ellos se gestionan a través de interfaces públicas o eventos.
*   **Facilidad de Mantenimiento**: Es más fácil entender, modificar y probar un módulo individualmente.
*   **Escalabilidad Selectiva**: Aunque es un monolito, la separación lógica prepara el camino para una futura extracción a microservicios si fuera necesario.

### Patrones de Diseño Utilizados

*   **Inyección de Dependencias (DI)**: Utilizado extensivamente por el framework Spring para gestionar los componentes de la aplicación.
*   **Patrón Repositorio**: Abstracción del acceso a datos proporcionada por Spring Data JPA.
*   **Data Transfer Object (DTO)**: Se utilizan DTOs para transferir datos entre las capas de la aplicación (especialmente entre el controlador y el servicio), y MapStruct se encarga de la conversión entre DTOs y entidades.
*   **Arquitectura Orientada a Eventos**: La comunicación entre módulos se realiza de forma asíncrona mediante eventos y un bus de mensajes (RabbitMQ), lo que desacopla los módulos.
*   **Fachada (Facade)**: Los servicios a menudo actúan como una fachada para la lógica de negocio subyacente.

## Estructura del Proyecto

El código fuente se encuentra en `src/main/java/org/blossom/ecommerce`. Dentro de este paquete, los diferentes módulos de negocio se organizan en sus propios paquetes.

```
src
└── main
    └── java
        └── org
            └── blossom
                └── ecommerce
                    ├── Orders      // Módulo de Pedidos
                    ├── Products    // Módulo de Productos
                    ├── Users       // Módulo de Usuarios
                    └── ...         // Otros módulos de negocio
```

## Cómo Empezar

### Prerrequisitos

*   JDK 21 o superior.
*   Apache Maven 3.8 o superior.
*   Docker y Docker Compose (recomendado para levantar servicios como PostgreSQL y RabbitMQ).
*   Crear un archivo `.env` en la raíz del proyecto para configurar las variables de entorno (ej. credenciales de la base de datos, secrets de JWT).

### Levantando los Servicios

Si usas Docker, puedes levantar los servicios necesarios con un archivo `docker-compose.yml`.

### Construcción del Proyecto

1.  Clona el repositorio.
2.  Navega a la raíz del proyecto y ejecuta el siguiente comando para compilar y empaquetar la aplicación:
    ```bash
    mvn clean install
    ```

### Ejecución de la Aplicación

Puedes ejecutar la aplicación de varias maneras:

1.  **Usando el plugin de Maven para Spring Boot**:
    ```bash
    mvn spring-boot:run
    ```

2.  **Ejecutando el archivo JAR generado**:
    ```bash
    java -jar target/ecommerce-0.0.1-SNAPSHOT.jar
    ```

Una vez que la aplicación esté en funcionamiento, estará disponible en `http://localhost:8080`.

## Documentación de la API

La documentación de la API se genera automáticamente con SpringDoc. Puedes acceder a la interfaz de Swagger UI en la siguiente URL mientras la aplicación se está ejecutando:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
