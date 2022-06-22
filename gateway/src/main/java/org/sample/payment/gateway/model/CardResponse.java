package org.sample.payment.gateway.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@JsonIgnoreProperties
@ToString
@Builder
public class CardResponse {

    @NotNull
    private String pan;

    @NotNull
    private String expiry;
}
