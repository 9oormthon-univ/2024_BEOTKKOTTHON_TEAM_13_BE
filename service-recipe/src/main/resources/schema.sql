USE service_recipes;

CREATE TABLE IF NOT EXISTS recipes (
                         id BIGINT PRIMARY KEY NOT NULL,
                         users_id BIGINT NOT NULL,
                         title VARCHAR(100) NOT NULL,
                         contents VARCHAR(500),
                         comment_count INT NOT NULL,
                         likes_count INT NOT NULL,
                         thumbnail_image_path VARCHAR(200) NOT NULL,
                         created_at DATE NOT NULL,
                         type INT NOT NULL

);

CREATE TABLE IF NOT EXISTS recipes_ingredients (
                                     id BIGINT PRIMARY KEY NOT NULL,
                                     name VARCHAR(50) NOT NULL,
                                     amount VARCHAR(20) NOT NULL,
                                     recipes_id BIGINT NOT NULL,
                                     FOREIGN KEY (recipes_id) REFERENCES recipes(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS recipes_processes (
                                   id BIGINT PRIMARY KEY NOT NULL,
                                   image_path VARCHAR(200) NOT NULL,
                                   contents VARCHAR(500) NOT NULL,
                                   recipes_id BIGINT NOT NULL,
                                   FOREIGN KEY (recipes_id) REFERENCES recipes(id) ON DELETE CASCADE
);
