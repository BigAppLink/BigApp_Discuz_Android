package com.youzu.clan.base.json.config.content;

import com.youzu.android.framework.json.annotation.JSONField;

public class SearchSettingItem {
        private String key;
        private String status;
        private String searchCtrl;
        private String maxSpm;
        private String maxSearchResults;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSearchCtrl() {
            return searchCtrl;
        }

        @JSONField(name = "searchctrl")
        public void setSearchCtrl(String searchCtrl) {
            this.searchCtrl = searchCtrl;
        }

        public String getMaxSpm() {
            return maxSpm;
        }

        @JSONField(name = "maxspm")
        public void setMaxSpm(String maxSpm) {
            this.maxSpm = maxSpm;
        }

        public String getMaxSearchResults() {
            return maxSearchResults;
        }

        @JSONField(name = "maxsearchresults")
        public void setMaxSearchResults(String maxSearchResults) {
            this.maxSearchResults = maxSearchResults;
        }
    }