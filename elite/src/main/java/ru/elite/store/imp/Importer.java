package ru.elite.store.imp;

import ru.elite.store.GalaxyService;
import ru.elite.store.imp.entities.CommanderData;
import ru.elite.store.imp.entities.StarSystemData;

import java.io.IOException;
import java.util.EnumSet;

public interface Importer {


    void addFlag(IMPORT_FLAG flag);
    void removeFlag(IMPORT_FLAG flag);
    void setFlags(EnumSet<IMPORT_FLAG> flags);

    void cancel();
    boolean next() throws IOException;
    StarSystemData getSystem();
    CommanderData getCmdr();

    void imp(GalaxyService galaxyService) throws IOException;


}
