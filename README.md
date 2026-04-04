<h1>
  <img src=".readme/tank.png" width="80" style="vertical-align: middle; margin-right: 10px;" alt="tank">
  TankingTanks DBSM
</h1>

**TankingTanks** is a database system designed to store and manage user preferences about tanks.  
From tracking **favorite tanks** to storing **user ratings**, the system provides a structured way to organize and explore tank data.

This **Database System Manager (DBSM)** handles the core **CRUD operations** required to interact with the database efficiently.

> [The database](https://github.com/Crocodin/University_projects/tree/main/semester%203/DB)
---
## Index
1. [Run config & about](About.md) <br>
Information on how to run the app, a small preview and some challenges I faced during the making of the applications.
2. [Hibernate migration and HikariCP stats](HibernateChanges.md) <br>
The process of migration the original app to **Hibernate** and adding _Logs_ via **Log4j2** and using _connection pooling_ via *HikariCP* including some performances statistic between JDBC making a connection for every operation vs connection pooling and _IoC_ via **Spring**.