package services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.mail.util.BASE64DecoderStream;
import model.dao.AdminUserDao;
import model.dao.AssistanceCallDao;
import model.dto.AssistanceCall;
import model.dto.User;
import rss.model.Feed;
import rss.writer.RSSFeedWriter;
import util.SHA1;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.nio.charset.Charset;

@Path("/")
public class AssistanceService {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        Gson gson = new Gson();
        AssistanceCallDao assistanceCallDao = new AssistanceCallDao();
        JsonArray jsonArray = gson.toJsonTree(assistanceCallDao.getAll()).getAsJsonArray();
        return Response.ok(jsonArray.toString()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response getAllRss() {
        AssistanceCallDao assistanceCallDao = new AssistanceCallDao();
        Feed rssFeeder = Feed.createStandardFeed();
        assistanceCallDao.getAll().forEach(e -> rssFeeder.getMessages().add(e.mapToFeedMessage()));
        RSSFeedWriter writer = new RSSFeedWriter(rssFeeder);
        byte[] bytes = null;
        try {
            bytes = writer.write();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok(bytes).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@PathParam("id") int id) {
        Gson gson = new Gson();
        AssistanceCallDao assistanceCallDao = new AssistanceCallDao();
        JsonObject jsonArray = gson.toJsonTree(assistanceCallDao.get(id)).getAsJsonObject();
        return Response.ok(jsonArray.toString()).build();
    }

    @RolesAllowed("ADMIN")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(AssistanceCall assistanceCall, @HeaderParam("Authorization") String authString) {
        if (!isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } else {
            if (assistanceCall == null) return Response.status(Response.Status.NOT_ACCEPTABLE).build();

            AssistanceCallDao assistanceCallDao = new AssistanceCallDao();
            Integer id = assistanceCallDao.add(assistanceCall);
            if (id == null) return Response.status(Response.Status.NOT_ACCEPTABLE).build();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("success", true);
            jsonObject.addProperty("id", id);

            return Response.ok(jsonObject).build();
        }
    }

    @RolesAllowed("ADMIN")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(AssistanceCall assistanceCall, @HeaderParam("Authorization") String authString) {
        return post(assistanceCall, authString);
    }

    @RolesAllowed("ADMIN")
    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response patch(@PathParam("id") int id, AssistanceCall assistanceCall, @HeaderParam("Authorization") String authString) {
        if (!isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } else {
            AssistanceCallDao assistanceCallDao = new AssistanceCallDao();
            if (!assistanceCallDao.update(id, assistanceCall))
                return Response.status(Response.Status.NOT_ACCEPTABLE).build();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("success", true);
            return Response.ok(jsonObject).build();
        }
    }

    @RolesAllowed("ADMIN")
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") int id, @HeaderParam("Authorization") String authString) {
        if (!isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } else {
            AssistanceCallDao assistanceCallDao = new AssistanceCallDao();
            if (!assistanceCallDao.delete(id))
                return Response.status(Response.Status.NOT_ACCEPTABLE).build();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("success", true);
            return Response.ok(jsonObject).build();
        }

    }

    private boolean isUserAuthenticated(String authString) {

        String decodedAuth = "";
        // Header is in the format "Basic 5tyc0uiDat4"
        // We need to extract data before decoding it back to original string
        System.out.println(authString);
        String[] authParts = authString.split(" ");
        String authInfo = authParts[1];
        // Decode the data back to original string
        byte[] bytes = null;
        bytes = BASE64DecoderStream.decode(authInfo.getBytes(Charset.defaultCharset()));
        decodedAuth = new String(bytes);
        String username = decodedAuth.split(":")[0];
        String password = decodedAuth.split(":")[1];

        AdminUserDao adminUserDao = new AdminUserDao();
        User user = adminUserDao.getByUsername(username);
        if (user.getPassword().equals(SHA1.encryptPassword(password)))
            return true;
        return false;
    }
}
