use movieverse;

/* ========================================================================= */
/* USUARIOS ADICIONALES */
/* Password para todos: MovieVerse123! */
/* ========================================================================= */

 VALUES
('cinefilo92', 'cinefilo92@mail.com', '$2b$12$dlZPzvvDWEpnAqB7GJsCNec9KjqoP9PDsh1KKnOB9w1zo/n6EbvmS', 'STANDARD', 'ACTIVE'),
('sofia_reviews', 'sofiareviews@mail.com', '$2b$12$dlZPzvvDWEpnAqB7GJsCNec9KjqoP9PDsh1KKnOB9w1zo/n6EbvmS', 'STANDARD', 'ACTIVE'),
('david_series', 'davidseries@mail.com', '$2b$12$dlZPzvvDWEpnAqB7GJsCNec9KjqoP9PDsh1KKnOB9w1zo/n6EbvmS', 'STANDARD', 'ACTIVE'),
('laura_movies', 'lauramovies@mail.com', '$2b$12$dlZPzvvDWEpnAqB7GJsCNec9KjqoP9PDsh1KKnOB9w1zo/n6EbvmS', 'STANDARD', 'ACTIVE'),
('marco_tv', 'marcotv@mail.com', '$2b$12$dlZPzvvDWEpnAqB7GJsCNec9KjqoP9PDsh1KKnOB9w1zo/n6EbvmS', 'STANDARD', 'ACTIVE'),
('isabel_streaming', 'isabelstream@mail.com', '$2b$12$dlZPzvvDWEpnAqB7GJsCNec9KjqoP9PDsh1KKnOB9w1zo/n6EbvmS', 'STANDARD', 'ACTIVE'),
('alex_popcorn', 'alexpopcorn@mail.com', '$2b$12$dlZPzvvDWEpnAqB7GJsCNec9KjqoP9PDsh1KKnOB9w1zo/n6EbvmS', 'STANDARD', 'ACTIVE'),
('dani_bingewatcher', 'danibinge@mail.com', '$2b$12$dlZPzvvDWEpnAqB7GJsCNec9KjqoP9PDsh1KKnOB9w1zo/n6EbvmS', 'STANDARD', 'ACTIVE'),
('troll_master99', 'troll99@mail.com', '$2b$12$dlZPzvvDWEpnAqB7GJsCNec9KjqoP9PDsh1KKnOB9w1zo/n6EbvmS', 'STANDARD', 'ACTIVE'),
('anti_everything', 'antieverything@mail.com', '$2b$12$dlZPzvvDWEpnAqB7GJsCNec9KjqoP9PDsh1KKnOB9w1zo/n6EbvmS', 'STANDARD', 'ACTIVE'),
('spoiler_king', 'spoilerking@mail.com', '$2b$12$dlZPzvvDWEpnAqB7GJsCNec9KjqoP9PDsh1KKnOB9w1zo/n6EbvmS', 'STANDARD', 'ACTIVE'),
('rageviewer', 'rageviewer@mail.com', '$2b$12$dlZPzvvDWEpnAqB7GJsCNec9KjqoP9PDsh1KKnOB9w1zo/n6EbvmS', 'STANDARD', 'ACTIVE'

/* ========================================================= */
/* REVIEWS NORMALES */
/* ========================================================= */

INSERT INTO reviews
(user_id, media_id, media_type, title, rating, comment, created_at)
VALUES

(3, 986056, 'MOVIE', 'Thunderbolts*', 8,
'Me ha sorprendido para bien. Tiene más personalidad de la que esperaba y el grupo funciona muy bien en pantalla.',
'2026-05-26 18:15:00'),

(5, 94605, 'SERIE', 'Arcane', 10,
'Una de las mejores series de animación que he visto. Visualmente es espectacular.',
'2026-05-26 20:30:00'),

(6, 696506, 'MOVIE', 'Mickey 17', 8,
'Original y entretenida. Robert Pattinson vuelve a demostrar que elige proyectos muy interesantes.',
'2026-05-27 12:10:00'),

(9, 792307, 'MOVIE', 'Pobres criaturas', 9,
'Una propuesta muy diferente. Emma Stone está impresionante y la dirección artística es increíble.',
'2026-05-27 15:45:00'),

(7, 111110, 'SERIE', 'ONE PIECE', 9,
'Sigue manteniendo un nivel altísimo incluso después de tantos años.',
'2026-05-28 11:00:00'


/* ========================================================= */
/* REVIEWS REPORTABLES */
/* ========================================================= */

INSERT INTO reviews
(user_id, media_id, media_type, title, rating, comment, created_at)
VALUES

(11, 986056, 'MOVIE', 'Thunderbolts*', 1,
'Película para NPCs. Si te gusta esto tienes el cerebro completamente lavado.',
'2026-05-28 19:15:00'),

(12, 696506, 'MOVIE', 'Mickey 17', 1,
'Otra basura pretenciosa para pseudointelectuales que quieren sentirse listos.',
'2026-05-28 19:30:00'),

(13, 94997, 'SERIE', 'La casa del dragón', 1,
'Rhaenyra muere al final y Daemon tampoco sobrevive.',
'2026-05-28 19:45:00'),

(11, 1330021, 'MOVIE', 'Criaturas luminosas', 1,
'La protagonista es insufrible y cualquiera que disfrute esta película tiene gustos horribles.',
'2026-05-28 20:00:00'),

(14, 94605, 'SERIE', 'Arcane', 1,
'Todos los fans de esta serie parecen niños de 12 años.',
'2026-05-28 20:15:00'),

(12, 687163, 'MOVIE', 'Proyecto Salvación', 1,
'CRYPTO100.NET GANA DINERO YA CRYPTO100.NET GANA DINERO YA CRYPTO100.NET',
'2026-05-28 20:30:00'


/* ========================================================= */
/* REPORTES */
/* ========================================================= */

INSERT INTO reports
(reporter_id, review_id, reported_user_id, reason, status, created_at)
VALUES

(1, 7, 11,
'Contenido ofensivo',
'PENDING',
'2026-05-29 09:00:00'),

(1, 8, 12,
'Insultos y lenguaje despectivo',
'PENDING',
'2026-05-29 09:05:00'),

(1, 9, 13,
'Spoilers graves',
'PENDING',
'2026-05-29 09:10:00'),

(1, 10, 11,
'Ataques a otros usuarios',
'UNDER_REVIEW',
'2026-05-29 09:20:00'),

(1, 11, 14,
'Insultos a la comunidad',
'UNDER_REVIEW',
'2026-05-29 09:25:00'),

(1, 12, 12,
'Spam',
'RESOLVED',
'2026-05-29 09:30:00'),

(1, 10, 11,
'Opinión polémica pero no incumple las normas',
'REJECTED',
'2026-05-29 09:35:00'

