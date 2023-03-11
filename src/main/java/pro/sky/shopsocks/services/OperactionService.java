package pro.sky.shopsocks.services;

import pro.sky.shopsocks.models.Operaction;

import java.io.File;

public interface OperactionService {
    void registerTheOperation(Operaction operaction);

    File getTextFile();
}
