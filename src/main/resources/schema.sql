CREATE TABLE game (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE unit (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    game_id BIGINT,
    type VARCHAR(255),
    color VARCHAR(255) NOT NULL,
    x INT,
    y INT,
    destroyed BOOLEAN,
    moves INT,
    last_command_timestamp BIGINT,
    FOREIGN KEY (game_id) REFERENCES game(id)
);

CREATE TABLE command (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    game_id BIGINT,
    unit_id BIGINT,
    type VARCHAR(255),
    details VARCHAR(255),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (game_id) REFERENCES game(id),
    FOREIGN KEY (unit_id) REFERENCES unit(id)
);

CREATE TABLE unit_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    unit_id BIGINT,
    game_id BIGINT,
    x INT,
    y INT,
    destroyed BOOLEAN,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (unit_id) REFERENCES unit(id),
    FOREIGN KEY (game_id) REFERENCES game(id)
);
