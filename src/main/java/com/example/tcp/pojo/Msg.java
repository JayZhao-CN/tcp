package com.example.tcp.pojo;

import lombok.Data;

@Data
public class Msg {

    private String userIdSender;

    private String userIdReceiver;

    private String msg;
}
