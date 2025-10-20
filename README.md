# Task 2 – Kubernetes Deployment (Kaiburr Assessment)

This repository contains the **Kubernetes-based deployment (KTO)** of the Spring Boot application developed in **Task 1**.  
You can refer to Task 1 implementation here: [[Task 1](https://github.com/Adithya2369/Kaiburr_Task1)]

---

## 🧩 Overview

Task 2 focuses on containerizing and deploying the existing Spring Boot application and MongoDB into a **Kubernetes cluster**.  
It covers:
- Creating Docker images  
- Writing Kubernetes manifests for both the app and MongoDB  
- Connecting the services within the cluster  
- Testing CRUD operations using `curl` commands  

---

## 🗂️ Files and Structure

Because of the complexity of the complete project structure, only the directories and files relevant to **Task 2 (Kubernetes setup)** are shown below:

```
README.md
images/
task1/
├── Dockerfile
├── pom.xml
├── target/
│   └── task1-0.0.1-SNAPSHOT.jar
└── k8s/
    ├── mongo-pv.yaml
    ├── mongo-deployment.yaml
    ├── mongo-service.yaml
    ├── app-deployment.yaml
    └── app-service.yaml
```

---

## ⚙️ Key Components

| File | Description |
|------|--------------|
| **Dockerfile** | Builds a Docker image of the Spring Boot application |
| **mongo-pv.yaml** | Defines PersistentVolume and PersistentVolumeClaim for MongoDB |
| **mongo-deployment.yaml** | Deploys MongoDB pod in the cluster |
| **mongo-service.yaml** | Exposes MongoDB as an internal ClusterIP service |
| **app-deployment.yaml** | Deploys the Spring Boot app container |
| **app-service.yaml** | Exposes the application using NodePort for external access |

---

## 🚀 How to Use

Follow these steps to run this Task 2 project locally and deploy it on your own Kubernetes cluster:

1. **Clone the repository**

```bash
git clone https://github.com/Adithya2369/Kaiburr_Task1.git
cd <repo-folder>/task1
```

2. **Build the Docker image**

```bash
# Make sure your JAR is built first
mvnw clean package -DskipTests

# Build Docker image
docker build -t task1-app:latest .
```

3. **Push to Docker Hub**

```bash
docker tag task1-app:latest yourdockerhubusername/task1-app:latest
docker push yourdockerhubusername/task1-app:latest
```

Note: you need to update the above commands with you dockerhub username.

4. **Start Kubernetes cluster**

- **Docker Desktop:** Enable Kubernetes in settings and wait for it to start  
- **Minikube:** Run `minikube start`

Verify cluster is running:

```bash
kubectl get nodes
```

5. **Deploy MongoDB**

```bash
kubectl apply -f k8s/mongo-pv.yaml
kubectl apply -f k8s/mongo-deployment.yaml
kubectl apply -f k8s/mongo-service.yaml
```

6. **Deploy Spring Boot App**

```bash
kubectl apply -f k8s/app-deployment.yaml
kubectl apply -f k8s/app-service.yaml
```

7. **Verify pods and services**

```bash
kubectl get pods
kubectl get svc
```

8. **Test API using curl**

```bash
curl -X PUT http://localhost:30007/tasks -H "Content-Type: application/json" -d "{\"id\":\"1\",\"name\":\"HelloTask\",\"owner\":\"Adithya\",\"command\":\"echo Hello Kaiburr\"}"
curl http://localhost:30007/tasks
```

Now the app is up and running in your local Kubernetes cluster and connected to MongoDB.\
You will now be able to use the commands mentioned in Task1 repo. \

---


## 🧠 Error Handling

### ❌ Error: `failed to download openapi: Get "http://localhost:8080/openapi/v2?timeout=32s": dial tcp [::1]:8080: connectex: No connection could be made because the target machine actively refused it.`

**Cause:**  
This occurs when `kubectl` cannot connect to the Kubernetes API server — usually because Kubernetes is **not running**.

**Fix:**
1. If you’re using **Docker Desktop**:
   - Go to **Settings → Kubernetes**
   - Enable **“Enable Kubernetes”**
   - Wait until the status changes to **Running**

2. If you’re using **Minikube**:
   ```bash
   minikube start
   ```

3. Verify the cluster:
   ```bash
   kubectl get nodes
   ```
   Once you see a node like `docker-desktop` or `minikube`, retry applying your YAML files.

---

## ✅ Verification Commands

```bash
kubectl get pods
kubectl get svc
kubectl logs deployment/task1-app
```

Once the services are up:
```bash
curl -X PUT http://localhost:30007/tasks -H "Content-Type: application/json" -d "{\"id\":\"1\",\"name\":\"HelloTask\",\"owner\":\"Adithya\",\"command\":\"echo Hello Kaiburr\"}"
```

---

## ⏳ Pending Work – BusyBox TaskExecution

The remaining part of Task 2 is to implement **dynamic task execution using BusyBox pods**.  

- Each task contains a shell command in the `command` field.  
- The goal is to create a **temporary BusyBox pod** via the Kubernetes Java client that executes the task’s command.  
- After execution, the pod can be deleted automatically.  

Once this is implemented, Task 2 will be fully complete, demonstrating:
- Spring Boot + MongoDB integration on Kubernetes  
- Dynamic command execution via Kubernetes pods

---

## 👨‍💻 Author
**Adithya Reddy**  
Kaiburr Assessment 2025 — Task 2:   Kubernetes \
**Topic:** Creating and Managinging pods for task1 deployment

---

## 🔒 License Agreement
### Proprietary Rights Notice
This project and all associated materials, including but not limited to source code, documentation, models, and analysis, are the proprietary property of Kaiburr LLC.

---

## Copyright Notice
© 2025 Kaiburr LLC. All Rights Reserved.
