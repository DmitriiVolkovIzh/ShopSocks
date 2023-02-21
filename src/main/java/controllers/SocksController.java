package controllers;

import exception.InvalidValueException;
import exception.NotEnoughSocksException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import models.Socks;
import models.enums.Color;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.SocksService;

@RestController
@RequestMapping("/socks")
@Tag(name = "Учет носков")
public class SocksController {

    private final SocksService socksService;

    public SocksController(SocksService socksService) {
        this.socksService = socksService;
    }

    @Operation(summary = "Прием носков на склад")
    @PostMapping()
    public ResponseEntity<String> addNewSocks(@RequestBody Socks socks,
                                              @RequestParam @Parameter(description = "Количество носков добавленых на склад")
                                              Integer quantity) {
        try {
            socksService.addSocksStore(socks, quantity);
            return ResponseEntity.ok(socksService.giveSameSocks(String.valueOf(socks.getColor()), socks.getReallySize(), socks.getComposition())
                    + " носки такого типа в магазине");
        } catch (InvalidValueException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/sameSocks/{color}&{size}&{composition}\")")
    public ResponseEntity<String> getQuantityOfTheSameSocks( @RequestParam(name = "Цвет носков")
                                                             @Parameter(description = "Цвет") String color,
                                                             @RequestParam(name = "Размер носков")
                                                             @Parameter(description = "Размеры от 35 до 47") double size,
                                                             @RequestParam(name = "Процентное содержание хлопка в составе")
                                                             @Parameter(description = "Процентное содержание хлопка в составе от 0 до 100") int composition) {
        try {
            int quantity = socksService.giveSameSocks(color, size, composition);
            return ResponseEntity.ok(quantity
                    + " пар носков такого типа");
        } catch (InvalidValueException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @Operation(summary = "Реализация носков со склада")
    @PutMapping("/release")
    public ResponseEntity<Object> releaseTheSocks(@RequestBody Socks socks,
                                                   @RequestParam @Parameter(description = "Количество носков отгруженны со склада")
                                                   Integer quantity) {
        try {
            socksService.releaseSocksFromStore(socks, quantity);
        } catch (NotEnoughSocksException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (InvalidValueException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(socksService.giveSameSocks(String.valueOf(socks.getColor()), socks.getReallySize(), socks.getComposition())
                + " остаток носков данного цвета");
    }

    @Operation(summary = "Списание носков со склада")
    @DeleteMapping("/write-off")
    public ResponseEntity<Object> writeOffSocks(@RequestBody(required = false) Socks socks,
                                                @RequestParam @Parameter(description = "Количество носков подлежащих списанию")
                                                Integer quantity,
                                                @RequestParam(name = "Причина списания носков")
                                                @Parameter(description = "Опишите проблему. Почему носки следует списать")
                                                String cause) {
          try {
            socksService.writeOffSocksFromStore(socks, quantity, cause);
        } catch (NotEnoughSocksException | InvalidValueException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body("Дефектные носки успешно списаны");
    }

}
