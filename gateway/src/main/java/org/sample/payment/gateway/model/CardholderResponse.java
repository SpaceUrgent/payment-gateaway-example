package org.sample.payment.gateway.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@JsonIgnoreProperties
@ToString
@Builder
public class CardholderResponse {

    @NotNull
    private String name;

    @NotNull
    private String email;
}
