package services;

import models.Socks;
import models.enums.Color;



public interface SocksService {
    void addSocksStore(Socks socks, Integer quantity);

    int giveSameSocks(String color, Double size, Integer composition);

    void releaseSocksFromStore(Socks socks, Integer quantity);

    void writeOffSocksFromStore(Socks socks, Integer quantity, String cause);

    }
