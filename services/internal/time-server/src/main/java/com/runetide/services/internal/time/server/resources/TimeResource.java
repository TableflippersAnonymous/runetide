package com.runetide.services.internal.time.server.resources;

import com.runetide.services.internal.time.common.Time;
import com.runetide.services.internal.time.server.services.ClockTickService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/time")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TimeResource {
    private final ClockTickService clockTickService;

    @Inject
    public TimeResource(final ClockTickService clockTickService) {
        this.clockTickService = clockTickService;
    }

    @GET
    public Time getTime() {
        return new Time(clockTickService.getCurrentTick());
    }

    @POST
    public Time setTime(final Time newTime) {
        clockTickService.setCurrentTick(newTime.getTime());
        return getTime();
    }
}
