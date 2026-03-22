SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE estado;
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO estado (nombre, activo) VALUES ('EN PROCESO', true);
INSERT INTO estado (nombre, activo) VALUES ('CERRADA', true);

INSERT INTO estado (nombre, activo) VALUES ('PENDIENTE', true);
INSERT INTO estado (nombre, activo) VALUES ('COMPRADO', true);
INSERT INTO estado (nombre, activo) VALUES ('NO ENCONTRADO', true);