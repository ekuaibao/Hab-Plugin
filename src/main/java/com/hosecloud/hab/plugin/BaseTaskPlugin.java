package com.hosecloud.hab.plugin;

import com.hosecloud.hab.plugin.api.HoseHttpService;
import com.hosecloud.hab.plugin.cache.CacheService;
import com.hosecloud.hab.plugin.model.Log;
import org.apache.http.client.HttpClient;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseTaskPlugin implements TaskPluginInterface {
    protected String corporationId;
    protected HttpClient httpClient;
    protected CacheService cacheService;
    protected HoseHttpService httpService;
    protected HoseHttpService hoseHttpService;
    protected List<Log> executeLogs = new ArrayList<>();

    @Override
    public void setCorporationId(String corporationId) {
        this.corporationId = corporationId;
    }

    @Override
    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public void setHttpService(HoseHttpService httpService) {
        this.httpService = httpService;
    }

    public void setExecuteLogs(List<Log> executeLogs) {
        this.executeLogs = executeLogs;
    }

    @Override
    public void setHoseHttpService(HoseHttpService hoseHttpService) {
        this.hoseHttpService = hoseHttpService;
    }

    @Override
    public List<Log> getExecuteLogs() {
        return executeLogs;
    }
}
