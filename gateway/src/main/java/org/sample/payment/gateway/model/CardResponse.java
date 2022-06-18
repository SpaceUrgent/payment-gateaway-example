package org.sample.payment.gateway.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@NoArgsConstructor
@Getter @Setter
@JsonIgnoreProperties
@ToString
public class CardResponse {

    @NotNull
    private String pan;

    @NotNull
    private String expiry;
}
