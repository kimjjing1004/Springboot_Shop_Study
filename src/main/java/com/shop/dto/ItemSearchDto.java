package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import lombok.Data;

@Data
public class ItemSearchDto {

    private String searchDateType;

    private ItemSellStatus searchSellStatus;

    private String searchBy;

    private String searchQuery = "";

}
