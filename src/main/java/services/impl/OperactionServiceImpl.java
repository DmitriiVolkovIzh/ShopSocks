package services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Operaction;
import models.Socks;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import services.FileService;
import services.OperactionService;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

@Service
public class OperactionServiceImpl implements OperactionService {

    private ArrayList<Operaction> operactions;

    private final FileService operactionFileService;


    public OperactionServiceImpl(@Qualifier("operactionServiceImpl") FileService operactionFileService) {
        this.operactions = new ArrayList<Operaction>();
        this.operactionFileService = operactionFileService;
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
    public void registerTheOperation(Operaction operaction) {
        operactions.add(operaction);
        saveToFile();
    }

    @Override
    public File getTextFile() {
        File file = operactionFileService.createTempFile("Операции").toFile();
        Path path = file.toPath();
        try (Writer writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            for (Operaction operaction : operactions) {
                if (operaction.getTypeOfOperaction().equals(Operaction.TypeOfOperaction.ACCEPTANCE)) {
                    writer.append("Пополнение носков на складе").append("\n");
                } else if (operaction.getTypeOfOperaction().equals(Operaction.TypeOfOperaction.WRITING_OFF)) {
                    writer.append("Списание носков").append("\n");
                } else if (operaction.getTypeOfOperaction().equals(Operaction.TypeOfOperaction.RELEASING)) {
                    writer.append("Реализация носков").append("\n");
                }
                Socks socks = operaction.getSocks().getSocks();
                writer.append(operaction.getDateOfOperation()).append("\n").
                        append("Цвет: ").append(socks.getColor().getNameColor()).append("\n").
                        append("Размер: ").append(String.valueOf(socks.getReallySize())).append("\n").
                        append("Содержание хлопка: ").append(String.valueOf(socks.getComposition())).append("% \n").
                        append(String.valueOf(operaction.getSocks().getSizeSocks())).append("\n").
                        append("Количество: ").append(String.valueOf(operaction.getSocks().getQuantity())).append("\n").
                        append(operaction.getDescription()).append("\n").append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }


    private void saveToFile() {
        try {
            String json = new ObjectMapper().writeValueAsString(operactions);
            operactionFileService.saveToFile(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void readFromFile() {
        try {
            String json = operactionFileService.readFromFile();
            operactions = new ObjectMapper().readValue(json, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }
}

