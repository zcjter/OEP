package com.zijin.oep.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserLoginInRequest {
    private String id;
    private String password;
}
