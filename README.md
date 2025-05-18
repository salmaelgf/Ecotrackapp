# EcoTrack: Carbon Footprint Tracking App 🌱

EcoTrack is a mobile and web application that empowers consumers to make sustainable food choices by providing real-time carbon footprint data, intelligent alternatives, and chatbot-powered assistance. Built using modern frameworks like Spring Boot, Android SDK, and Flask AI microservices, EcoTrack is an open-source solution designed for research, education, and practical climate action.

## Table of Contents
- [Architecture](#architecture)
- [Docker Deployment](#docker-deployment)
- [Features](#features)
- [Technical Stack](#technical-stack)
- [Backend Structure](#backend-structure)
- [Getting Started](#getting-started)
- [Screenshots](#screenshots)
- [Contributors](#contributors)
- [Contact](#contact)
- [License](#license)

## Architecture 🏗
EcoTrack is composed of:
- **Android Mobile App (Java)**: Barcode scanning, AI predictions, offline access.
- **Spring Boot Backend**: User auth, database, REST APIs.
- **Flask Microservice**: AI model predictions.
- **PostgreSQL DB**: Persistent product and user data.
- **Dockerized Services**: Easy deployment and scalability.

## Docker Deployment 

### Backend (`docker-compose.yml`):
Includes PostgreSQL, phpPgAdmin, and Spring Boot service.

### Android App (Local):
Built and run on Android Studio.

```bash
cd backend
docker-compose up -d
```

Access services:
- API: http://localhost:8082
- DB: localhost:5432

## Features 
- 🌿 **Barcode Scanning**: Retrieve product carbon data instantly.
- 💬 **EcoChat**: Smart chatbot assistant (AI + REST API).
- 🧠 **Carbon Prediction**: ML-powered inference for unknown values.
- 🔁 **Smart Alternatives**: Suggests eco-friendly product replacements.


## Technical Stack 🛠
- Android SDK, Retrofit, ZXing, TensorFlow Lite
- Spring Boot, Spring Security, PostgreSQL
- Flask, Scikit-learn, Docker, GitHub

## Backend Structure 📂
```
├── controller         # API routes
├── services           # Business logic
├── repository         # DB access layer
├── ai                 # AI integration (Flask APIs)
├── entities           # JPA models
├── sec                # JWT Auth & config
└── resources
```

## Getting Started 
### Prerequisites:
- Java 22, Node.js, PostgreSQL, Android Studio

### Setup:
```bash
git clone https://github.com/salmaelgf/ecotrack.git
cd ecotrack/backend
mvn spring-boot:run
```

- Android App:
  - Open in Android Studio
  - Set base URL for API in `Constants.java`
  - Build and run

## Screenshots 🖼
> Include images in `/screenshots` folder

## Contributors 👥
- Salma El Gouffi
- Khaoula Aguabdre

## Contact 📧
- salmaelgouffi@gmail.com



