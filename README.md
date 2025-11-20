# GeoSpatial Match Service

> High-performance, event-driven geospatial indexing service capable of handling real-time driver location updates and radius-based search queries with sub-millisecond latency.

![Java](https://img.shields.io/badge/Java-25-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.7-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Kafka](https://img.shields.io/badge/Apache_Kafka-KRaft-231F20?style=for-the-badge&logo=apache-kafka&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white)

## Architecture

This project implements a **Command Query Responsibility Segregation (CQRS)** architecture backed by **Event Sourcing**. It uses an in-memory **QuadTree** for spatial indexing, enabling efficient `O(log N)` range queries.

```mermaid
graph TD
    Client(["Client / Driver"]) -->|POST Position| API["Spring Boot API"]
    Client -->|GET Search| API
    
    subgraph "Write Path (Async)"
        API -->|Produce Event| Kafka["Apache Kafka (KRaft)"]
        Kafka -->|Consume Event| Consumer["Location Consumer"]
    end
    
    subgraph "Read Path (In-Memory)"
        Consumer -->|Update| QT["QuadTree Engine"]
        API -->|Query| QT
    end
    
    subgraph "Persistence Strategy"
        QT -->|Scheduled Snapshot| Disk[("JSON Snapshot")]
        Disk -->|Bootstrap Load| QT
    end
