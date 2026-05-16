USE movieverse;

-- RESET COMPLETO BD --------------------------------------------------------------

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS list_items;
DROP TABLE IF EXISTS media_lists;
DROP TABLE IF EXISTS reports;
DROP TABLE IF EXISTS moderation_actions;
DROP TABLE IF EXISTS audit_logs;
DROP TABLE IF EXISTS likes;
DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS users;

SET FOREIGN_KEY_CHECKS = 1;

-- CREACIÓN DE TABLAS --------------------------------------------------------------

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN','MODERATOR','STANDARD') NOT NULL DEFAULT 'STANDARD',
    status ENUM('ACTIVE','BANNED','DISABLED') NOT NULL DEFAULT 'ACTIVE'


CREATE TABLE reviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    media_id BIGINT NOT NULL,
    media_type VARCHAR(20) NOT NULL,
    rating INT NOT NULL,
    comment TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE


CREATE TABLE media_lists (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE


CREATE TABLE list_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    list_id BIGINT NOT NULL,
    media_id BIGINT NOT NULL,
    added_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (list_id) REFERENCES media_lists(id)
        ON DELETE CASCADE


CREATE TABLE reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    reporter_id BIGINT,
    review_id BIGINT,
    reported_user_id BIGINT,

    reason TEXT,

    status ENUM('PENDING','UNDER_REVIEW','RESOLVED','REJECTED')
        DEFAULT 'PENDING',

    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (reporter_id) REFERENCES users(id)
        ON DELETE SET NULL,

    FOREIGN KEY (review_id) REFERENCES reviews(id)
        ON DELETE SET NULL,

    FOREIGN KEY (reported_user_id) REFERENCES users(id)
        ON DELETE SET NULL


CREATE TABLE likes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    media_id BIGINT NOT NULL,
    media_type ENUM('MOVIE','SERIE') NOT NULL,
    title VARCHAR(255) NOT NULL,
    poster_path VARCHAR(255),
    vote_average DOUBLE,
    user_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_like_user FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT unique_like UNIQUE (user_id, media_id, media_type)



CREATE INDEX idx_likes_user_created
ON likes(user_id, created_at DESC

CREATE TABLE moderation_actions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    moderator_id BIGINT NOT NULL,
    target_user_id BIGINT,
    target_review_id BIGINT,

    action_type ENUM(
        'DELETE_REVIEW',
        'BAN_USER',
        'DISABLE_ACCOUNT',
        'REMOVE_MODERATOR_ROLE'
    ),

    reason TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (moderator_id) REFERENCES users(id)
        ON DELETE CASCADE,

    FOREIGN KEY (target_user_id) REFERENCES users(id)
        ON DELETE SET NULL,

    FOREIGN KEY (target_review_id) REFERENCES reviews(id)
        ON DELETE SET NULL


CREATE TABLE audit_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    moderator_id BIGINT NOT NULL,
    action VARCHAR(100),
    target_id BIGINT,

    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    details TEXT,

    FOREIGN KEY (moderator_id) REFERENCES users(id)
        ON DELETE CASCADE


-- DATOS DE PRUEBA ----------------------------------------------------------------------------------------------------

-- Usuarios prueba | password = "1234"
INSERT INTO users (username, email, password) VALUES
('mara', 'mara@test.com', '$2a$10$QtG2G2tQcNYwn4t33OkiP.78QOpKU6STjGGn.sTcJkvf/qkYONMKy'),
('cinefan', 'cinefan@test.com', '$2a$10$QtG2G2tQcNYwn4t33OkiP.78QOpKU6STjGGn.sTcJkvf/qkYONMKy'),
('critic', 'critic@test.com', '$2a$10$QtG2G2tQcNYwn4t33OkiP.78QOpKU6STjGGn.sTcJkvf/qkYONMKy'

-- Reviews prueba | para Dune (438631)
INSERT INTO reviews (comment, rating, created_at, media_id, media_type, user_id) VALUES
('Gran dirección y fotografía, pero se siente incompleta.', 8, '2025-03-12 18:20:00', 438631, 'MOVIE', 1),
('Muy buena, aunque cuesta seguir el lore si no conoces el libro.', 7, '2024-06-10 21:15:00', 438631, 'MOVIE', 2),
('Visualmente impecable, historia demasiado pausada.', 7, '2023-04-05 19:40:00', 438631, 'MOVIE', 3),
('Buen casting y ambientación, pero esperaba más ritmo.', 6, '2022-08-01 17:10:00', 438631, 'MOVIE', 1),
('Una experiencia audiovisual increíble.', 9, '2022-05-14 22:05:00', 438631, 'MOVIE', 2),
('Muy fiel al libro, pero no es para todo el mundo.', 8, '2022-02-10 20:30:00', 438631, 'MOVIE', 3),
('Buen inicio de saga, aunque se queda corta como película independiente.', 7, '2021-12-05 16:50:00', 438631, 'MOVIE', 1),
('Espectacular en pantalla grande, imprescindible verla en cine.', 9, '2021-11-10 21:00:00', 438631, 'MOVIE', 2),
('Muy ambiciosa, pero algo fría emocionalmente.', 6, '2021-10-25 18:00:00', 438631, 'MOVIE', 3
