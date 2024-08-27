USE n1;

CREATE TABLE IF NOT EXISTS users(
                       id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                       email VARCHAR(50) NOT NULL,
                       password VARCHAR(20) NOT NULL,
                       nickname VARCHAR(20) NOT NULL,
                       user_rating FLOAT NOT NULL,
                       profile_image_url VARCHAR(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS reviews (
                         id BIGINT PRIMARY KEY NOT NULL,
                         posts_id BIGINT NOT NULL,
                         to_users_id BIGINT NOT NULL,
                         from_users_id BIGINT NOT NULL,
                         text VARCHAR(500),
                         score VARCHAR(100) NOT NULL,
                         FOREIGN KEY (to_users_id) REFERENCES users(id) ON DELETE CASCADE,
                         FOREIGN KEY (from_users_id) REFERENCES users(id) ON DELETE CASCADE
);