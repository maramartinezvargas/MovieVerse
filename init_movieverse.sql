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
DROP TABLE IF EXISTS user_media_status;
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
    title VARCHAR(255) NOT NULL,
    poster_path VARCHAR(500),
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
    moderator_id BIGINT,

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

CREATE TABLE user_media_status (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    media_id BIGINT NOT NULL,
    media_type ENUM('MOVIE','SERIE') NOT NULL,
    title VARCHAR(255) NOT NULL,
    poster_path VARCHAR(255),
    vote_average DOUBLE,
    status ENUM('WATCHED','WATCHLIST') NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_user_media_status_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT uq_user_media_status
        UNIQUE (user_id, media_id, media_type)


CREATE INDEX idx_user_media_status_user_created
ON user_media_status(user_id, created_at DESC

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
('mara', 'mara@test.com', '$2a$10$QtG2G2tQcNYwn4t33OkiP.78QOpKU6STjGGn.sTcJkvf/qkYONMKy'











-- Reviews prueba | para Dune (438631)
INSERT INTO reviews (comment, rating, created_at, media_id, media_type, title, poster_path, user_id)

    'Gran dirección y fotografía, pero se siente incompleta.',
    8,
    '2025-03-12 18:20:00',
    438631,
    'MOVIE',
    'Dune',
    'https://image.tmdb.org/t/p/w500/d5NXSklXo0qyIYkgV94XAgMIckC.jpg',
    1

