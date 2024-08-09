USE service_users;
-- DROP TABLE IF EXISTS users;
CREATE TABLE IF NOT EXISTS users(
                       id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                       login_id VARCHAR(50) NOT NULL,
                       password VARCHAR(20) NOT NULL,
                       nickname VARCHAR(20) NOT NULL,
                       user_rating FLOAT NOT NULL,
                       location_bcode INT NOT NULL,
                       location_address VARCHAR(100) NOT NULL,
                       profile_image_url VARCHAR(200) NOT NULL
);
SHOW TABLES;