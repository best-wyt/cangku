package com.wang.yun.common;

import com.wang.yun.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ActiverUser {

    private User user;

    private List<String> roles;

    private List<String> permissions;
}
