package services;

import models.Operaction;

import java.io.File;

public interface OperactionService {
    void registerTheOperation(Operaction operaction);

    File getTextFile();
}
