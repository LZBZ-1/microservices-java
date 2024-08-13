com.lzbz
Este proyecto contiene el código fuente para la aplicación com.lzbz.
Estructura del proyecto
El proyecto se organiza en los siguientes directorios principales:

business: Contiene el código relacionado con la lógica de negocio, incluyendo:

bff: Backend for Frontend
client: Código del cliente
product: Código relacionado con productos


infrastructure: Contiene configuraciones de infraestructura, incluyendo:

eurekaServer: Configuración del servidor Eureka
springAdmin: Configuración de Spring Admin


External Libraries: Bibliotecas externas utilizadas en el proyecto

Requisitos

Java 11 o superior
Maven 3.6.0 o superior

Compilación
Para compilar el proyecto, ejecuta el siguiente comando en la raíz del proyecto:
Copymvn clean install
Ejecución
Para ejecutar la aplicación, utiliza el siguiente comando:
Copymvn spring-boot:run
La aplicación estará disponible en http://localhost:8080.
Contribución
Si deseas contribuir a este proyecto, por favor crea un Pull Request con tus cambios. Asegúrate de seguir las guías de estilo de código y proporcionar pruebas unitarias cuando sea necesario.
Licencia
Este proyecto está licenciado bajo los términos de la licencia MIT. Consulta el archivo LICENSE para más detalles.
