CREATE TABLE tokens(
    id BIGINT AUTO_INCREMENT PRIMARY KEY ,
    token VARCHAR(100) NOT NULL ,
    created DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL ,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_tokens_users
        FOREIGN KEY (user_id) REFERENCES users (id)
                   ON DELETE CASCADE
);