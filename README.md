
# 🌎 Visualizador de Datos de Cambio Climático

Aplicación web desarrollada para la **visualización y análisis de datos climáticos** provenientes de distintas fuentes y estaciones de medición.  
Permite explorar variables como temperatura, concentración de CO₂ y nivel del mar a lo largo del tiempo y en diferentes ubicaciones.

---

## 🧱 Tecnologías utilizadas

- **Backend:** Spring Boot (Java)
- **Frontend:** Vue.js
- **Base de Datos:** PostgreSQL
- **Gestión de dependencias:** Maven y npm

---

## ⚙️ Configuración del entorno

Antes de ejecutar la aplicación, asegúrate de contar con:

- **Java 17** o superior  
- **Node.js** (v18 o superior) y **npm**  
- **PostgreSQL** en ejecución

La base de datos debe estar **creada y configurada previamente**, junto con sus tablas y datos iniciales.  
Se recomienda ejecutar los scripts de creación y poblamiento disponibles en el directorio `/dataBase` del proyecto.

---

## 🚀 Ejecución del proyecto

### 1️⃣ Backend (Spring Boot)

```bash
cd climateViewer
mvn spring-boot:run
````

El servidor backend se ejecutará en:
➡️ **[http://localhost:8080](http://localhost:8080)**

---

### 2️⃣ Frontend (Vue.js)

```bash
cd frontend
npm install
npm install leaflet
npm run dev
```

La aplicación frontend estará disponible en:
➡️ **[http://localhost:3000](http://localhost:3000)**

---

## 🧩 Estructura del proyecto

```
visualizador-datos-climaticos/
├── climateViewer/                 # Código fuente del backend (Spring Boot)
│   ├── src/                 # Controladores, servicios y repositorios
│   ├── pom.xml              # Dependencias Maven
│   └── ...
├── frontend/                # Código fuente del frontend (Vue.js)
│   ├── src/                 # Componentes y vistas
│   ├── package.json         # Dependencias npm
│   └── ...
├── dataBase/                     # Scripts SQL de creación y poblamiento de la base de datos
└── README.md                # Documentación principal del proyecto
```

---

## 🧠 Descripción general

El sistema se conecta a una base de datos PostgreSQL que almacena información de usuarios, datasets , puntos de medición y registros de observaciones.
A través del frontend, los usuarios pueden visualizar los datos de manera interactiva y analizar las tendencias del cambio climático.
