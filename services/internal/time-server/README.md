# Time Service
The time service is responsible, primarily, for providing global tick time, as well as emitting tick events on a
schedule (`Constants.TIME_TICK_RATE_MS`).  The global tick time is also stored in redis.