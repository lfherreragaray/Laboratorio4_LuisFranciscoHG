# Parque Turístico de Dinosaurios - Simulación 

## Herramientas utilizadas 
- Java 17 
- Maven 3.9+ 
- JUnit 5, Mockito 
- JaCoCo para cobertura 

## Configuración 
1. Clonar el repositorio. 
2. Verificar que `park.properties` esté en `src/main/resources/`. 
3. Asegurarse de tener configurada la variable de entorno `JAVA_HOME` apuntando a JDK 17. 

## Explicación del sistema Simulación secuencial de un parque de dinosaurios con llegada de turistas, zonas (souvenirs, baños, encierros), planta de energía, eventos aleatorios, trabajadores y persistencia en CSV. 

## Comandos 
### 1. Limpiar builds anteriores 
```bash mvn clean ``` 
Elimina la carpeta `target/` y cualquier artefacto compilado previamente. 
### 2. Compilar el proyecto 
```bash mvn compile ``` 
Compila las fuentes Java y verifica dependencias. 
### 3. Ejecutar pruebas y generar reporte de cobertura 
```bash mvn test jacoco:report ``` 
- Corre todos los tests con JUnit 5. 
- Mockito se usa para simular dependencias. 
- JaCoCo genera un reporte de cobertura en `target/site/jacoco/index.html`. 
### 4. Ejecutar la simulación 
```bash mvn exec:java ``` 

## Diagramas UML

### Diagrama de secuencia – Bucle principal de simulación
![Secuencia del bucle](docs/diagrams/Bucle principal de simulación.png)