INSERT INTO CATEGORIAS (name,active)
VALUES ('SERIE',true);
INSERT INTO CATEGORIAS (name,active)
VALUES ('DISNEY',true);
INSERT INTO CATEGORIAS (name,active)
VALUES ('SUPERHEROES',true);
INSERT INTO CATEGORIAS (name,active)
VALUES ('PELICULAS',true);
INSERT INTO CATEGORIAS (name,active)
VALUES ('OTROS',true);

INSERT INTO FUNKOS (name, price, quantity, category_id)
VALUES
  ('Funko 1', 19.99, 5, 1),
  ('Funko 2', 24.99, 3, 2),
  ('Funko 3', 15.99, 8, 3),
  ('Funko 4', 29.99, 2, 1),
  ('Funko 5', 12.99, 6, 4),
  ('Funko 6', 21.99, 4, 2),
  ('Funko 7', 18.99, 7, 3),
  ('Funko 8', 27.99, 1, 1),
  ('Funko 9', 14.99, 9, 4),
  ('Funko 10', 31.99, 3, 2);
--Password: Admin1
insert into USERS(name, username, email, password, is_active)
values ('Admin Admin', 'admin', 'admin@prueba.net',
        '$2a$10$vPaqZvZkz6jhb7U7k/V/v.5vprfNdOnh4sxi/qpPRkYTzPmFlI9p2', true);

insert into USER_ROLES (user_id, roles)
values (1, 'USER');
insert into USER_ROLES (user_id, roles)
values (1, 'ADMIN');

-- Contraseña: User1
insert into USERS(name, username, email, password, is_active)
values ('User User', 'user', 'user@prueba.net',
        '$2a$12$RUq2ScW1Kiizu5K4gKoK4OTz80.DWaruhdyfi2lZCB.KeuXTBh0S.', true);
insert into USER_ROLES (user_id, roles)
values (2, 'USER');