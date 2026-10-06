# Java Zombie Apocalypse

Concurrent simulation of a zombie apocalypse written in Java. Every human and every zombie is an independent thread; humans leave a shared refuge through four tunnels to collect food in risk zones while zombies roam those zones attacking them. A Swing GUI shows the state of every zone in real time, and a second GUI client connects through Java RMI to monitor the simulation and pause or resume it remotely.

Developed as a lab project for the *Programación Avanzada* (Advanced Programming) course at the University of Alcalá (UAH), 2024–2025 academic year.

## How the simulation works

- **Humans** (`backend/entities/Humano.java`) repeat a cycle: wait in the common area, pick a random tunnel, cross it to a risk zone, explore and collect 2 units of food, come back through the tunnel, drop the food in the refuge, rest, eat and, if they were wounded, recover.
- **Zombis** (`backend/entities/Zombi.java`) move between the four risk zones and attack a random human found there. A human defends themself with a 2/3 probability (and comes back wounded); otherwise they die and turn into a new zombie. Each zombie keeps a kill count.
- **Tunnels** (`backend/zones/Tunel.java`) only let humans leave in groups of three (`CyclicBarrier`) and only one human can be inside a tunnel at a time (`Semaphore`); humans returning to the refuge have priority.
- **Refuge** (`backend/zones/Refugio.java`) stores the shared food; access is guarded by a semaphore and synchronized methods.
- **Global pause** (`backend/utils/PausaGlobal.java`) lets every thread stop and resume together.
- Ids are generated with `AtomicInteger` counters and the position of every entity is tracked in a `ConcurrentHashMap` (`backend/zones/MapaZonas.java`).
- Every event is appended with a timestamp to `apocalipsis.txt` (`backend/utils/ApocalipsisLogger.java`).

The main program (`Simulador`) starts the first zombie and then creates up to 10,000 humans, one every 0.5–2 seconds.

## Remote monitoring (RMI)

`Simulador` creates an RMI registry on port `1099` and binds the remote object `//localhost/objeto` (`backend/server/ServidorRMI.java`, interface `ServidorRemoto`). The client (`frontend/client/ClienteGUI.java`) uses it to show:

- humans inside the refuge and inside each tunnel,
- humans and zombies in each risk zone,
- the top 3 deadliest zombies,

and to pause or resume the simulation.

## Tech stack

- Java 17 (Maven project)
- Threads and `java.util.concurrent` (`CyclicBarrier`, `Semaphore`, `AtomicInteger`, `ConcurrentHashMap`) plus monitors (`synchronized`)
- Java RMI
- Swing (forms designed with NetBeans, `AbsoluteLayout` dependency)

## Project structure

```
ApocalipsisZombi/
├── pom.xml
└── src/main/java/
    ├── programacion/avanzada/apocalipsiszombi/Simulador.java   # entry point (server)
    ├── backend/
    │   ├── entities/   # Humano, Zombi (threads)
    │   ├── zones/      # MapaZonas, Refugio, Tunel, Zona
    │   ├── server/     # ServidorRemoto (RMI interface), ServidorRMI
    │   └── utils/      # ApocalipsisLogger, IdGenerator, PausaGlobal
    └── frontend/
        ├── server/     # ApocalipsisGUI + updater thread
        └── client/     # ClienteGUI (RMI client) + updater thread
```

## Build and run

Requirements: JDK 17+ and Maven.

```bash
cd ApocalipsisZombi
mvn compile

# 1. Start the simulation (server GUI + RMI registry on port 1099)
mvn exec:java -Dexec.mainClass=programacion.avanzada.apocalipsiszombi.Simulador

# 2. In another terminal, start the remote monitoring client
mvn exec:java -Dexec.mainClass=frontend.client.ClienteGUI
```

The project can also be opened directly in NetBeans or IntelliJ IDEA and run from `Simulador` and `ClienteGUI`.

## Authors

- Adrián Morales Rodríguez ([@crest4s](https://github.com/crest4s))
- [@Hugoserrano2005](https://github.com/Hugoserrano2005)

## License

[MIT](LICENSE)
