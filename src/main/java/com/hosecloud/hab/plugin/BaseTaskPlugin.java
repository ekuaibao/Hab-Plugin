package com.hosecloud.hab.plugin;

import com.hosecloud.hab.plugin.cache.CacheService;
import com.hosecloud.hab.plugin.model.Log;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.client.HttpClient;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseTaskPlugin implements TaskPluginInterface {
    @Setter
    protected String corporationId;
    @Setter
    protected HttpClient httpClient;
    @Setter
    protected CacheService cacheService;
    @Getter
    protected List<Log> executeLogs = new ArrayList<>();
}
