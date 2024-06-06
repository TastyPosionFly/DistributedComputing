USE e_commerce;

CREATE TABLE IF NOT EXISTS products
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255)   NOT NULL,
    price       DECIMAL(10, 2) NOT NULL,
    description TEXT
);

INSERT INTO products (name, price, description)
VALUES ('商品A', 99.99, '这是商品A的详细描述'),
       ('商品B', 49.99, '这是商品B的详细描述'),
       ('商品C', 19.99, '这是商品C的详细描述');