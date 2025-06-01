package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.service.CityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Контроллер городов", description = "Позволяет получать города")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/coordinates")
public class CityController {

    private final CityService cityService;


    @Operation(summary = "Получение координат города",
        description = "Позволяет получить координаты города по его названию")
    @GetMapping("/{city}")
    public String getCityCoordinates(@PathVariable
                                         @Parameter(name = "Название города", required = true) String city) {
        return cityService.getCityCoordinates(city);
    }
}
