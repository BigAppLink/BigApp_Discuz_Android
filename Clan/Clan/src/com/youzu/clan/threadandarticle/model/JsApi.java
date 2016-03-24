package com.youzu.clan.threadandarticle.model;

/**
 * Created by Zhao on 15/11/10.
 */
public class JsApi {
    private String zhaoApi;

    private String api;
    private PostData data;
    private String success;
    private String error;

    public String getZhaoApi() {
        return zhaoApi;
    }

    public void setZhaoApi(String zhaoApi) {
        this.zhaoApi = zhaoApi;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public PostData getData() {
        return data;
    }

    public void setData(PostData data) {
        this.data = data;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
