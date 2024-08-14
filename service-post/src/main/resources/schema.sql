USE n1;
# DROP TABLE IF EXISTS posts;
CREATE TABLE IF NOT EXISTS posts (
                       id BIGINT PRIMARY KEY NOT NULL,
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

CREATE TABLE IF NOT EXISTS posts_ingredients (
                                   id BIGINT PRIMARY KEY NOT NULL,
                                   posts_id BIGINT NOT NULL,
                                   name VARCHAR(500) NOT NULL,
                                   url VARCHAR(300) NOT NULL,
                                   FOREIGN KEY (posts_id) REFERENCES posts(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS posts_images (
                              id BIGINT PRIMARY KEY NOT NULL,
                              posts_id BIGINT NOT NULL,
                              image_path VARCHAR(200) NOT NULL,
                              FOREIGN KEY (posts_id) REFERENCES posts(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS likes_posts (
                             id BIGINT PRIMARY KEY NOT NULL,
                             posts_id BIGINT NOT NULL,
                             users_id BIGINT NOT NULL,
                             FOREIGN KEY (posts_id) REFERENCES posts(id) ON DELETE CASCADE
);