package com.atlassian.plugins.tutorial.rest;

import com.atlassian.crowd.embedded.api.Group;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.security.groups.GroupManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A resource of message.
 */
@Path("/usersfromgroup")
public class UsersFromGroup {

    private static final Logger log = LoggerFactory.getLogger(UsersFromGroup.class);

    @GET
    @AnonymousAllowed
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getMessage(@QueryParam("groupname") String groupName)
    {

        Gson gson = new Gson();
        JsonArray dataJson = new JsonArray();

        if (groupName == null || groupName.isEmpty()) {
            return Response.ok(gson.toJson(dataJson)).build();
        }

        GroupManager groupManager = ComponentAccessor.getGroupManager();
        Group group = groupManager.getGroup(groupName);

        if (group == null) {
            return Response.ok(gson.toJson(dataJson)).build();
        }

        Collection<ApplicationUser> appUsers = groupManager.getDirectUsersInGroup(group);

        for (ApplicationUser oneUser : appUsers) {
            JsonObject userJson = new JsonObject();
            userJson.addProperty("label", oneUser.getName());
            userJson.addProperty("value", oneUser.getUsername());

            dataJson.add(userJson);

        }

        return Response.ok(gson.toJson(dataJson)).build();

    }
}