package org.slack.vpnupdater.model;

import javafx.beans.property.*;

public class Vpn {
    private final StringProperty country;
    private final StringProperty ip;
    private final StringProperty port;
    private final StringProperty protocol;
    private final BooleanProperty ddos;
    private final StringProperty uuid;

    public Vpn(String country, String ip, String port, String protocol, Boolean ddos, String uuid) {
        //this(null, null, null, null);
        this.country = new SimpleStringProperty(country);
        this.ip = new SimpleStringProperty(ip);
        this.port = new SimpleStringProperty(port);
        this.protocol = new SimpleStringProperty(protocol);
        this.ddos = new SimpleBooleanProperty(ddos);
        this.uuid = new SimpleStringProperty(uuid);
    }

    public Vpn() {
        this(null, null, null, null, false, null);
    }

    public String getCountry() {
        return country.get();
    }

    public StringProperty countryProperty() {
        return country;
    }

    public void setCountry(String country) {
        this.country.set(country);
    }

    public String getIp() {
        return ip.get();
    }

    public StringProperty ipProperty() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip.set(ip);
    }

    public String getPort() {
        return port.get();
    }

    public StringProperty portProperty() {
        return port;
    }

    public void setPort(String port) {
        this.port.set(port);
    }

    public String getProtocol() {
        return protocol.get();
    }

    public StringProperty protocolProperty() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol.set(protocol);
    }

    public boolean isDdos() {
        return ddos.get();
    }

    public BooleanProperty ddosProperty() {
        return ddos;
    }

    public void setDdos(boolean ddos) {
        this.ddos.set(ddos);
    }

    public String getUuid() {
        return uuid.get();
    }

    public StringProperty uuidProperty() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid.set(uuid);
    }
}
