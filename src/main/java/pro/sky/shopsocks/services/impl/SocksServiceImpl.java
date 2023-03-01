package pro.sky.shopsocks.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pro.sky.shopsocks.exception.InvalidValueException;
import pro.sky.shopsocks.exception.NotEnoughSocksException;
import pro.sky.shopsocks.models.Operaction;
import pro.sky.shopsocks.models.Socks;
import pro.sky.shopsocks.models.SocksPrototype;
import pro.sky.shopsocks.models.enums.SizeSocks;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pro.sky.shopsocks.services.FileService;
import pro.sky.shopsocks.services.OperactionService;
import pro.sky.shopsocks.services.SocksService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service

public class SocksServiceImpl implements SocksService {

    private ArrayList<SocksPrototype> store;
    private final FileService fileService;

    private final OperactionService operactionService;

    public SocksServiceImpl(@Qualifier("fileServiceImpl")
                            FileService fileService, OperactionService operactionService) {
        this.store = new ArrayList<>();
        this.fileService = fileService;
        this.operactionService = operactionService;
    }

    @PostConstruct
    public void init() {
        try {
            readFromFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addSocksStore(Socks socks, Integer quantity) {
        throwsInvalidValueException(socks, quantity);
        SizeSocks sizeSocks = SizeSocks.checkFitToSize(socks.getReallySize());
        if (!store.isEmpty()) {
            for (SocksPrototype socksPrototype : store) {
                if (socks.getReallySize() == socksPrototype.getSocks().getReallySize() &&
                        socks.getColor().equals(socksPrototype.getSocks().getColor()) &&
                        socks.getComposition() == socksPrototype.getSocks().getComposition()) {
                    socksPrototype.setQuantity(socksPrototype.getQuantity() + quantity);
                    saveToFile();
                    operactionService.registerTheOperation(
                            new Operaction(Operaction.TypeOfOperaction.ACCEPTANCE,
                                    String.valueOf(LocalDateTime.now()),
                                    new SocksPrototype(socks, socksPrototype.getSizeSocks(), quantity),
                                    "Добавление носков на склад"));
                    return;
                }
            }
        }
        store.add(new SocksPrototype(socks, sizeSocks, quantity));
        operactionService.registerTheOperation(
                new Operaction(Operaction.TypeOfOperaction.ACCEPTANCE,
                        String.valueOf(LocalDateTime.now()),
                        new SocksPrototype(socks, sizeSocks, quantity),
                        "Добавление носков на склад"));
        saveToFile();
    }

    @Override
    public int giveSameSocks(String color, Double size, Integer composition) {
        if (color.isEmpty() || color.isBlank() || size < 36 || size > 47 | composition > 100
                || composition < 0) {
            throw new InvalidValueException();
        }
        for (SocksPrototype socksPrototype : store) {
            if (socksPrototype.getSocks().getReallySize() == size && socksPrototype.getSocks().getColor().equals(color.toUpperCase()) &&
                    socksPrototype.getSocks().getComposition() == composition) {
                return socksPrototype.getQuantity();
            }
        }
        return 0;
    }

    @Override
    public void releaseSocksFromStore(Socks socks, Integer quantity) {
        throwsInvalidValueException(socks, quantity);
        if (!store.isEmpty()) {
            for (SocksPrototype socksPrototype : store) {
                if (socks.getReallySize() == socksPrototype.getSocks().getReallySize() &&
                        socks.getColor().equals(socksPrototype.getSocks().getColor()) &&
                        socks.getComposition() == socksPrototype.getSocks().getComposition()) {
                    if (socksPrototype.getQuantity() - quantity < 0) {
                        throw new NotEnoughSocksException();
                    } else {
                        socksPrototype.setQuantity(socksPrototype.getQuantity() - quantity);
                        saveToFile();
                        operactionService.registerTheOperation(
                                new Operaction(Operaction.TypeOfOperaction.RELEASING,
                                        String.valueOf(LocalDateTime.now()),
                                        new SocksPrototype(socks, socksPrototype.getSizeSocks(), quantity), "Товар реализаован"));
                        return;
                    }
                }
            }
        }
        saveToFile();
    }

    @Override
    public void writeOffSocksFromStore(Socks socks, Integer quantity, String cause) {
        throwsInvalidValueException(socks, quantity);
        if (!store.isEmpty()) {
            for (SocksPrototype socksPrototype : store) {
                if (socks.getReallySize() == socksPrototype.getSocks().getReallySize() &&
                        socks.getColor().equals(socksPrototype.getSocks().getColor()) &&
                        socks.getComposition() == socksPrototype.getSocks().getComposition()) {
                    if (socksPrototype.getQuantity() - quantity <= 0) {
                        throw new NotEnoughSocksException();
                    } else {
                        socksPrototype.setQuantity(socksPrototype.getQuantity() - quantity);
                        saveToFile();
                        operactionService.registerTheOperation(
                                new Operaction(Operaction.TypeOfOperaction.WRITING_OFF,
                                        String.valueOf(LocalDateTime.now()),
                                        new SocksPrototype(socks, socksPrototype.getSizeSocks(), quantity),
                                        "Списывание носков со склада \n Причина: " + cause));
                        return;
                    }
                }
            }
        }
        saveToFile();
    }

    private void throwsInvalidValueException(Socks socks, Integer quantity) {
        if (socks.getReallySize() < 36 || socks.getReallySize() > 47 ||
                socks.getComposition() < 0 || socks.getComposition() > 100 || quantity <= 0) {
            throw new InvalidValueException();
        }
    }

    private void saveToFile() {
        try {
            String json = new ObjectMapper().writeValueAsString(store);
            fileService.saveToFile(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void readFromFile() {
        try {
            String json = fileService.readFromFile();
            store = new ObjectMapper().readValue(json, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }

}

