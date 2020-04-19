package com.runetide.services.internal.xp.server.resources;

import com.google.inject.Inject;
import com.runetide.services.internal.xp.server.services.XPManager;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/xp")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class XPResource {
    private final XPManager xpManager;

    @Inject
    public XPResource(XPManager xpManager) {
        this.xpManager = xpManager;
    }
}
