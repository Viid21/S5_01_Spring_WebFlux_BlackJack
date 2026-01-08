# 📘 **Blackjack API – CI/CD Deployment with Docker, GitHub Packages & Render**

## 📝 **Project Overview**
This project implements a fully reactive Blackjack game backend using **Spring WebFlux**, connected to **PostgreSQL** (via R2DBC) and **MongoDB Atlas**.  
The application is containerized with **Docker**, stored in **GitHub Packages**, and deployed automatically to **Render** using a **GitHub Actions CI/CD pipeline**.

The goal of this assignment is to demonstrate a complete, professional deployment workflow.

## 🚀 **Project endpoints**
```
https://springwebflux-blackjack.onrender.com/swagger-ui.html
```

---

# 🏗️ **Architecture**

```
Local Development → GitHub Repo → GitHub Actions → GitHub Packages → Render → Production
```

### **Technologies Used**
- **Spring WebFlux** (Reactive backend)
- **R2DBC** (Reactive PostgreSQL driver)
- **MongoDB Reactive Driver**
- **Docker** (Containerization)
- **GitHub Packages** (Container registry)
- **GitHub Actions** (CI/CD pipeline)
- **Render** (Cloud deployment)

---

# 🐳 **Docker Setup**

### **Build the Docker image locally**
```bash
docker build -t blackjack:latest .
```

### **Run the container**
```bash
docker run -p 8080:8080 blackjack:latest
```

### **Required environment variables**
The application needs the following variables to run:

```
SPRING_R2DBC_URL
SPRING_R2DBC_USERNAME
SPRING_R2DBC_PASSWORD
SPRING_DATA_MONGODB_URI
```

These are configured in Render for production.

---

# 📦 **Publishing the Docker Image to GitHub Packages**

### **1. Authenticate to GitHub Container Registry**
```bash
echo <GHCR_TOKEN> | docker login ghcr.io -u viid21 --password-stdin
```

### **2. Build the image with the correct lowercase tag**
```bash
docker build -t ghcr.io/viid21/blackjack:latest .
```

### **3. Push the image**
```bash
docker push ghcr.io/viid21/blackjack:latest
```

GitHub Packages now stores the production-ready image.

---

# 🚀 **Render Deployment**

Render is configured to pull the image from:

```
ghcr.io/viid21/blackjack:latest
```

### **Environment variables in Render**
- PostgreSQL credentials (from Render PostgreSQL service)
- MongoDB URI (from MongoDB Atlas)
- `PORT` (Render uses this automatically)

### **Automatic redeploy**
Render redeploys automatically whenever GitHub Actions triggers a new deployment.

---

# 🔄 **GitHub Actions CI/CD Pipeline**

The CI/CD workflow is located at:

```
.github/workflows/deploy-to-render.yml
```

### **What the pipeline does**
1. Checks out the repository  
2. Logs into GitHub Packages  
3. Builds the Docker image  
4. Pushes the image to `ghcr.io`  
5. Sends a deploy request to Render’s API  

### **Required GitHub Secrets**
You must configure these in:

**GitHub → Repository → Settings → Secrets → Actions**

- `GHCR_TOKEN` – Token with `write:packages`
- `RENDER_EMAIL` – Render account email
- `RENDER_PASSWORD` – Render account password

---

# 🌐 **API Documentation**

### **Swagger UI**
```
https://springwebflux-blackjack.onrender.com/swagger-ui.html
```

### **OpenAPI JSON**
```
https://springwebflux-blackjack.onrender.com/v3/api-docs
```

### **Main Endpoints**
- **PUT `/player/{id}`**  
  Update an existing player's name.

- **GET `/ranking`**  
  Retrieve the global player ranking.
  
- **POST `/game/new`**  
  Create a new Blackjack game.

- **POST `/game/{id}/play`**  
  Make a move in an existing game (e.g., hit, stand, etc.).

- **GET `/game/{id}`**  
  Get the current state of a specific game.

- **DELETE `/game/{id}/delete`**  
  Delete a game from the system.
  
---

# 🧪 **Testing the API**

You can test the API using:

- Swagger UI  
- Postman  
- cURL  
- Any HTTP client  

Example:

```bash
curl -X POST https://springwebflux-blackjack.onrender.com/api/blackjack/start
```

---

# 🛠️ **Common Issues & Solutions**

### **1. Docker tag must be lowercase**
Solution:  
Use only lowercase in the image tag:
```
ghcr.io/viid21/blackjack:latest
```

### **2. MongoDB “Database name must not be empty”**
Solution:  
Ensure the URI ends with a database name:
```
mongodb+srv://user:pass@cluster.mongodb.net/blackjack
```

### **3. SSLException connecting to MongoDB Atlas**
Solution:  
Add this IP range in Atlas → Network Access:
```
0.0.0.0/0
```

### **4. 404 on Render root URL**
Normal.  
Use `/swagger-ui.html`.

---

# ✅ **Conclusion**

This project demonstrates a complete, production-ready CI/CD workflow using:

- Docker  
- GitHub Packages  
- GitHub Actions  
- Render  
- Spring WebFlux  

Every push to `main` automatically builds, publishes, and deploys the application.
