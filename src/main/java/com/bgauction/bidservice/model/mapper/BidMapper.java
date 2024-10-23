package com.bgauction.bidservice.model.mapper;

import com.bgauction.bidservice.model.dto.BidCreationDto;
import com.bgauction.bidservice.model.dto.BidDto;
import com.bgauction.bidservice.model.entity.Bid;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BidMapper {
    Bid bidDtoToBid(BidDto dto);
    Bid bidCreationDtoToBid(BidCreationDto dto);
    BidDto bidToBidDto(Bid entity);
}
