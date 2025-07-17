package com.example.messengerquery.mapper;

import com.example.messengerquery.model.User;
import com.example.messengerquery.model.UserDocument;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserDocument toUserDocument(User user);

    User toUser(UserDocument userDocument);
}
