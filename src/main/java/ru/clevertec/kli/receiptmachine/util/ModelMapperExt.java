package ru.clevertec.kli.receiptmachine.util;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;

public class ModelMapperExt extends ModelMapper {

    /**
     * Maps list of source items to list of instances of destinationType.
     * @param sourceItems list of objects to map from
     * @param destinationType type of instances that is expected to be in result list
     * @return list of mapped instances of destinationType
     */
    public <S, D> List<D> mapList(List<S> sourceItems, Class<D> destinationType) {
        return sourceItems.stream()
            .map(category -> map(category, destinationType))
            .collect(Collectors.toList());
    }
}
