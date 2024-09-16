package org.example.smartgarage.dtos.request;

import java.util.List;

public record OrderListInDTO(

        List<Long> serviceTypeIds
) {
}
