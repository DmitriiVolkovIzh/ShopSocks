package pro.sky.shopsocks.services;

import pro.sky.shopsocks.models.Socks;


public interface SocksService {
    void addSocksStore(Socks socks, Integer quantity);

    int giveSameSocks(String color, Double size, Integer composition);

    void releaseSocksFromStore(Socks socks, Integer quantity);

    void writeOffSocksFromStore(Socks socks, Integer quantity, String cause);

    }
