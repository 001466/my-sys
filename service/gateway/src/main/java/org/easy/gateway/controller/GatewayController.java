package org.easy.gateway.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.easy.gateway.model.GatewayFilterDefinition;
import org.easy.gateway.model.GatewayPredicateDefinition;
import org.easy.gateway.model.GatewayRouteDefinition;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.springframework.cloud.gateway.actuate.GatewayControllerEndpoint;

@RestController("gatewayController")
@Api(value = "服务网关", tags = { "服务网关" })
public class GatewayController extends org.easy.gateway.controller.GatewayControllerEndpoint {

	public GatewayController(RouteDefinitionLocator routeDefinitionLocator, List<GlobalFilter> globalFilters,
			List<GatewayFilterFactory> GatewayFilters, RouteDefinitionWriter routeDefinitionWriter,
			RouteLocator routeLocator) {
		super(routeDefinitionLocator, globalFilters, GatewayFilters, routeDefinitionWriter, routeLocator);
	}

	@Override
	@PostMapping("/refresh")
	public Mono<Void> refresh() {
		return super.refresh();
	}

	@Override
	@GetMapping("/globalfilters")
	public Mono<HashMap<String, Object>> globalfilters() {
		return super.globalfilters();
	}

	@Override
	@GetMapping("/routefilters")
	public Mono<HashMap<String, Object>> routefilers() {
		return super.routefilers();
	}

	@Override
	@GetMapping("/routes")
	public Mono<List<Map<String, Object>>> routes() {
		return super.routes();
	}

	@PostMapping("/routes/{id}")
	@SuppressWarnings("unchecked")
    @ApiOperation(value="save",notes="save",position = 1)
	@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String", paramType = "path")
	public Mono<ResponseEntity<Void>> save(@PathVariable String id,
			@RequestBody @ApiParam(name = "RouteDefinition", value = "RouteDefinition") @Valid GatewayRouteDefinition route) {
		return super.save(id, Mono.just(assembleRouteDefinition(route)));
	}

	@Override
	@DeleteMapping("/routes/{id}")
	public Mono<ResponseEntity<Object>> delete(@PathVariable String id) {
		return super.delete(id);
	}

	@Override
	@GetMapping("/routes/{id}")
	public Mono<ResponseEntity<RouteDefinition>> route(@PathVariable String id) {
		return super.route(id);
	}

	@Override
	@GetMapping("/routes/{id}/combinedfilters")
	public Mono<HashMap<String, Object>> combinedfilters(@PathVariable String id) {
		return super.combinedfilters(id);
	}

	private RouteDefinition assembleRouteDefinition(GatewayRouteDefinition gwdefinition) {

		if (StringUtils.isEmpty(gwdefinition.getId())) {
			throw new NullPointerException("id can not be null");
		}

		RouteDefinition definition = new RouteDefinition();

		// ID
		definition.setId(gwdefinition.getId());

		// Predicates
		List<PredicateDefinition> pdList = new ArrayList<>();
		for (GatewayPredicateDefinition gpDefinition : gwdefinition.getPredicates()) {

			if (StringUtils.isEmpty(gpDefinition.getName())) {
				throw new NullPointerException("predicates.name can not be null");
			}

			if (gpDefinition.getArgs().size() == 0) {
				throw new NullPointerException("predicates.args can not be null");
			}

			PredicateDefinition predicate = new PredicateDefinition();
			predicate.setName(gpDefinition.getName());
			predicate.setArgs(gpDefinition.getArgs());
			pdList.add(predicate);
		}
		definition.setPredicates(pdList);

		// Filters
		List<FilterDefinition> fdList = new ArrayList<>();
		for (GatewayFilterDefinition gfDefinition : gwdefinition.getFilters()) {

			if (StringUtils.isEmpty(gfDefinition.getName())) {
				throw new NullPointerException("filters.name can not be null");
			}

			if (gfDefinition.getArgs().size() == 0) {
				throw new NullPointerException("filters.args can not be null");
			}

			FilterDefinition filter = new FilterDefinition();
			filter.setName(gfDefinition.getName());
			filter.setArgs(gfDefinition.getArgs());
			fdList.add(filter);
		}
		definition.setFilters(fdList);

		// URI
		URI uri = UriComponentsBuilder.fromUriString(gwdefinition.getUri()).build().toUri();

		if (StringUtils.isEmpty(uri)) {
			throw new NullPointerException("uri can not be null");
		}

		definition.setUri(uri);

		return definition;
	}

}
