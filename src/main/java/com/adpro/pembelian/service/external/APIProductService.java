package com.adpro.pembelian.service.external;

import com.adpro.pembelian.model.dto.DTOProduct;

import java.util.List;

public interface APIProductService {
    DTOProduct getProductFromAPI(String id);
    List<DTOProduct> getAllProductFromAPI();
}
