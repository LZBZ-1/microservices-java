# com.lzbz

Este proyecto contiene el código fuente para la aplicación **com.lzbz**.

## Estructura del Proyecto

El proyecto se organiza en los siguientes directorios principales:

### `business`

Contiene el código relacionado con la lógica de negocio, incluyendo:

- **bff**: Backend for Frontend
- **client**: Código relacionado con el cliente (Puerto: `8081`)
- **product**: Código relacionado con productos financieros (Puerto: `8082`)

### `infrastructure`

Contiene configuraciones de infraestructura, incluyendo:

- **eurekaServer**: Configuración del servidor Eureka
- **springAdmin**: Configuración de Spring Admin

### `External Libraries`

Bibliotecas externas utilizadas en el proyecto.

## Requisitos

- **Java 11** o superior
- **Maven 3.6.0** o superior

## Compilación

Para compilar el proyecto, ejecuta el siguiente comando en la raíz del proyecto:

```bash
mvn clean install
```

## Ejecución
Para ejecutar la aplicación, usa Docker Compose:

```bash
docker-compose up
```
La aplicación BFF estará disponible en http://localhost:8080/swagger-ui.html.

## Contribución
Si deseas contribuir a este proyecto, por favor crea un Pull Request con tus cambios. Asegúrate de seguir las guías de estilo de código y proporcionar pruebas unitarias cuando sea necesario.

## Licencia
Este proyecto está licenciado bajo los términos de la licencia MIT. Consulta el archivo LICENSE para más detalles.
