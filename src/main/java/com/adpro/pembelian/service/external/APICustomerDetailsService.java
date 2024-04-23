package com.adpro.pembelian.service.external;
import com.adpro.pembelian.model.dto.DTOCustomerDetails;

public interface APICustomerDetailsService {
    DTOCustomerDetails getUserDetailsAPI(String userId);
}
