INSERT INTO authority(id, name) VALUES (-1, 'ROLE_AUTHOR');
INSERT INTO authority(id, name) VALUES (-2, 'ROLE_EDITOR');

-- SIFRA JE 'author'
insert into author(id, email, password, firstname, lastname) values (-1, 'autor1@gmail.com', '$2y$12$/2zhcxjmhArGzkzb.idyCuYaz1B54vliIP2cMAP/mGtRBVQkjTxda', 'Petar', 'Petrovic');

INSERT INTO user_authorities(user_id, authorities_id) VALUES (-1, -1);

-- SIFRA JE 'author'
insert into author(id, email, password, firstname, lastname) values (-3, 'autor2@gmail.com', '$2y$12$/2zhcxjmhArGzkzb.idyCuYaz1B54vliIP2cMAP/mGtRBVQkjTxda', 'Nikola', 'Nikolic');
INSERT INTO user_authorities(user_id, authorities_id) VALUES (-3, -1);

-- SIFRA JE 'author'
insert into author(id, email, password, firstname, lastname) values (-4, 'autor3@gmail.com', '$2y$12$/2zhcxjmhArGzkzb.idyCuYaz1B54vliIP2cMAP/mGtRBVQkjTxda', 'Dario', 'Nakuchi');
INSERT INTO user_authorities(user_id, authorities_id) VALUES (-4, -1);

-- SIFRA JE 'author'
insert into author(id, email, password, firstname, lastname) values (-5, 'autor4@gmail.com', '$2y$12$/2zhcxjmhArGzkzb.idyCuYaz1B54vliIP2cMAP/mGtRBVQkjTxda', 'John', 'Smith');
INSERT INTO user_authorities(user_id, authorities_id) VALUES (-5, -1);

-- SIFRA JE 'editor'
insert into editor(id, email, password, firstname, lastname) values (-2, 'editor@gmail.com', '$2y$12$Ehu7J84nE3AMZsQy1dHz4.niDzGVXRBiQENnqYr5Inksp7.ovbXia', 'Darko', 'Darkovic');
INSERT INTO user_authorities(user_id, authorities_id) VALUES (-2, -2);
