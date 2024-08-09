USE service_posts;
-- DROP TABLE IF EXISTS posts;
CREATE TABLE posts (
                       id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                       users_id BIGINT NOT NULL,
                       status INT NOT NULL,
                       group_size INT NOT NULL,
                       cur_group_size INT NOT NULL,
                       chat_id VARCHAR(100) NOT NULL,
                       created_at DATE NOT NULL,
                       closed_at DATE NOT NULL,
                       location_bcode INT NOT NULL,
                       location_address VARCHAR(100) NOT NULL,
                       location_longitude VARCHAR(100) NOT NULL,
                       location_latitude VARCHAR(100) NOT NULL,
                       title VARCHAR(100) NOT NULL,
                       price INT NOT NULL,
                       price_per_user INT NOT NULL,
                       type INT NOT NULL,
                       contents VARCHAR(500) NOT NULL
);
