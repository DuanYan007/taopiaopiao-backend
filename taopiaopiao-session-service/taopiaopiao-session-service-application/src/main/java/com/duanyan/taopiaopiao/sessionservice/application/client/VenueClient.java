package com.duanyan.taopiaopiao.sessionservice.application.client;

import com.duanyan.taopiaopiao.common.response.Result;
import com.duanyan.taopiaopiao.venueservice.api.dto.VenueResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "venue-service", path = "/admin/venues")
public interface VenueClient {

    @GetMapping("/{id}")
    Result<VenueResponse> getVenueById(@PathVariable("id") Long id);
}