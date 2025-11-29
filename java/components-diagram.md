# Diagrama de Componentes (C4 Nivel 3)

```mermaid
graph TB
    subgraph "AstroBookings Application"
        subgraph "Application Layer"
            RH[RocketHandler]
            FH[FlightHandler]
            BH[BookingHandler]
            AH[AdminHandler]
            BASE[BaseHandler]
        end
        
        subgraph "Business Layer"
            MODELS[Request Models]
            RS[RocketService]
            FS[FlightService]
            BS[BookingService]
            FCS[FlightCancellationService]
            PG[PaymentGateway]
            NS[NotificationService]
            EXC[Custom Exceptions]
        end
        
        subgraph "Persistence Layer"
            RR[RocketRepository]
            FR[FlightRepository]
            BR[BookingRepository]
        end
        
        subgraph "Domain Models"
            ROCKET[Rocket]
            FLIGHT[Flight]
            BOOKING[Booking]
            STATUS[FlightStatus]
        end
    end
    
    subgraph "External Systems"
        PAYMENT[Payment Gateway API]
        EMAIL[Email Service]
    end
    
    CLIENT[HTTP Client]
    
    CLIENT -->|HTTP/JSON| RH
    CLIENT -->|HTTP/JSON| FH
    CLIENT -->|HTTP/JSON| BH
    CLIENT -->|HTTP/JSON| AH
    
    RH --> BASE
    FH --> BASE
    BH --> BASE
    AH --> BASE
    
    RH -->|CreateRocketRequest| MODELS
    FH -->|CreateFlightRequest| MODELS
    BH -->|CreateBookingRequest| MODELS
    
    MODELS --> RS
    MODELS --> FS
    MODELS --> BS
    
    RH --> RS
    FH --> FS
    BH --> BS
    AH --> FCS
    
    RS --> RR
    FS --> FR
    FS --> RR
    
    BS --> BR
    BS --> FR
    BS --> RR
    BS --> PG
    BS --> NS
    
    FCS --> FR
    FCS --> BR
    FCS --> PG
    FCS --> NS
    
    RS --> EXC
    FS --> EXC
    BS --> EXC
    
    BASE --> EXC
    
    PG -.->|Simulated| PAYMENT
    NS -.->|Simulated| EMAIL
    
    RR --> ROCKET
    FR --> FLIGHT
    BR --> BOOKING
    FLIGHT --> STATUS
    
    style PG fill:#00ccff,stroke:#333
    style NS fill:#00ccff,stroke:#333
    style MODELS fill:#90EE90,stroke:#333
    style EXC fill:#FFB6C1,stroke:#333
    style RS fill:#FFFFE0,stroke:#333
    style PAYMENT fill:#ddd,stroke:#333,stroke-dasharray: 5 5
    style EMAIL fill:#ddd,stroke:#333,stroke-dasharray: 5 5
```

**Leyenda**:
- **Líneas sólidas**: Dependencias directas
- **Líneas punteadas**: Servicios externos simulados
- **Azul**: Gateways a servicios externos
- **Verde**: Request Models (anemic records en business/models)
- **Rosa**: Excepciones personalizadas
- **Amarillo**: RocketService (añadido para eliminar acceso directo a repositorio)
- **Gris**: Servicios externos (no implementados)
