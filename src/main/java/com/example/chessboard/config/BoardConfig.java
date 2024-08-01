package com.example.chessboard.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class BoardConfig {
    @Value("${board.width}")
    private int width;

    @Value("${board.height}")
    private int height;

}
