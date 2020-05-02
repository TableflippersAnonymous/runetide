package com.runetide.common.services.cql;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CassandraConfig {
    private String keyspace;
    private List<String> contactPoints;
    private int port = 9042;
    private boolean authenticated = false;
    private String username;
    private String password;

    private String consistencyLevel = "LOCAL_ONE";
    private String serialConsistencyLevel = "SERIAL";
    private int fetchSize = 5000;

    private long reconnectBaseDelayMs = 100;
    private long reconnectMaxDelayMs = 5000;

    private int connectTimeoutMs = 5000;
    private boolean tcpKeepAlive = true;

    @JsonProperty("keyspace")
    public String getKeyspace() {
        return keyspace;
    }

    @JsonProperty("keyspace")
    public void setKeyspace(final String keyspace) {
        this.keyspace = keyspace;
    }

    @JsonProperty("contact_points")
    public List<String> getContactPoints() {
        return contactPoints;
    }

    @JsonProperty("contact_points")
    public void setContactPoints(final List<String> contactPoints) {
        this.contactPoints = contactPoints;
    }

    @JsonProperty("port")
    public int getPort() {
        return port;
    }

    @JsonProperty("port")
    public void setPort(final int port) {
        this.port = port;
    }

    @JsonProperty("authenticated")
    public boolean isAuthenticated() {
        return authenticated;
    }

    @JsonProperty("authenticated")
    public void setAuthenticated(final boolean authenticated) {
        this.authenticated = authenticated;
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    @JsonProperty("username")
    public void setUsername(final String username) {
        this.username = username;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    @JsonProperty("password")
    public void setPassword(final String password) {
        this.password = password;
    }

    @JsonProperty("consistency_level")
    public String getConsistencyLevel() {
        return consistencyLevel;
    }

    @JsonProperty("consistency_level")
    public void setConsistencyLevel(final String consistencyLevel) {
        this.consistencyLevel = consistencyLevel;
    }

    @JsonProperty("serial_consistency_level")
    public String getSerialConsistencyLevel() {
        return serialConsistencyLevel;
    }

    @JsonProperty("serial_consistency_level")
    public void setSerialConsistencyLevel(final String serialConsistencyLevel) {
        this.serialConsistencyLevel = serialConsistencyLevel;
    }

    @JsonProperty("fetch_size")
    public int getFetchSize() {
        return fetchSize;
    }

    @JsonProperty("fetch_size")
    public void setFetchSize(final int fetchSize) {
        this.fetchSize = fetchSize;
    }

    @JsonProperty("reconnect_base_delay_ms")
    public long getReconnectBaseDelayMs() {
        return reconnectBaseDelayMs;
    }

    @JsonProperty("reconnect_base_delay_ms")
    public void setReconnectBaseDelayMs(final long reconnectBaseDelayMs) {
        this.reconnectBaseDelayMs = reconnectBaseDelayMs;
    }

    @JsonProperty("reconnect_max_delay_ms")
    public long getReconnectMaxDelayMs() {
        return reconnectMaxDelayMs;
    }

    @JsonProperty("reconnect_max_delay_ms")
    public void setReconnectMaxDelayMs(final long reconnectMaxDelayMs) {
        this.reconnectMaxDelayMs = reconnectMaxDelayMs;
    }

    @JsonProperty("connect_timeout_ms")
    public int getConnectTimeoutMs() {
        return connectTimeoutMs;
    }

    @JsonProperty("connect_timeout_ms")
    public void setConnectTimeoutMs(final int connectTimeoutMs) {
        this.connectTimeoutMs = connectTimeoutMs;
    }

    @JsonProperty("tcp_keep_alive")
    public boolean isTcpKeepAlive() {
        return tcpKeepAlive;
    }

    @JsonProperty("tcp_keep_alive")
    public void setTcpKeepAlive(final boolean tcpKeepAlive) {
        this.tcpKeepAlive = tcpKeepAlive;
    }
}
