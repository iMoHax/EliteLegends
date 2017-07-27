package ru.elite.store.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;

public class EnumSetConverter<T extends Enum<T>> implements AttributeConverter<EnumSet<T>, Collection<String>> {
    private final Class<T> clazz;

    public EnumSetConverter(Class<T> clazz) {
        this.clazz = clazz;
    }


    @Override
    public Collection<String> convertToDatabaseColumn(EnumSet<T> attribute) {
        Collection<String> res = new ArrayList<>();
        for (T item : attribute) {
            res.add(item.name());
        }
        return res;
    }

    @Override
    public EnumSet<T> convertToEntityAttribute(Collection<String> dbData) {
        EnumSet<T> res = EnumSet.noneOf(clazz);
        for (String s : dbData) {
            T item = T.valueOf(clazz, s);
            res.add(item);
        }
        return res;
    }
}
