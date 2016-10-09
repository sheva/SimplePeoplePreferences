package com.sheva.api;

import com.sheva.data.Food;
import com.sheva.services.FoodDAO;
import io.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Resource class for Food entities.
 *
 * Created by Sheva on 9/28/2016.
 */
@Path("/foodlist")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Api(value = "foodlist", description = "Resource that provides operations with food entities",
        consumes = "application/json | application/xml", produces = "application/json | application/xml")
public class FoodResource {

    private static final Logger logger = Logger.getLogger(FoodResource.class.getName());

    private static final String ID_PATH_PATTERN = "/{id: [0-9]+}";

    private FoodDAO dao = new FoodDAO();

    @Context UriInfo uriInfo;

    @GET
    @ApiOperation(value = "Finds all food entities", notes = "Find all food entities", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success with list of food", response = List.class),
            @ApiResponse(code = 500, message = "Internal server error") }
    )
    public Response findAll() {
        logger.log(Level.FINE, "Find all food request received " + uriInfo.getRequestUri());
        List<Food> list = dao.findAll();
        return Response.ok().entity(new GenericEntity<List<Food>>(list){}).build();
    }

    @GET
    @Path(ID_PATH_PATTERN)
    @ApiOperation(
            value = "Returns food info by specified identifier",
            notes = "Find food by specified id and return food information", response = Food.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success with retrieved information about food", response = Food.class),
            @ApiResponse(code = 404, message = "Food does not exist"),
            @ApiResponse(code = 500, message = "Internal server error") }
    )
    public Response findById(@ApiParam(value = "Index of food entity to be found", required = true) @PathParam("id") int id) {
        logger.log(Level.FINE, String.format("Find request by id=%d received %s", id, uriInfo.getRequestUri()));
        Food food = dao.findById(id);
        return Response.ok().entity(food).build();
    }

    @GET
    @Path("/search")
    @ApiOperation(
            value = "Search for food by parameter",
            notes = "Search for food by specified name and return food collection", response = List.class)
    @ApiParam
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success with retrieved information about food. Multiple entities could be available", response = List.class),
            @ApiResponse(code = 500, message = "Internal server error") }
    )
    public Response searchByName(
            @ApiParam(value = "search substring to be matched with food name") @QueryParam("name") String name) {
        logger.log(Level.FINE, String.format("Search request to find food by name=%s received %s", name, uriInfo.getRequestUri()));
        List<Food> list = dao.findByName(name);
        return Response.ok(new GenericEntity<List<Food>>(list){}).build();
    }

    @PUT
    @Path(ID_PATH_PATTERN)
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @ApiOperation(
            value = "Update food entity.", notes = "Update food entity by specified id",
            consumes = "application/json | application/xml", response = Food.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success with update of food entity", response = Food.class),
            @ApiResponse(code = 400, message = "Invalid data request"),
            @ApiResponse(code = 404, message = "Food does not exist"),
            @ApiResponse(code = 500, message = "Internal server error") }
    )
    public Response updateById(
            @ApiParam(value = "Index of food entity to be updated", required = true) @PathParam("id") int id,
            @ApiParam(value = "Updated food object", required = true) Food food) {
        logger.log(Level.FINE, String.format("Update request by id=%d received %s", id, uriInfo.getRequestUri()));
        Food foodUpdated = dao.updateById(id, food.getName());
        logger.log(Level.FINE, "Food updated " + foodUpdated);
        return Response.ok(foodUpdated).build();
    }
}
