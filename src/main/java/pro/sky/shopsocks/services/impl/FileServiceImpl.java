package pro.sky.shopsocks.services.impl;

import org.springframework.stereotype.Service;
import pro.sky.shopsocks.services.FileService;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileServiceImpl implements FileService {
    @Value("${path.to.data.file}")
    private String dataFilePath;
    @Value("${socks.in.the.store}")
    private String dataFileName;

    @Override
    public File getDataFile(){
        return new File(dataFilePath + "/" + dataFileName);
    }
    @Override
    public void saveToFile(String json){
        try {
            cleanDataFile();
            Files.writeString(Path.of(dataFilePath, dataFileName), json);
        } catch (IOException ignored) {
            throw new RuntimeException();
        }
    }
    @Override
    public  String readFromFile(){
        try {
            return Files.readString(Path.of(dataFilePath, dataFileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void cleanDataFile() {
        try {
            Path path = Path.of(dataFilePath, dataFileName);
            Files.deleteIfExists(path);
            Files.createFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Path createTempFile(String suffix) {
        try{
            return Files.createTempFile(Path.of(dataFilePath), "temp", suffix);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
