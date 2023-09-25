package com.example.mainservice.model;

import com.example.mainservice.enums.StateAction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateEventAdminRequest extends UpdateEventUserRequest {
    private StateAction stateAction;
}
