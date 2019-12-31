package com.runetide.services.internal.loader.server.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.concurrent.ScheduledExecutorService;

@Singleton
public class LoaderCheckService {
    private final ScheduledExecutorService executorService;

    @Inject
    public LoaderCheckService(final ScheduledExecutorService executorService) {
        this.executorService = executorService;
    }

    public void run() {
        executorService.schedul
    }
}
