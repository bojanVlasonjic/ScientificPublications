INSERT INTO authority(id, name) VALUES (-1, 'ROLE_AUTHOR');
INSERT INTO authority(id, name) VALUES (-2, 'ROLE_EDITOR');

-- SIFRA JE 'author'
insert into author(id, email, password, firstname, lastname) values (-1, 'autor@gmail.com', '$2y$12$/2zhcxjmhArGzkzb.idyCuYaz1B54vliIP2cMAP/mGtRBVQkjTxda', 'ivkovic', 'djordje');
INSERT INTO user_authorities(user_id, authorities_id) VALUES (-1, -1);

-- SIFRA JE 'editor'
insert into editor(id, email, password, firstname, lastname) values (-2, 'editor@gmail.com', '$2y$12$Ehu7J84nE3AMZsQy1dHz4.niDzGVXRBiQENnqYr5Inksp7.ovbXia', 'darkovic', 'darko');
INSERT INTO user_authorities(user_id, authorities_id) VALUES (-2, -2);
