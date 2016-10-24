package com.youzan.zan.elements;

import java.util.List;
import java.util.Map;

public class ApiConfig {

    public class Mod {
        private String mod;
        private String host;
        private String type;
        private List<Mod> sub;

        public String getMod() {
            return mod;
        }

        public void setMod(String mod) {
            this.mod = mod;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<Mod> getSub() {
            return sub;
        }

        public void setSub(List<Mod> sub) {
            this.sub = sub;
        }
    }

    private Map<String, Mod> dev;
    private Map<String, Mod> test;
    private Map<String, Mod> pre;
    private Map<String, Mod> online;
    private Map<String, Mod> unittest;
    private Map<String, Mod> qatest;
    private Map<String, Mod> pubtest;
    private Map<String, Mod> readonly;

    public Map<String, Mod> getDev() {
        return dev;
    }

    public void setDev(Map<String, Mod> dev) {
        this.dev = dev;
    }

    public Map<String, Mod> getTest() {
        return test;
    }

    public void setTest(Map<String, Mod> test) {
        this.test = test;
    }

    public Map<String, Mod> getPre() {
        return pre;
    }

    public void setPre(Map<String, Mod> pre) {
        this.pre = pre;
    }

    public Map<String, Mod> getOnline() {
        return online;
    }

    public void setOnline(Map<String, Mod> online) {
        this.online = online;
    }

    public Map<String, Mod> getUnittest() {
        return unittest;
    }

    public void setUnittest(Map<String, Mod> unittest) {
        this.unittest = unittest;
    }

    public Map<String, Mod> getQatest() {
        return qatest;
    }

    public void setQatest(Map<String, Mod> qatest) {
        this.qatest = qatest;
    }

    public Map<String, Mod> getPubtest() {
        return pubtest;
    }

    public void setPubtest(Map<String, Mod> pubtest) {
        this.pubtest = pubtest;
    }

    public Map<String, Mod> getReadonly() {
        return readonly;
    }

    public void setReadonly(Map<String, Mod> readonly) {
        this.readonly = readonly;
    }
}
