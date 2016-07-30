package xserver.api.module.start.dto;

public class BootStrapRequest {
    private String terminalBrand;
    private String terminalSeries;
    private String terminalApplication;
    private String client;
    private String mac;
    private String installVersion;
    private String ip;
    private String langcode;
    private String pCode;

    public BootStrapRequest() {
        this.terminalBrand = "";
        this.terminalSeries = "";
        this.terminalApplication = "";
        this.client = "";
        this.mac = "";
        this.installVersion = "";
        this.ip = "";
        this.langcode = "";
        this.pCode = "";
    }

    public BootStrapRequest(String terminalBrand, String terminalSeries, String terminalApplication, String client,
            String mac, String installVersion, String ip, String langcode, String pCode) {
        this.terminalBrand = terminalBrand;
        this.terminalSeries = terminalSeries;
        this.terminalApplication = terminalApplication;
        this.client = client;
        this.mac = mac;
        this.installVersion = installVersion;
        this.ip = ip;
        this.langcode = langcode;
        this.pCode = pCode;
    }

    public String getTerminalBrand() {
        return this.terminalBrand;
    }

    public void setTerminalBrand(String terminalBrand) {
        this.terminalBrand = terminalBrand;
    }

    public String getTerminalSeries() {
        return this.terminalSeries;
    }

    public void setTerminalSeries(String terminalSeries) {
        this.terminalSeries = terminalSeries;
    }

    public String getTerminalApplication() {
        return this.terminalApplication;
    }

    public void setTerminalApplication(String terminalApplication) {
        this.terminalApplication = terminalApplication;
    }

    public String getClient() {
        return this.client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getMac() {
        return this.mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getInstallVersion() {
        return this.installVersion;
    }

    public void setInstallVersion(String installVersion) {
        this.installVersion = installVersion;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLangcode() {
        return langcode;
    }

    public void setLangcode(String langcode) {
        this.langcode = langcode;
    }

    public String getpCode() {
        return pCode;
    }

    public void setpCode(String pCode) {
        this.pCode = pCode;
    }

    public String toString() {
        return "TerminalAuthRequest [ terminalBrand=" + this.terminalBrand + ", terminalSeries=" + this.terminalSeries
                + ", terminalApplication=" + this.terminalApplication + ", client=" + this.client + ", mac=" + this.mac
                + ", installVersion=" + this.installVersion + ", ip=" + this.ip + "]";
    }

}
