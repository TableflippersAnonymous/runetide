package com.runetide.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.redisson.config.Config;

public class ServiceConfiguration extends Configuration {
    private Config redissonConfig;
    private String zookeeperConnectionString;
    private CassandraConfig cassandraConfig;

    @JsonProperty("redisson")
    public Config getRedissonConfig() {
        return redissonConfig;
    }

    @JsonProperty("redisson")
    public void setRedissonConfig(final Config redissonConfig) {
        this.redissonConfig = redissonConfig;
    }

    @JsonProperty("zookeeper_connection_string")
    public String getZookeeperConnectionString() {
        return zookeeperConnectionString;
    }

    @JsonProperty("zookeeper_connection_string")
    public void setZookeeperConnectionString(final String zookeeperConnectionString) {
        this.zookeeperConnectionString = zookeeperConnectionString;
    }

    @JsonProperty("cassandra")
    public CassandraConfig getCassandraConfig() {
        return cassandraConfig;
    }

    @JsonProperty("cassandra")
    public void setCassandraConfig(final CassandraConfig cassandraConfig) {
        this.cassandraConfig = cassandraConfig;
    }

}
