package com.youzan.zan.elements;

import java.util.HashMap;
import java.util.List;

public class SqlMapConfig extends HashMap<String, SqlMapConfig.Sql> {
    public class Sql {
        public List<String> getRequire() {
            return require;
        }

        public void setRequire(List<String> require) {
            this.require = require;
        }

        public List<String> getLimit() {
            return limit;
        }

        public void setLimit(List<String> limit) {
            this.limit = limit;
        }

        public String getSql() {
            return sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }

        private List<String> require;
        private List<String> limit;
        private String sql;
    }
}
