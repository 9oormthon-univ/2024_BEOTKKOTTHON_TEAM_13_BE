USE n1;
CREATE TABLE IF NOT EXISTS recipes (
                         id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                         users_id INT NOT NULL,
                         title VARCHAR(100) NOT NULL,
                         contents VARCHAR(500),
                         comment_count INT NOT NULL,
                         likes_count INT NOT NULL,
                         thumbnail_image_path VARCHAR(200) NOT NULL,
                         created_at DATE NOT NULL,
                         type INT NOT NULL
);

CREATE TABLE IF NOT EXISTS likes_recipes (
                               id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                               recipes_id INT NOT NULL,
                               users_id INT NOT NULL,
                               FOREIGN KEY (recipes_id) REFERENCES recipes(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS recipes_ingredients (
                                     id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                                     name VARCHAR(50) NOT NULL,
                                     amount VARCHAR(20) NOT NULL,
                                     recipes_id INT NOT NULL,
                                     FOREIGN KEY (recipes_id) REFERENCES recipes(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS recipes_processes (
                                   id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                                   image_path VARCHAR(200) NOT NULL,
                                   contents VARCHAR(500) NOT NULL,
                                   recipes_id INT NOT NULL,
                                   FOREIGN KEY (recipes_id) REFERENCES recipes(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS recipes_comments (
                                  id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                                  comment VARCHAR(100) NOT NULL,
                                  created_at date NOT NULL,
                                  parent_comments_id INT,
                                  recipes_id INT NOT NULL,
                                  users_id INT NOT NULL,
                                  FOREIGN KEY (parent_comments_id) REFERENCES recipes_comments(id) ON DELETE CASCADE,
                                  FOREIGN KEY (recipes_id) REFERENCES recipes(id) ON DELETE CASCADE
);
