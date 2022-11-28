package com.guflimc.brick.leaderboards.common.converters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.guflimc.brick.leaderboards.common.domain.DStatsPodium;
import marcono1234.gson.recordadapter.RecordTypeAdapterFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class PositionListConverter implements AttributeConverter<DStatsPodium.PositionList, String> {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(RecordTypeAdapterFactory.builder().allowMissingComponentValues().create())
            .create();

    @Override
    public String convertToDatabaseColumn(DStatsPodium.PositionList attribute) {
        return gson.toJson(attribute);
    }

    @Override
    public DStatsPodium.PositionList convertToEntityAttribute(String dbData) {
        return gson.fromJson(dbData, DStatsPodium.PositionList.class);
    }
}